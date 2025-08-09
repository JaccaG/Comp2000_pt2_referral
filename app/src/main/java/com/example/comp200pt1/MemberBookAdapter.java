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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Adapter for the member book list to request books
public class MemberBookAdapter extends ArrayAdapter<Book> {

    private final Context context;
    private final List<Book> books;
    private final Set<String> requested = new HashSet<>();

    public MemberBookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override

    // Inflate to custom layout view for list
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.book_item_member, parent, false);
        }

        Book book = books.get(position);

        // Bind views
        TextView title = convertView.findViewById(R.id.bookTitle);
        TextView author = convertView.findViewById(R.id.bookAuthor);
        TextView statusBadge = convertView.findViewById(R.id.statusBadge);
        Button requestBtn = convertView.findViewById(R.id.requestButton);

        // Populate fields TODO use real data from API instead of sample
        title.setText(book.getTitle());
        author.setText(book.getAuthor());

        // Badge status set based on availability
        String status = book.getStatus();
        boolean isAvailable = "Available".equalsIgnoreCase(status);
        statusBadge.setText(status);
        statusBadge.setBackgroundResource(isAvailable ? R.drawable.badge_available : R.drawable.badge_checked_out);

        // Button state - disabled if not available or already requested
        boolean alreadyRequested = requested.contains(book.getTitle());
        requestBtn.setEnabled(isAvailable && !alreadyRequested);
        requestBtn.setAlpha(requestBtn.isEnabled() ? 1f : 0.5f);
        requestBtn.setText(alreadyRequested ? context.getString(R.string.requested) : context.getString(R.string.request));

        // Open confirmation custom dialog
        requestBtn.setOnClickListener(v -> showRequestDialog(book, requestBtn));

        return convertView;
    }

    // Build custom dialog confirmation for book request
    private void showRequestDialog(Book book, Button requestBtn) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_request_book, null, false);

        TextView title = dialogView.findViewById(R.id.requestTitle);
        TextView message = dialogView.findViewById(R.id.requestMessage);
        Button confirm = dialogView.findViewById(R.id.confirmBtn);
        Button cancel = dialogView.findViewById(R.id.cancelBtn);

        title.setText(context.getString(R.string.request_book_title));
        message.setText(context.getString(R.string.request_book_message, book.getTitle()));

        AlertDialog dlg = new AlertDialog.Builder(context).setView(dialogView).create();

        // Confirmation button - remember request and lock request button
        confirm.setOnClickListener(v -> {
            requested.add(book.getTitle());
            requestBtn.setEnabled(false);
            requestBtn.setAlpha(0.5f);
            requestBtn.setText(context.getString(R.string.requested));
            Toast.makeText(context, context.getString(R.string.request_sent_toast), Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        });

        // Cancel button - closes dialog
        cancel.setOnClickListener(v -> dlg.dismiss());

        // Rounds the dialog corners instead of white corners not rounded
        dlg.show();
        if (dlg.getWindow() != null) {
            dlg.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
}
