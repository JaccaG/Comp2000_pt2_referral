package com.example.comp200pt1.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.comp200pt1.db.LibraryDb;

public final class AuthManager {
    private AuthManager() {}
    private static AuthManager INSTANCE;

    private static final String PREFS = "auth_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";

    // tries to login; stores session if ok
    public static boolean signIn(Context ctx, String email, String password) {
        LibraryDb db = new LibraryDb(ctx.getApplicationContext());
        User u = db.findUserByEmailPassword(email, password);
        if (u == null) return false;

        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_USERNAME, u.username)
                .putString(KEY_EMAIL, u.email)
                .putString(KEY_ROLE, u.role)
                .apply();
        return true;
    }

    // clears session
    public static void signOut(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    // returns current user or null
    public static User getCurrentUser(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String username = sp.getString(KEY_USERNAME, null);
        if (username == null) return null;
        String email = sp.getString(KEY_EMAIL, null);
        String role = sp.getString(KEY_ROLE, null);
        return new User(username, email, role);
    }

    public static synchronized AuthManager get() {
        if (INSTANCE == null) INSTANCE = new AuthManager();
        return INSTANCE;
    }
}
