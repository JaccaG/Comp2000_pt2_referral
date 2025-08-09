package com.example.comp200pt1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;

public class MemberBookCatalogueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_book_catalogue);

        // Bind views
        ListView list = findViewById(R.id.booksListView);
        ImageButton back = findViewById(R.id.backButton);
        EditText search = findViewById(R.id.searchBooksBar);

        // Sample data for the member view
        ArrayList<Book> data = new ArrayList<>();
        data.add(new Book("The Great Novel", "Author One", "978-0000000000", true));
        data.add(new Book("Mystery Book", "Author Two", "978-0000000001", false));
        data.add(new Book("Science Guide", "Author Three", "978-0000000002", true));

        // Sort A-Z by book title
        data.sort(Comparator.comparing(Book::getTitle));

        // Attach adapter
        MemberBookAdapter adapter = new MemberBookAdapter(this, data);
        list.setAdapter(adapter);

        // Back to previous screen
        back.setOnClickListener(v -> finish());

        // Search filter by title/author
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String q = s.toString().toLowerCase();
                ArrayList<Book> filtered = new ArrayList<>();
                for (Book b : data) {
                    if (b.getTitle().toLowerCase().contains(q) || b.getAuthor().toLowerCase().contains(q)) {
                        filtered.add(b);
                    }
                }
                list.setAdapter(new MemberBookAdapter(MemberBookCatalogueActivity.this, filtered));
            }
        });
    }
}
