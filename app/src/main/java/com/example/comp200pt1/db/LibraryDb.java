package com.example.comp200pt1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.comp200pt1.Book;
import com.example.comp200pt1.auth.User;

import java.util.ArrayList;
import java.util.List;

public class LibraryDb extends SQLiteOpenHelper {

    public static final String DB_NAME = "library.db";
    public static final int DB_VERSION = 1;

    // books table + columns
    public static final String T_BOOKS = "books";
    public static final String B_ID = "id";
    public static final String B_TITLE = "title";
    public static final String B_AUTHOR = "author";
    public static final String B_ISBN = "isbn";
    public static final String B_AVAILABLE = "available";

    // users table + columns
    public static final String T_USERS = "users";
    public static final String U_ID = "id";
    public static final String U_USERNAME = "username";
    public static final String U_EMAIL = "email";
    public static final String U_PASSWORD = "password";
    public static final String U_ROLE = "role";

    // requests table + columns
    public static final String T_REQUESTS = "requests";
    public static final String R_ID = "id";
    public static final String R_USERNAME = "username";
    public static final String R_BOOK_TITLE = "book_title";
    public static final String R_REQUESTED_DATE = "requested_date";
    public static final String R_STATUS = "status";
    public static final String R_APPROVED_DATE = "approved_date";
    public static final String R_RETURN_DATE = "return_date";
    public static final String R_DENY_REASON = "deny_reason";

    public LibraryDb(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create books
        db.execSQL(
                "CREATE TABLE " + T_BOOKS + " (" +
                        B_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        B_TITLE + " TEXT NOT NULL, " +
                        B_AUTHOR + " TEXT NOT NULL, " +
                        B_ISBN + " TEXT, " +
                        B_AVAILABLE + " INTEGER NOT NULL DEFAULT 1)"
        );

        // seed a few books so list isn’t empty
        insertBook(db, "The Great Novel", "Author One", "978-0000000000", true);
        insertBook(db, "Mystery Book", "Author Two", "978-0000000001", false);
        insertBook(db, "Science Guide", "Author Three", "978-0000000002", true);

        // create users
        db.execSQL(
                "CREATE TABLE " + T_USERS + " (" +
                        U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        U_USERNAME + " TEXT NOT NULL UNIQUE, " +
                        U_EMAIL + " TEXT NOT NULL UNIQUE, " +
                        U_PASSWORD + " TEXT NOT NULL, " +
                        U_ROLE + " TEXT NOT NULL)"
        );

        // seed users (static accounts)
        // staff
        insertUser(db, "staff1", "staff@example.com", "admin123", "staff");
        // member
        insertUser(db, "john123", "member@example.com", "user123", "member");

        // requests table
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + T_REQUESTS + " (" +
                        R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        R_USERNAME + " TEXT NOT NULL, " +
                        R_BOOK_TITLE + " TEXT NOT NULL, " +
                        R_REQUESTED_DATE + " TEXT NOT NULL, " +
                        R_STATUS + " TEXT NOT NULL, " +
                        R_APPROVED_DATE + " TEXT, " +
                        R_RETURN_DATE + " TEXT, " +
                        R_DENY_REASON + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // ---------- Users (auth) ----------

    // Add user
    private long insertUser(SQLiteDatabase db, String username, String email, String password, String role) {
        ContentValues cv = new ContentValues();
        cv.put(U_USERNAME, username);
        cv.put(U_EMAIL, email);
        cv.put(U_PASSWORD, password);
        cv.put(U_ROLE, role);
        return db.insert(T_USERS, null, cv);
    }

    // finds user in DB using email and pass
    public User findUserByEmailPassword(String email, String password) {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(
                T_USERS,
                new String[]{U_USERNAME, U_EMAIL, U_ROLE},
                U_EMAIL + "=? AND " + U_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null
        )) {
            if (c.moveToFirst()) {
                String username = c.getString(0);
                String em = c.getString(1);
                String role = c.getString(2);
                return new User(username, em, role);
            }
            return null;
        }
    }

    // ---------- Books ----------

    // Add a book to DB
    private long insertBook(SQLiteDatabase db, String title, String author, String isbn, boolean available) {
        ContentValues cv = new ContentValues();
        cv.put(B_TITLE, title);
        cv.put(B_AUTHOR, author);
        cv.put(B_ISBN, isbn);
        cv.put(B_AVAILABLE, available ? 1 : 0);
        return db.insert(T_BOOKS, null, cv);
    }

