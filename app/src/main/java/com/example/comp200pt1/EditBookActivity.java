package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.db.DatabaseWrapper;

public class EditBookActivity extends AppCompatActivity {

    // Form inputs
    private EditText inputTitle;
    private EditText inputAuthor;
    private EditText inputIsbn;
    private CheckBox checkboxAvailable;

    private long bookId = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        // Connect views + inputs
        ImageButton back = findViewById(R.id.backButton);
        inputTitle = findViewById(R.id.inputTitle);
        inputAuthor = findViewById(R.id.inputAuthor);
        inputIsbn = findViewById(R.id.inputIsbn);
        checkboxAvailable = findViewById(R.id.checkboxAvailable);
        Button btnSave = findViewById(R.id.btnSaveBook);

        // Read the book id passed in through intent for specific row
        bookId = getIntent().getLongExtra("book_id", -1L);

        // DB helper so can load/update book
        DatabaseWrapper ds = new DatabaseWrapper(this);

        if (bookId != -1L) {
            // If id present - prefill form with book details
            Book b = ds.getById(bookId);
            if (b != null) {
                inputTitle.setText(b.getTitle());
                inputAuthor.setText(b.getAuthor());
                inputIsbn.setText(b.getIsbn());
                checkboxAvailable.setChecked(b.isAvailable());
            } else {
                // If book not found in DB
                Toast.makeText(this, "Book not found.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            // no id, tries to prefill from what is there (if anything)
            String t = getIntent().getStringExtra("title");
            String a = getIntent().getStringExtra("author");
            String i = getIntent().getStringExtra("isbn");
            boolean av = getIntent().getBooleanExtra("available", true);

            inputTitle.setText(t);
            inputAuthor.setText(a);
            inputIsbn.setText(i);
            checkboxAvailable.setChecked(av);
        }

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Save updates back to SQLite
        btnSave.setOnClickListener(v -> {
            // Read current values from the form
            String title = text(inputTitle);
            String author = text(inputAuthor);
            String isbn = text(inputIsbn);
            boolean available = checkboxAvailable.isChecked();

            // Require minimum title + author
            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Please fill in title and author.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (bookId == -1L) {
                Toast.makeText(this, "Missing book id.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Do the update - rows > 0 = success
            int rows = ds.updateById(bookId, title, author, isbn, available);
            if (rows > 0) {
                Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper - get trimmed text
    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
}
