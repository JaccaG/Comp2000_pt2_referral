package com.example.comp200pt1;

import java.io.Serializable;

// Member model, kept it serializable so I can pass it between activities
public class Member implements Serializable {
    private final String fullName;
    private final String email;
    private String contact;
    private String memberSince;
    public Member(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public Member(String fullName, String email, String contact, String memberSince) {
        this.fullName = fullName;
        this.email = email;
        this.contact = contact;
        this.memberSince = memberSince;
    }

    // Getters
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getContact() { return contact; }
    public String getMemberSince() { return memberSince; }
}
