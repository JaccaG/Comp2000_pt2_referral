package com.example.comp200pt1;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;

public class MembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_staff_view);

        // Bind views
        ImageButton backButton = findViewById(R.id.backButton);
        ImageButton addButton = findViewById(R.id.addMemberButton);
        ListView membersListView = findViewById(R.id.membersListView);

        // Sample data to populate list
        ArrayList<Member> memberList = new ArrayList<>();
        memberList.add(new Member("John Smith", "john@example.com", "11111 111111","2025-01-01"));
        memberList.add(new Member("Sarah Johnson", "sarah@example.com", "22222 222222", "2025-01-02"));
        memberList.add(new Member("Mike Davis", "mike@example.com", "33333 333333", "2025-01-03"));

        // Sort list A-Z by full name TODO switch to surname when adjusted to API e.g. First Name, Last Name
        memberList.sort(Comparator.comparing(Member::getFullName));

        // Attach adapter
        MemberAdapter adapter = new MemberAdapter(this, memberList);
        membersListView.setAdapter(adapter);

        // Add member button
        addButton.setOnClickListener(v ->
                startActivity(new android.content.Intent(this, AddMemberActivity.class))
        );

        // Back to previous screen
        backButton.setOnClickListener(v -> finish());
    }
}
