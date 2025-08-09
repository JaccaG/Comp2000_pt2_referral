package com.example.comp200pt1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

// Role selection screen
public class SelectRoleActivity extends AppCompatActivity {

    Button staffButton, memberButton;
    ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role); // Ensure this matches your XML file name

        // Bind views
        staffButton = findViewById(R.id.staffButton);
        memberButton = findViewById(R.id.memberButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Go to staff dashboard
        staffButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectRoleActivity.this, StaffDashboardActivity.class);
            startActivity(intent);
        });

        // Go to Member dashboard
        memberButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectRoleActivity.this, MemberDashboardActivity.class);
            startActivity(intent);
        });

        // Logout back to login screen
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectRoleActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
