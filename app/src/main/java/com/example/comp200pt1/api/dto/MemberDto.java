package com.example.comp200pt1.api.dto;

import com.google.gson.annotations.SerializedName;

public class MemberDto {
    @SerializedName("username") public String username;
    @SerializedName("firstname") public String firstname;
    @SerializedName("lastname")  public String lastname;
    @SerializedName("email")     public String email;
    @SerializedName("contact")   public String contact;
    @SerializedName("membership_end_date") public String membershipEndDate;
}
