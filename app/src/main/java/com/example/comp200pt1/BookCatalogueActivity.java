package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;

public class BookCatalogueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_catalogue);

        // Bind views
        ListView booksList = findViewById(R.id.booksListView);
        ImageButton back = findViewById(R.id.backButton);
        ImageButton add = findViewById(R.id.addBookButton);
        EditText search = findViewById(R.id.searchBooksBar);

        // Sample data inputs for the list view and demonstration
        ArrayList<Book> data = new ArrayList<>();
        data.add(new Book("The Great Novel", "Author One", "Available"));
        data.add(new Book("Mystery Book", "Author Two", "Checked Out"));
        data.add(new Book("Science Guide", "Author Three", "Available"));

        // Sort list A-Z by book title
        data.sort(Comparator.comparing(Book::getTitle));

        // Attaches adapter too list view
        BookAdapter adapter = new BookAdapter(this, data);
        booksList.setAdapter(adapter);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Open the add book screen
        add.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, AddBookActivity.class))
        );

        // search filter on title/author using text input - case sensitive
        search.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                String q = s.toString().toLowerCase();
                ArrayList<Book> filtered = new ArrayList<>();
                for (Book b : data) {
                    if (b.getTitle().toLowerCase().contains(q) || b.getAuthor().toLowerCase().contains(q)) {
                        filtered.add(b);
                    }
                }
                booksList.setAdapter(new BookAdapter(BookCatalogueActivity.this, filtered));
            }
        });
    }
}
