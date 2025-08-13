package com.example.comp200pt1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MemberDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_dashboard);

        // Bind views
        ImageButton back = findViewById(R.id.backButton);
        ImageButton Logout = findViewById(R.id.logoutButton);

        Button browseBooks = findViewById(R.id.browse_books_member);
        Button myRequests  = findViewById(R.id.view_my_requests);
        Button editProfile = findViewById(R.id.edit_profile);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Logout to the login screen
        Logout.setOnClickListener(v -> {
            com.example.comp200pt1.auth.AuthManager.signOut(this);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Open the member book catalogue
        browseBooks.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, MemberBookCatalogueActivity.class)));

        // Open "My Requests" (made by member)
        myRequests.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, MemberRequestsActivity.class)));

        // Open "Edit Profile"
        editProfile.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, EditProfileActivity.class)));

    }
}