    public long insertBook(String title, String author, String isbn, boolean available) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            return insertBook(db, title, author, isbn, available);
        }
    }

    // Update book in DB
    public int updateBook(long id, String title, String author, String isbn, boolean available) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(B_TITLE, title);
            cv.put(B_AUTHOR, author);
            cv.put(B_ISBN, isbn);
            cv.put(B_AVAILABLE, available ? 1 : 0);
            return db.update(T_BOOKS, cv, B_ID + "=?", new String[]{String.valueOf(id)});
        }
    }

    // Book availability change on issue/return
    public int setBookAvailabilityByTitle(String title, boolean available) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(B_AVAILABLE, available ? 1 : 0);
            return db.update(T_BOOKS, cv, B_TITLE + "=?", new String[]{title});
        }
    }

    // Delete book from DB
    public int deleteBook(long id) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            return db.delete(T_BOOKS, B_ID + "=?", new String[]{String.valueOf(id)});
        }
    }


    // Retrieve single book
    public Book getBookById(long id) {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(
                T_BOOKS,
                new String[]{B_TITLE, B_AUTHOR, B_ISBN, B_AVAILABLE},
                B_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        )) {
            if (c.moveToFirst()) {
                String title = c.getString(0);
                String author = c.getString(1);
                String isbn = c.getString(2);
                boolean available = c.getInt(3) == 1;
                return new Book(title, author, (isbn == null ? "" : isbn), available);
            }
            return null;
        }
    }

    // Lookup book by title/author
    public Long findIdByTitleAuthor(String title, String author) {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(T_BOOKS, new String[]{B_ID},
                B_TITLE + "=? AND " + B_AUTHOR + "=?",
                new String[]{title, author}, null, null, null)) {
            if (c.moveToFirst()) return c.getLong(0);
            return null;
        }
    }

    // Get all books in DB
    public List<Book> getAllBooks() {
        ArrayList<Book> out = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(T_BOOKS,
                new String[]{B_TITLE, B_AUTHOR, B_ISBN, B_AVAILABLE},
                null, null, null, null, B_TITLE + " ASC")) {
            while (c.moveToNext()) {
                String title = c.getString(0);
                String author = c.getString(1);
                String isbn = c.getString(2);
                boolean available = c.getInt(3) == 1;
                out.add(new Book(title, author, isbn == null ? "" : isbn, available));
            }
        }
        return out;
    }

    // Search book filter
    public List<Book> searchBooks(String queryLower) {
        ArrayList<Book> out = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(T_BOOKS,
                new String[]{B_TITLE, B_AUTHOR, B_ISBN, B_AVAILABLE},
                "LOWER(" + B_TITLE + ") LIKE ? OR LOWER(" + B_AUTHOR + ") LIKE ?",
                new String[]{"%" + queryLower + "%", "%" + queryLower + "%"},
                null, null, B_TITLE + " ASC")) {
            while (c.moveToNext()) {
                String title = c.getString(0);
                String author = c.getString(1);
                String isbn = c.getString(2);
                boolean available = c.getInt(3) == 1;
                out.add(new Book(title, author, isbn == null ? "" : isbn, available));
            }
        }
        return out;
    }

    // --- requests ---

    // Add a pending request
    public long insertRequest(String username, String bookTitle, String requestedDate) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(R_USERNAME, username);
            cv.put(R_BOOK_TITLE, bookTitle);
            cv.put(R_REQUESTED_DATE, requestedDate);
            cv.put(R_STATUS, "pending");
            return db.insert(T_REQUESTS, null, cv);
        }
    }

    // Approve by username+title
    public int approveRequest(String username, String bookTitle, String approvedDate, String returnDate) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(R_STATUS, "approved");
            cv.put(R_APPROVED_DATE, approvedDate);
            cv.put(R_RETURN_DATE, returnDate);
            return db.update(T_REQUESTS, cv,
                    R_USERNAME + "=? AND " + R_BOOK_TITLE + "=? AND " + R_STATUS + "='pending'",
                    new String[]{username, bookTitle});
        }
    }

    // Deny by username+title
    public int denyRequest(String username, String bookTitle, String reason, String deniedDate) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues cv = new ContentValues();
            cv.put(R_STATUS, "denied");
            cv.put(R_DENY_REASON, reason);
            cv.put(R_APPROVED_DATE, deniedDate); // store date in approved_date column as simple stamp (or add separate)
            return db.update(T_REQUESTS, cv,
                    R_USERNAME + "=? AND " + R_BOOK_TITLE + "=? AND " + R_STATUS + "='pending'",
                    new String[]{username, bookTitle});
        }
    }

    // Check if user already has pending/approved for this title
    public boolean hasActiveRequest(String username, String bookTitle) {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(T_REQUESTS, new String[]{"count(*)"},
                R_USERNAME + "=? AND " + R_BOOK_TITLE + "=? AND (" + R_STATUS + "='pending' OR " + R_STATUS + "='approved')",
                new String[]{username, bookTitle}, null, null, null)) {
            if (c.moveToFirst()) return c.getInt(0) > 0;
            return false;
        }
    }

    // Get all (for staff)
    public List<RequestRow> getAllRequests() {
        ArrayList<RequestRow> out = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(T_REQUESTS,
                new String[]{R_USERNAME, R_BOOK_TITLE, R_REQUESTED_DATE, R_STATUS, R_APPROVED_DATE, R_RETURN_DATE, R_DENY_REASON},
                null, null, null, null, R_REQUESTED_DATE + " DESC")) {
            while (c.moveToNext()) {
                RequestRow r = new RequestRow();
                r.username = c.getString(0);
                r.bookTitle = c.getString(1);
                r.requestedDate = c.getString(2);
                r.status = c.getString(3);
                r.approvedDate = c.getString(4);
                r.returnDate = c.getString(5);
                r.denyReason = c.getString(6);
                out.add(r);
            }
        }
        return out;
    }

    // Get for one user (member)
    public List<RequestRow> getRequestsForUser(String username) {
        ArrayList<RequestRow> out = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase(); Cursor c = db.query(T_REQUESTS,
                new String[]{R_USERNAME, R_BOOK_TITLE, R_REQUESTED_DATE, R_STATUS, R_APPROVED_DATE, R_RETURN_DATE, R_DENY_REASON},
                R_USERNAME + "=?", new String[]{username}, null, null, R_REQUESTED_DATE + " DESC")) {
            while (c.moveToNext()) {
                RequestRow r = new RequestRow();
                r.username = c.getString(0);
                r.bookTitle = c.getString(1);
                r.requestedDate = c.getString(2);
                r.status = c.getString(3);
                r.approvedDate = c.getString(4);
                r.returnDate = c.getString(5);
                r.denyReason = c.getString(6);
                out.add(r);
            }
        }
        return out;
    }

    // Simple holder
    public static class RequestRow {
        public String username, bookTitle, requestedDate, status, approvedDate, returnDate, denyReason;
    }
}