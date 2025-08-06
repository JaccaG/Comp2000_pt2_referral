package com.example.comp200pt1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

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

        // Staff button click
        /*
        staffButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectRoleActivity.this, StaffDashboardActivity.class);
            startActivity(intent);
        });

        // Member button click
        memberButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectRoleActivity.this, MemberDashboardActivity.class);
            startActivity(intent);
        }); */

        // Logout button click
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(SelectRoleActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Optional: Prevent back navigation
        });
    }
}
