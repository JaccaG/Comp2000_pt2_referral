package com.example.comp200pt1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.api.ApiClient;
import com.example.comp200pt1.api.LibraryApi;
import com.example.comp200pt1.api.dto.ApiMessage;
import com.example.comp200pt1.api.dto.CreateMemberRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberActivity extends AppCompatActivity {
    private EditText inputUsername, inputFirstName, inputLastName,
            inputEmail, inputContact, inputMemberEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        ImageButton backButton = findViewById(R.id.backButton);

        // Connect inputs
        inputUsername      = findViewById(R.id.inputUsername);
        inputFirstName     = findViewById(R.id.inputFirstName);
        inputLastName      = findViewById(R.id.inputLastName);
        inputEmail         = findViewById(R.id.inputEmail);
        inputContact       = findViewById(R.id.inputContact);
        inputMemberEndDate = findViewById(R.id.inputMemberEndDate);
        Button btnAddMember = findViewById(R.id.btnAddMember);

        // default date (current day)
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar today = Calendar.getInstance();
        inputMemberEndDate.setText(df.format(today.getTime()));
        inputMemberEndDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, y, m, d) -> {
                        Calendar chosen = Calendar.getInstance();
                        chosen.set(y, m, d);
                        inputMemberEndDate.setText(df.format(chosen.getTime()));
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        backButton.setOnClickListener(v -> finish());

        // On add member button pressed - build request body eventually sending to API
        btnAddMember.setOnClickListener(v -> {
            String username = text(inputUsername);
            String first    = text(inputFirstName);
            String last     = text(inputLastName);
            String email    = text(inputEmail);
            String contact  = text(inputContact);
            String endDate  = text(inputMemberEndDate);

            if (username.isEmpty() || first.isEmpty() || last.isEmpty()
                    || email.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // API wants yyyy-mm-dd, so make string input as API wants
            endDate = toYMD(endDate);

            CreateMemberRequest body = new CreateMemberRequest(
                    username, first, last, email, contact, endDate
            );

            LibraryApi api = ApiClient.get();
            api.createMember(body).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ApiMessage> call, @NonNull Response<ApiMessage> response) {
                    if (!response.isSuccessful()) {
                        // If the username already exists, tell the user, otherwise show the HTTP code
                        String extra = errorText(response); // try to pull server’s error message
                        String msg = (response.code() == 409)
                                ? "Username already exists"
                                : "Add failed (" + response.code() + ")";
                        if (!extra.isEmpty()) msg += ": " + extra;
                        Toast.makeText(AddMemberActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Success - activity finish + refresh previous screen with newly added member on list
                    Toast.makeText(AddMemberActivity.this, "Member Added", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<ApiMessage> call, @NonNull Throwable t) {
                    Toast.makeText(AddMemberActivity.this,
                            "Network error: " + t.getClass().getSimpleName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Grab text safely from an EditText (avoids nulls and trims spaces)
    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }

    // If the date comes in like “Sun, 10 Aug 2025 00:00:00 GMT”, makes the date 2025-08-10 to appease the API
    private String toYMD(String raw) {
        if (raw == null) return "";
        String t = raw.trim();
        if (t.matches("^\\d{4}-\\d{2}-\\d{2}$")) return t;
        try {
            SimpleDateFormat in = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.UK);
            Date d = in.parse(t);
            if (d != null) {
                return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(d);
            }
        } catch (Exception ignored) { }
        return t;
    }

    // Pull a readable error from the server response
    private String errorText(Response<?> r) {
        try {
            return r.errorBody() == null ? "" : r.errorBody().string();
        } catch (IOException e) {
            return "";
        }
    }
}
