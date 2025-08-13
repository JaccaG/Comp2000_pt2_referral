package com.example.comp200pt1.api.dto;

import com.google.gson.annotations.SerializedName;

// Body for POST /members
public class CreateMemberRequest {

    @SerializedName("username")
    public String username;

    @SerializedName("firstname")
    public String firstname;

    @SerializedName("lastname")
    public String lastname;

    @SerializedName("email")
    public String email;

    @SerializedName("contact")
    public String contact;

    @SerializedName("membership_end_date")
    public String membershipEndDate;

    public CreateMemberRequest(String username, String firstname, String lastname,
                               String email, String contact, String membershipEndDate) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.membershipEndDate = membershipEndDate;
    }
}
