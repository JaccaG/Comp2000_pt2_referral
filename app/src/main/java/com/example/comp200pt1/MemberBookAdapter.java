package com.example.comp200pt1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.comp200pt1.db.DatabaseWrapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MemberBookAdapter extends ArrayAdapter<Book> {

    // Adapter draws each book row for members catalogue screen
    private final Context context;
    private final List<Book> books;
    private final DatabaseWrapper db;
    private final String username;

    public MemberBookAdapter(Context context, List<Book> books, DatabaseWrapper db, String username) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
        this.db = db;
        this.username = username;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Layout inflater for custom row
        if (convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.book_item_member, parent, false);

        Book book = books.get(position);

        TextView title = convertView.findViewById(R.id.bookTitle);
        TextView author = convertView.findViewById(R.id.bookAuthor);
        TextView statusBadge = convertView.findViewById(R.id.statusBadge);
        Button requestBtn = convertView.findViewById(R.id.requestButton);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());

        // Badge changes based on availability
        boolean isAvailable = book.isAvailable();
        statusBadge.setText(book.getStatus());
        statusBadge.setBackgroundResource(isAvailable ? R.drawable.badge_available : R.drawable.badge_checked_out);

        // Only allow request if: book is available AND this user doesn’t already have request for this title.
        boolean already = db.hasActiveRequest(username, book.getTitle());
        boolean enabled = isAvailable && !already;
        requestBtn.setEnabled(enabled);
        requestBtn.setAlpha(enabled ? 1f : 0.5f);
        requestBtn.setText(already ? context.getString(R.string.requested) : context.getString(R.string.request));

        // User taps Request, confirm dialog box appears before inserting into SQLite.
        requestBtn.setOnClickListener(v -> showRequestDialog(book, requestBtn));

        return convertView;
    }

    private void showRequestDialog(Book book, Button requestBtn) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_request_book, null, false);

        TextView title = dialogView.findViewById(R.id.requestTitle);
        TextView message = dialogView.findViewById(R.id.requestMessage);
        Button confirm = dialogView.findViewById(R.id.confirmBtn);
        Button cancel = dialogView.findViewById(R.id.cancelBtn);

        title.setText(context.getString(R.string.request_book_title));
        message.setText(context.getString(R.string.request_book_message, book.getTitle()));

        AlertDialog dlg = new AlertDialog.Builder(context).setView(dialogView).create();

        confirm.setOnClickListener(v -> {
            //Store request dates as yyyy-mm-dd to sort/filter later
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


            // Add request for staff to view later
            db.addRequest(username, book.getTitle(), today);

            // Reflect state in UI for responsiveness
            requestBtn.setEnabled(false);
            requestBtn.setAlpha(0.5f);
            requestBtn.setText(context.getString(R.string.requested));
            Toast.makeText(context, context.getString(R.string.request_sent_toast), Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        });
        cancel.setOnClickListener(v -> dlg.dismiss());

        // Removes sharp white corners from dialog box (ugly)
        dlg.show();
        if (dlg.getWindow() != null) {
            dlg.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
}
