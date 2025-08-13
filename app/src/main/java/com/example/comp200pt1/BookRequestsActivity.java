package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.api.ApiClient;
import com.example.comp200pt1.api.LibraryApi;
import com.example.comp200pt1.db.DatabaseWrapper;
import com.example.comp200pt1.db.LibraryDb;

import java.util.ArrayList;
import java.util.List;

public class BookRequestsActivity extends AppCompatActivity {

    private DatabaseWrapper db;
    private LibraryApi api;

    private ListView list;
    private BookRequestAdapter adapter;

    private ArrayList<BookRequest> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_requests);

        // Helpers
        db = new DatabaseWrapper(this);
        api = ApiClient.get();

        // Connect views
        list = findViewById(R.id.requestsListView);
        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        loadPending();
    }

    // Rebuild the list from SQLite, only keep status=pending
    private void loadPending() {
        items.clear();

        List<LibraryDb.RequestRow> rows = db.allRequests();
        for (LibraryDb.RequestRow r : rows) {
            if (!"pending".equalsIgnoreCase(r.status)) continue;

            // DB row goes to UI model the adapter understands
            BookRequest ui = new BookRequest(
                    r.bookTitle,
                    r.username,
                    r.requestedDate,
                    "Est. approval: 1-2 business days",
                    false
            );
            items.add(ui);
        }

        // Use the adapter with small callback so refresh after approve/deny
        adapter = new BookRequestAdapter(this, items, db, api, this::onListChanged);
        list.setAdapter(adapter);

        // Toast to say no pending requests
        if (items.isEmpty()) {
            Toast.makeText(this, "No pending requests.", Toast.LENGTH_SHORT).show();
        }
    }

    // Runs after approve/deny to refresh the list
    private void onListChanged() { loadPending(); }
}
