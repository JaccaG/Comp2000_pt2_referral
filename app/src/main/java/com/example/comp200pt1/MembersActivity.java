package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.comp200pt1.api.ApiClient;
import com.example.comp200pt1.api.LibraryApi;
import com.example.comp200pt1.api.dto.MemberDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembersActivity extends AppCompatActivity {

    private ArrayList<Member> memberList;
    private ArrayList<String> usernameList;
    private MemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_staff_view);

        // Bind views
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton addButton  = findViewById(R.id.addMemberButton);
        ListView membersListView = findViewById(R.id.membersListView);

        // Data to populate list (start empty, fill from API)
        memberList   = new ArrayList<>();
        usernameList = new ArrayList<>();

        // Attach adapter
        adapter = new MemberAdapter(this, memberList, usernameList);
        membersListView.setAdapter(adapter);

        // Add member button
        addButton.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, AddMemberActivity.class))
        );

        // Back to previous screen
        backButton.setOnClickListener(v -> finish());

        // Fetch members from API and update UI
        fetchMembers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When coming back here refresh the list
        fetchMembers();
    }

    private void fetchMembers() {
        LibraryApi api = ApiClient.get();

        api.getMembers().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<MemberDto>> call, @NonNull Response<List<MemberDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(MembersActivity.this,
                            "Failed to load members (" + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                List<MemberDto> dtos = response.body();

                // Turn API items into my Member objects
                memberList.clear();
                usernameList.clear();
                for (MemberDto dto : dtos) {
                    String first = safe(dto.firstname);
                    String last = safe(dto.lastname);
                    String fullName = (first + " " + last).trim();

                    usernameList.add(safe(dto.username));
                    memberList.add(new Member(
                            fullName,
                            safe(dto.email),
                            safe(dto.contact),
                            toYMD(dto.membershipEndDate) // show short date
                    ));
                }

                // make names A–Z without losing usernames
                ArrayList<Integer> order = new ArrayList<>();
                for (int i = 0; i < memberList.size(); i++) order.add(i);
                order.sort(Comparator.comparing(i -> memberList.get(i).getFullName().toLowerCase(Locale.ROOT)));

                ArrayList<Member> sortedMembers = new ArrayList<>(memberList.size());
                ArrayList<String> sortedUsernames = new ArrayList<>(usernameList.size());
                for (Integer i : order) {
                    sortedMembers.add(memberList.get(i));
                    sortedUsernames.add(usernameList.get(i));
                }
                memberList.clear();
                memberList.addAll(sortedMembers);
                usernameList.clear();
                usernameList.addAll(sortedUsernames);

                // Tell the list to redraw
                adapter.notifyDataSetChanged();

                if (memberList.isEmpty()) {
                    Toast.makeText(MembersActivity.this, "No members found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<MemberDto>> call, @NonNull Throwable t) {
                Toast.makeText(MembersActivity.this,
                        "Network error: " + t.getClass().getSimpleName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String safe(String s) { return (s == null) ? "" : s; }

    // Turn "Sunday, 10 Aug 2025 00:00:00 GMT" into "2025-08-10" for correct API format
    private static String toYMD(String raw) {
        if (raw == null) return "";
        String t = raw.trim();
        if (t.matches("^\\d{4}-\\d{2}-\\d{2}$")) return t;
        try {
            SimpleDateFormat in = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.UK);
            Date d = in.parse(t);
            if (d != null) {
                return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(d);
            }
        } catch (Exception ignored) { }
        return t;
    }
}