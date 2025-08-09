package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MemberRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_requests);

        // Bind views
        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        // Back to previous screen
        ListView list = findViewById(R.id.myRequestsList);

        // Sample data for members my request - pending/approved/denied states
        ArrayList<MemberRequest> items = new ArrayList<>();
        items.add(new MemberRequest("The Great Novel", "2024-01-15", "pending")
                .setPendingDetails("Est. approval: 2-3 business days", "Pickup: Within 1 week of approval"));
        items.add(new MemberRequest("History Book", "2024-01-10", "approved")
                .setApprovedDetails("2024-01-12", "2024-01-19"));
        items.add(new MemberRequest("Fiction Story", "2024-01-08", "denied")
                .setDeniedDetails("2024-01-10", "Reason: Book currently unavailable"));

        // Attach adapter
        list.setAdapter(new MemberRequestAdapter(this, items));
    }
}
