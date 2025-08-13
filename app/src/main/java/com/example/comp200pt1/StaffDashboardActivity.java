package com.example.comp200pt1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class StaffDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        // Bind views
        Button viewMembersButton = findViewById(R.id.view_library_members);
        Button viewBooksButton = findViewById(R.id.view_book_catalogue_staff);
        Button viewRequestsButton = findViewById(R.id.view_book_requests);
        Button viewIssuedButton = findViewById(R.id.view_issued_books);
        ImageButton logoutButton = findViewById(R.id.logoutButton);
        ImageButton backButton = findViewById(R.id.backButton);

        // Open Members screen
        viewMembersButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, MembersActivity.class);
            startActivity(intent);
        });

        // Open Book Catalogue
        viewBooksButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, BookCatalogueActivity.class);
            startActivity(intent);
        });

        // Open Book Requests
        viewRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, BookRequestsActivity.class);
            startActivity(intent);
        });

        // Open Issued Books
        viewIssuedButton.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboardActivity.this, IssuedBooksActivity.class);
            startActivity(intent);
        });

        // Logout back to login screen
        logoutButton.setOnClickListener(v -> {
            com.example.comp200pt1.auth.AuthManager.signOut(this);
            Intent intent = new Intent(StaffDashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Back to previous screen
        backButton.setOnClickListener(v -> finish());
    }
}