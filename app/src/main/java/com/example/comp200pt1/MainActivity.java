package com.example.comp200pt1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Login inputs
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button signInbutton = findViewById(R.id.signInButton);

        // Sign in buttons
        signInbutton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Check fields arent empty
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "Please enter both Email and Password", Toast.LENGTH_SHORT).show();
            } else {
                // On successful login show toast message TODO make login work with actual login information via API
                Toast.makeText(MainActivity.this, "Logging in as: " +email, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, SelectRoleActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}