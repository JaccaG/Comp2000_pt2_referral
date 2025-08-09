package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {

    // Inputs on the form
    private EditText inputTitle, inputAuthor, inputIsbn;
    private CheckBox checkboxAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Bind views
        ImageButton back = findViewById(R.id.backButton);
        inputTitle = findViewById(R.id.inputTitle);
        inputAuthor = findViewById(R.id.inputAuthor);
        inputIsbn = findViewById(R.id.inputIsbn);
        checkboxAvailable = findViewById(R.id.checkboxAvailable);
        Button btnAdd = findViewById(R.id.btnAddBook);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Add book action
        btnAdd.setOnClickListener(v -> {
            String title = text(inputTitle);
            String author = text(inputAuthor);
            String isbn = text(inputIsbn);

            // Simple input validation
            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Read availability choice and give feedback
            String status = checkboxAvailable.isChecked() ? "Available" : "Not available";
            Toast.makeText(this, "Book added: " + status, Toast.LENGTH_SHORT).show();

            finish();
        });
    }

    // Helper: get trimmed text safely
    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
}
