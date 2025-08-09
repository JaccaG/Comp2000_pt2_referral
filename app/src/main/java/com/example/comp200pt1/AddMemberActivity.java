package com.example.comp200pt1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddMemberActivity extends AppCompatActivity {

    // Inputs on the form
    private EditText inputFullName, inputEmail, inputContact, inputMemberSince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        // Bind views
        ImageButton backButton = findViewById(R.id.backButton);
        inputFullName = findViewById(R.id.inputFullName);
        inputEmail = findViewById(R.id.inputEmail);
        inputContact = findViewById(R.id.inputContact);
        inputMemberSince = findViewById(R.id.inputMemberSince);
        Button btnAddMember = findViewById(R.id.btnAddMember);

        // Pre-fill date with today; tap to choose a different date
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar today = Calendar.getInstance();
        inputMemberSince.setText(df.format(today.getTime()));

        inputMemberSince.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, y, m, d) -> {
                        Calendar chosen = Calendar.getInstance();
                        chosen.set(y, m, d);
                        inputMemberSince.setText(df.format(chosen.getTime()));
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Back to previous screen
        backButton.setOnClickListener(v -> finish());

        // Add member action
        btnAddMember.setOnClickListener(v -> {
            String name = text(inputFullName);
            String email = text(inputEmail);
            String contact = text(inputContact);

            // Simple input check
            if (name.isEmpty() || email.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Feedback for success
            Toast.makeText(this, "Member Added", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Helper: get trimmed text safely
    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
}
