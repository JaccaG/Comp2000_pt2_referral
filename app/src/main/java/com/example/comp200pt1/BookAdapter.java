package com.example.comp200pt1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// Adapter for the staff book list
public class BookAdapter extends ArrayAdapter<Book> {

    private final Context context;
    private final List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Book book = books.get(position);

        // Inflate the row into item layout I made
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        }

        // Bind views
        TextView title = convertView.findViewById(R.id.bookTitle);
        TextView author = convertView.findViewById(R.id.bookAuthor);
        TextView statusBadge = convertView.findViewById(R.id.statusBadge);
        ImageButton editButton = convertView.findViewById(R.id.editButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);

        // Populate row with data
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        statusBadge.setText(book.getStatus());

        // Set badge style based on availability
        if ("Available".equalsIgnoreCase(book.getStatus())) {
            statusBadge.setBackgroundResource(R.drawable.badge_available);
        } else {
            statusBadge.setBackgroundResource(R.drawable.badge_checked_out);
        }

        // Edit: open the edit screen and pass the selected book
        editButton.setOnClickListener(v -> {
            Intent i = new Intent(context, EditBookActivity.class);
            i.putExtra("book", book);
            context.startActivity(i);
        });

        // Delete: show a confirmation dialog
        deleteButton.setOnClickListener(v -> showDeleteDialog(book));

        return convertView;
    }

    // Build and show the delete confirmation dialog
    private void showDeleteDialog(Book book) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_book, null, false);

        TextView title = dialogView.findViewById(R.id.deleteTitle);
        TextView msg = dialogView.findViewById(R.id.deleteMessage);
        Button confirm = dialogView.findViewById(R.id.confirmDeleteBtn);
        Button cancel = dialogView.findViewById(R.id.cancelDeleteBtn);

        // Fill dialog text from resources
        title.setText(context.getString(R.string.delete_book_title));
        msg.setText(context.getString(R.string.delete_book_message, book.getTitle()));
        confirm.setText(context.getString(R.string.delete_book_confirm));
        cancel.setText(context.getString(R.string.delete_book_cancel));

        AlertDialog dlg = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        // Confirm: remove the item and refresh the list
        confirm.setOnClickListener(v -> {
            books.remove(book);
            notifyDataSetChanged();
            Toast.makeText(context, context.getString(R.string.deleted_book_toast, book.getTitle()), Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        });

        // Cancel: just closes the dialog box
        cancel.setOnClickListener(v -> dlg.dismiss());

        // Match the rounded dialog layout - had nasty white corners (ugly)
        dlg.show();
        if (dlg.getWindow() != null) {
            dlg.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }
}
