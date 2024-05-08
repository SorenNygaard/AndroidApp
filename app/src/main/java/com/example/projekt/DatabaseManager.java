package com.example.projekt;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {

    private static DatabaseReference databaseReference;

    // Initialize the database reference
    public static void initialize() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    // Method to get the database reference
    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    // Method to get the current user's ID
    public static String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null; // Return null if no user is signed in
    }
}
