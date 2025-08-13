package com.example.comp200pt1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.db.DatabaseWrapper;

import java.util.ArrayList;
import java.util.List;

public class BookCatalogueActivity extends AppCompatActivity {

    private DatabaseWrapper books;

    private EditText search;

    private BookAdapter adapter;
    private final ArrayList<Book> current = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalogue);

        // Get DB ready
        books = new DatabaseWrapper(this);

        // Connect inputs
        ListView listView = findViewById(R.id.booksListView);
        ImageButton back = findViewById(R.id.backButton);
        ImageButton add = findViewById(R.id.addBookButton);
        search = findViewById(R.id.searchBooksBar);

        // Connect adapter to listview
        adapter = new BookAdapter(this, current);
        listView.setAdapter(adapter);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Open the add-book screen
        add.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, AddBookActivity.class))
        );

        // Whenever the text changes just reloads fromm DB
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                loadBooks(s.toString().trim());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list when coming back from Add/Edit screens
        loadBooks(search.getText() == null ? "" : search.getText().toString().trim());
    }

    // Pulls books from SQLite and refreshes the adapter
    private void loadBooks(String q) {
        List<Book> data = q.isEmpty() ? books.getAll() : books.search(q);
        current.clear();
        current.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
