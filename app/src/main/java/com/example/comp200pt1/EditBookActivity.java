package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditBookActivity extends AppCompatActivity {

    private EditText inputTitle;
    private EditText inputAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        // Bind views
        ImageButton back = findViewById(R.id.backButton);
        inputTitle = findViewById(R.id.inputTitle);
        inputAuthor = findViewById(R.id.inputAuthor);
        EditText inputIsbn = findViewById(R.id.inputIsbn);
        CheckBox checkboxAvailable = findViewById(R.id.checkboxAvailable);
        Button btnSave = findViewById(R.id.btnSaveBook);

        // Prefill fields from the book sent from intent
        Book b = (Book) getIntent().getSerializableExtra("book");
        if (b != null) {
            inputTitle.setText(b.getTitle());
            inputAuthor.setText(b.getAuthor());
            inputIsbn.setText(b.getIsbn().isEmpty() ? "978-0000000000" : b.getIsbn());
            checkboxAvailable.setChecked(b.isAvailable());
        }

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Save changes button/checker
        btnSave.setOnClickListener(v -> {
            if (text(inputTitle).isEmpty() || text(inputAuthor).isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Helper: get trimmed text safely
    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
}
