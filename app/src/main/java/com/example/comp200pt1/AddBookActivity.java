package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.db.DatabaseWrapper;

public class AddBookActivity extends AppCompatActivity {

    // Form inputs
    private EditText inputTitle, inputAuthor, inputIsbn;
    private CheckBox checkboxAvailable;

    // Simple wrapper to communicate with with the local SQLite DB
    private DatabaseWrapper books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Get the DB helper ready (so I can insert a book)
        books = new DatabaseWrapper(this);

        // Wire up the views from XML
        ImageButton back = findViewById(R.id.backButton);
        inputTitle = findViewById(R.id.inputTitle);
        inputAuthor = findViewById(R.id.inputAuthor);
        inputIsbn = findViewById(R.id.inputIsbn);
        checkboxAvailable = findViewById(R.id.checkboxAvailable);
        Button btnAdd = findViewById(R.id.btnAddBook);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Save button: read fields, validate, then insert into DB
        btnAdd.setOnClickListener(v -> {
            // Pull the text out of the inputs
            String title = text(inputTitle);
            String author = text(inputAuthor);
            String isbn = text(inputIsbn);
            boolean available = checkboxAvailable.isChecked(); // true = available

            // Basic validation: needs minimum title+author
            if (title.isEmpty() || author.isEmpty()) {
                Toast.makeText(this, "Please fill in title and author.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Try to insert. Success or failure message depends on outcome
            long id = books.add(title, author, isbn, available);

            if (id > 0) {
                // Success path: quick toast then back too the list
                Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // If failure show this toast
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
}
