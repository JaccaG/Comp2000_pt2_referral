package com.example.comp200pt1;

import java.io.Serializable;

// Basic book model, serializable so I can pass it between screens
public class Book implements Serializable {
    // Book details (read-only)
    private final String title;
    private final String author;
    private final String isbn;
    private final boolean available;

    // If the status comes in as text, map it to a boolean
    public Book(String title, String author, String status) {
        this.title = title;
        this.author = author;
        this.isbn = "";
        this.available = "Available".equalsIgnoreCase(status);
    }

    // If I already have all fields
    public Book(String title, String author, String isbn, boolean available) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }

    // Useful label for the UI
    public String getStatus() { return available ? "Available" : "Checked Out"; }
}
