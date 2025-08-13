package com.example.comp200pt1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.auth.AuthManager;
import com.example.comp200pt1.auth.User;

public class MainActivity extends AppCompatActivity {

    // Login form fields
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // connect inputs and button
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button signInbutton = findViewById(R.id.signInButton);

        // Validate when sign in button pressed, then attempt to authenticate
        signInbutton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // inputs must be filled
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "Please enter both Email and Password", Toast.LENGTH_SHORT).show();
                return;
            }

            // AuthManager checks SQLite users table, stores sessions if correct
            boolean ok = AuthManager.signIn(this, email, password);
            if (!ok) {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                return;
            }

            User u = AuthManager.getCurrentUser(this);
            if (u == null) {
                Toast.makeText(this, "Login error", Toast.LENGTH_SHORT).show();
                return;
            }

            // go straight to the correct dashboard
            if ("staff".equalsIgnoreCase(u.role)) {
                startActivity(new Intent(this, StaffDashboardActivity.class));
            } else {
                startActivity(new Intent(this, MemberDashboardActivity.class));
            }
            finish();
        });
    }
}
