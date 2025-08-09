package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookRequestsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_requests);

        // Bind views
        ListView list = findViewById(R.id.requestsListView);
        ImageButton back = findViewById(R.id.backButton);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Fake dataset to populate list
        ArrayList<BookRequest> data = new ArrayList<>();
        data.add(new BookRequest("The Great Novel", "John Smith", "2024-01-15", "Est. approval: 2-3 business days", false));
        data.add(new BookRequest("Mystery Book", "Sarah Johnson", "2024-01-16", "Est. approval: 1-2 business days", false));
        BookRequest approved = new BookRequest("Science Guide", "Mike Davis", "2024-01-10", "", true);
        approved.approve("Pickup by: 2024-01-19");
        data.add(approved);

        // Connect adapter
        list.setAdapter(new BookRequestAdapter(this, data));
    }
}
