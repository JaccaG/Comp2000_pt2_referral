package com.example.comp200pt1.db;

import android.content.Context;

import com.example.comp200pt1.Book;
import com.example.comp200pt1.db.LibraryDb.RequestRow;

import java.util.List;

public class DatabaseWrapper {
    private final LibraryDb db;

    public DatabaseWrapper(Context ctx) {
        this.db = new LibraryDb(ctx.getApplicationContext());
    }

    // --------------- Books -----------------

    // Pull every book
    public List<Book> getAll() {
        return db.getAllBooks();
    }

    // search by title/author
    public List<Book> search(String q) {
        return db.searchBooks(q.toLowerCase());
    }

    // Insert one book
    public long add(String title, String author, String isbn, boolean available) {
        return db.insertBook(title, author, isbn, available);
    }

    // lookup by title/author
    public Long findIdByTitleAuthor(String title, String author) {
        return db.findIdByTitleAuthor(title, author);
    }

    // delete book
    public int deleteById(long id) {
        return db.deleteBook(id);
    }

    // Load single book
    public Book getById(long id) {
        return db.getBookById(id);
    }

    // Update single book - done by row
    public int updateById(long id, String title, String author, String isbn, boolean available) {
        return db.updateBook(id, title, author, isbn, available);
    }

    // Availability change after issue/return
    public int setAvailabilityByTitle(String title, boolean available) {
        return db.setBookAvailabilityByTitle(title, available);
    }


    // --------------- Book Requests -----------------

    // Create book request
    public long addRequest(String username, String bookTitle, String requestedDate) {
        return db.insertRequest(username, bookTitle, requestedDate);
    }

    // Stop new request while book is pending/approved
    public boolean hasActiveRequest(String username, String bookTitle) {
        return db.hasActiveRequest(username, bookTitle);
    }

    // Mark request approved and store approve/return dates
    public int approveRequest(String username, String bookTitle, String approvedDate, String returnDate) {
        return db.approveRequest(username, bookTitle, approvedDate, returnDate);
    }

    // Mark request denied + timestamp
    public int denyRequest(String username, String bookTitle, String reason, String deniedDate) {
        return db.denyRequest(username, bookTitle, reason, deniedDate);
    }

    // Pulls all book requests
    public List<RequestRow> allRequests() {
        return db.getAllRequests();
    }

    // Member view of just their own requests
    public List<RequestRow> requestsForUser(String username) {
        return db.getRequestsForUser(username);
    }


}
