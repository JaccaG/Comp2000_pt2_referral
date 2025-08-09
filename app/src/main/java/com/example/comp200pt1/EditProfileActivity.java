package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText inputFullName;
    private EditText inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Bind views
        ImageButton back = findViewById(R.id.backButton);
        inputFullName = findViewById(R.id.inputFullName);
        inputEmail = findViewById(R.id.inputEmail);
        EditText inputContact = findViewById(R.id.inputContact);
        CheckBox checkboxEmailNotifs = findViewById(R.id.checkboxEmailNotifs);
        Button btnSave = findViewById(R.id.btnSave);

        // Fill with sample details
        inputFullName.setText(getString(R.string.profile_full_name_default));
        inputEmail.setText(getString(R.string.profile_email_default));
        inputContact.setText(getString(R.string.profile_contact_default));
        checkboxEmailNotifs.setChecked(true);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // confirmation of saved changes toast feedback and field check
        btnSave.setOnClickListener(v -> {
            if (text(inputFullName).isEmpty() || text(inputEmail).isEmpty()) {
                Toast.makeText(this, "Please fill in name and email.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Helper: read trimmed text safely
    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }
}





// TODO: Not lose mind