package com.example.comp200pt1.api.dto;

import com.google.gson.annotations.SerializedName;

public class IssueBookRequest {
    @SerializedName("username")   public String username;
    @SerializedName("book_title") public String bookTitle;
    @SerializedName("issue_date") public String issueDate;
    @SerializedName("return_date")public String returnDate;


    public IssueBookRequest(String username, String bookTitle, String issueDate, String returnDate) {
        this.username = username;
        this.bookTitle = bookTitle;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }
}
