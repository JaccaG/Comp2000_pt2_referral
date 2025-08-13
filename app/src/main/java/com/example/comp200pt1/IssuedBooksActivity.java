package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.api.ApiClient;
import com.example.comp200pt1.api.LibraryApi;
import com.example.comp200pt1.api.dto.IssuedBookDto;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssuedBooksActivity extends AppCompatActivity {

    private final ArrayList<IssuedBookDto> data = new ArrayList<>();
    private IssuedBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books);

        // Grab views from layout
        ImageButton back     = findViewById(R.id.backButton);
        EditText usernameInput = findViewById(R.id.usernameInput);
        Button loadBtn       = findViewById(R.id.loadBtn);
        ListView list        = findViewById(R.id.issuedListView);

        // Plug the adapter into the ListView
        adapter = new IssuedBookAdapter(this, data);
        list.setAdapter(adapter);

        // If a username was passed in, prefill
        String initialUser = getIntent().getStringExtra("username");
        if (initialUser != null) {
            usernameInput.setText(initialUser);
            loadIssued(initialUser);
        }

        back.setOnClickListener(v -> finish());

        // When I press “Load”, I call the API for that username
        loadBtn.setOnClickListener(v -> {
            String u = usernameInput.getText() == null ? "" : usernameInput.getText().toString().trim();
            if (u.isEmpty()) {
                Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
                return;
            }
            loadIssued(u);
        });
    }

    // On success: replace list contents and refresh the adapter.
    private void loadIssued(String username) {
        LibraryApi api = ApiClient.get();
        api.getIssuedBooks(username).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<IssuedBookDto>> call, @NonNull Response<List<IssuedBookDto>> resp) {
                if (!resp.isSuccessful() || resp.body() == null) {
                    Toast.makeText(IssuedBooksActivity.this, "Failed (" + resp.code() + ")", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Swap the data set shown by the adapter
                data.clear();
                data.addAll(resp.body());

                // If user has no checked out books, show this toast
                if (data.isEmpty()) {
                    Toast.makeText(IssuedBooksActivity.this, "No issued books for " + username, Toast.LENGTH_SHORT).show();
                }

                // Tell the ListView to redo with the new items
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<IssuedBookDto>> call, @NonNull Throwable t) {
                // Network errors toast, for debugging
                Toast.makeText(IssuedBooksActivity.this, "Network error: " + t.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
