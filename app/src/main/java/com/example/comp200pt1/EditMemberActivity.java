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
import com.example.comp200pt1.api.dto.UpdateMemberRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        // Connect views + inputs
        ImageButton backButton      = findViewById(R.id.backButton);
        EditText inputUsername      = findViewById(R.id.inputUsername);
        EditText inputFirstName     = findViewById(R.id.inputFirstName);
        EditText inputLastName      = findViewById(R.id.inputLastName);
        EditText inputEmail         = findViewById(R.id.inputEmail);
        EditText inputContact       = findViewById(R.id.inputContact);
        EditText inputMemberEndDate = findViewById(R.id.inputMemberEndDate);
        Button btnSave              = findViewById(R.id.btnSave);

        // Prefill the form from Intent extras (sent from the list row)
        String username = getIntent().getStringExtra("username");
        String firstname = getIntent().getStringExtra("firstname");
        String lastname = getIntent().getStringExtra("lastname");
        String email = getIntent().getStringExtra("email");
        String contact = getIntent().getStringExtra("contact");
        String membershipEndDate = getIntent().getStringExtra("membershipEndDate");

        if (username != null)  inputUsername.setText(username);
        if (firstname != null) inputFirstName.setText(firstname);
        if (lastname != null)  inputLastName.setText(lastname);
        if (email != null)     inputEmail.setText(email);
        if (contact != null)   inputContact.setText(contact);
        if (membershipEndDate != null) inputMemberEndDate.setText(toYMD(membershipEndDate)); // normalise any date strings

        // Simple date picker: writes back as yyyy-mm-dd so API will accept correct format
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        inputMemberEndDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, y, mo, d) -> {
                        Calendar chosen = Calendar.getInstance();
                        chosen.set(y, mo, d);
                        inputMemberEndDate.setText(df.format(chosen.getTime()));
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Back to previous screen
        backButton.setOnClickListener(v -> finish());

        // Save + validates fields, builds request, calls Retrofit, shows result - in that order
        btnSave.setOnClickListener(v -> {
            String u  = safe(text(inputUsername));
            String fn = safe(text(inputFirstName));
            String ln = safe(text(inputLastName));
            String em = safe(text(inputEmail));
            String ct = safe(text(inputContact));
            String ed = safe(text(inputMemberEndDate));

            // Checks so it will actually be valid for API format
            if (u.isEmpty())  { toast("Username is required"); return; }
            if (fn.isEmpty()) { toast("First name is required"); return; }
            if (ln.isEmpty()) { toast("Last name is required"); return; }
            if (em.isEmpty()) { toast("Email is required"); return; }
            if (ed.isEmpty()) { toast("Membership end date is required"); return; }

            // Make sure the date is in the format API accepts
            ed = toYMD(ed);

            UpdateMemberRequest body = new UpdateMemberRequest(fn, ln, em, ct, ed);
            LibraryApi api = ApiClient.get();

            api.updateMember(u, body).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ApiMessage> call, @NonNull Response<ApiMessage> response) {
                    // Server response - Failure message, try to get useful information
                    if (!response.isSuccessful()) {
                        String msg = errorText(response);
                        toast("Update failed (" + response.code() + ")" + (msg.isEmpty() ? "" : ": " + msg));
                        return;
                    }
                    // Confirm the call worked, closes screen and refreshes list
                    setResult(RESULT_OK);
                    toast("Changes Saved");
                    finish();
                }

                // Network error message, for debugging
                @Override
                public void onFailure(@NonNull Call<ApiMessage> call, @NonNull Throwable t) {
                    toast("Network error: " + t.getClass().getSimpleName());
                }
            });
        });
    }

    // Helpers — just keeping stuff tidy below

    private String text(EditText e) {
        return e.getText() == null ? "" : e.getText().toString().trim();
    }

    private String safe(String s) { return s == null ? "" : s; }

    private void toast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }

    // Convert things like "Sun, 10 Aug 2025 00:00:00 GMT" into 2025-08-10, so the API accepts it
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

    // Pulls any error message the server sent back so I can show it in the toast
    private String errorText(Response<?> r) {
        try {
            return r.errorBody() == null ? "" : r.errorBody().string();
        } catch (IOException e) {
            return "";
        }
    }
}
