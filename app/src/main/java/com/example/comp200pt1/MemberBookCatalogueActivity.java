package com.example.comp200pt1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.auth.AuthManager;
import com.example.comp200pt1.auth.User;
import com.example.comp200pt1.db.DatabaseWrapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MemberBookCatalogueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_book_catalogue);

        // Connect views + inputs
        ListView list = findViewById(R.id.booksListView);
        ImageButton back = findViewById(R.id.backButton);
        EditText search = findViewById(R.id.searchBooksBar);

        DatabaseWrapper db = new DatabaseWrapper(this);

        // Grab whoever is logged in from AuthManager
        User u = AuthManager.getCurrentUser(this);
        String username = (u == null || u.getUsername() == null || u.getUsername().isEmpty())
                ? "john123"
                : u.getUsername();

        // Pulls everything from SQLite and sort A-Z by title
        List<Book> data = db.getAll();
        data.sort(Comparator.comparing(Book::getTitle));

        // Adapter needs db + username to decide if Request enabled or not
        MemberBookAdapter adapter = new MemberBookAdapter(this, new ArrayList<>(data), db, username);
        list.setAdapter(adapter);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Live search - as type rebuild list using filter
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Search by title/author
                String q = s.toString().trim().toLowerCase();
                List<Book> filtered = q.isEmpty() ? db.getAll() : db.search(q);

                filtered.sort(Comparator.comparing(Book::getTitle));

                list.setAdapter(new MemberBookAdapter(
                        MemberBookCatalogueActivity.this,
                        new ArrayList<>(filtered),
                        db,
                        username
                ));
            }
        });
    }
}
