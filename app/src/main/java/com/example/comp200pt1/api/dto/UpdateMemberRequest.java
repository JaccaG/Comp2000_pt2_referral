package com.example.comp200pt1.api.dto;

import com.google.gson.annotations.SerializedName;

public class UpdateMemberRequest {

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

    public UpdateMemberRequest(String firstname, String lastname,
                               String email, String contact, String membershipEndDate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.membershipEndDate = membershipEndDate;
    }
}
