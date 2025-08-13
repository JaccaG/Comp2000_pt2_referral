package com.example.comp200pt1.api.dto;

import com.google.gson.annotations.SerializedName;

public class IssuedBookDto {
    @SerializedName("id")         public int id;
    @SerializedName("username")   public String username;
    @SerializedName("book_title") public String bookTitle;
    @SerializedName("issue_date") public String issueDate;
    @SerializedName("return_date")public String returnDate;
}
