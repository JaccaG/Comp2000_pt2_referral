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

public class EditMemberActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        // Bind views
        ImageButton backButton = findViewById(R.id.backButton);
        EditText inputFullName = findViewById(R.id.inputFullName);
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputContact = findViewById(R.id.inputContact);
        EditText inputMemberSince = findViewById(R.id.inputMemberSince);
        Button btnSave = findViewById(R.id.btnSave);

        // Prefill from the passed member through the intent - presuming it exists (it should </3)
        Member m = (Member) getIntent().getSerializableExtra("member");
        if (m != null) {
            inputFullName.setText(m.getFullName());
            inputEmail.setText(m.getEmail());
            if (m.getContact() != null) inputContact.setText(m.getContact());
            if (m.getMemberSince() != null) inputMemberSince.setText(m.getMemberSince());
        }

        // Date picker for member since field
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        inputMemberSince.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, y, mo, d) -> {
                        Calendar chosen = Calendar.getInstance();
                        chosen.set(y, mo, d);
                        inputMemberSince.setText(df.format(chosen.getTime()));
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // BNack to previous screen
        backButton.setOnClickListener(v -> finish());

        // confirmation of saved changes toast feedback
        btnSave.setOnClickListener(v -> {
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
