package com.example.projekt;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    // Method to read data from the database
    public static void readData(String child, ValueEventListener valueEventListener) {
        databaseReference.child(child).addListenerForSingleValueEvent(valueEventListener);
    }

    // Method to write data to the database
    public static void writeData(String child, Object data, Context context) {
        DatabaseReference newChildRef = databaseReference.child(child).push();
        newChildRef.setValue(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d("DatabaseManager", "Data successfully written to the database");
                    Toast.makeText(context, "Book added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("DatabaseManager", "Error writing data to the database", e);
                    Toast.makeText(context, "Failed to add book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    // Method to search books in the database
    public static void searchBooks(String child, String query, ValueEventListener valueEventListener) {
        // Convert the query to lowercase
        String lowercaseQuery = query.toLowerCase();

        // Get the reference to the specified node in the Firebase Realtime Database
        DatabaseReference reference = databaseReference.child(child);

        // Construct the Firebase query for searching
        Query searchQuery = reference.orderByChild("titel")
                .startAt(lowercaseQuery)
                .endAt(lowercaseQuery + "\uf8ff");

        // Add a ValueEventListener to the query
        searchQuery.addListenerForSingleValueEvent(valueEventListener);
    }
}
