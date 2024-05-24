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

/**
 * DatabaseManager is a utility class that manages interactions with the Firebase Realtime Database.
 * It provides methods for initializing the database, reading and writing data, and searching for books.
 */
public class DatabaseManager {

    // Static reference to the Firebase Database
    private static DatabaseReference databaseReference;

    /**
     * Initializes the Firebase Realtime Database reference.
     * This method should be called once, typically at the start of the application.
     */
    public static void initialize() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // Get an instance of the Firebase database
        databaseReference = database.getReference(); // Get a reference to the root node of the database
    }

    /**
     * Gets the Firebase database reference.
     *
     * @return The database reference.
     */
    public static DatabaseReference getDatabaseReference() {
        return databaseReference; // Return the database reference initialized in the initialize method
    }

    /**
     * Gets the current user's ID.
     *
     * @return The current user's ID, or null if no user is signed in.
     */
    public static String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser(); // Get the currently signed-in user
        if (currentUser != null) { // Check if a user is signed in
            return currentUser.getUid(); // Return the user ID
        }
        return null; // Return null if no user is signed in
    }

    /**
     * Reads data from the specified child node in the Firebase database.
     *
     * @param child              The child node to read from.
     * @param valueEventListener The listener to handle the data read event.
     */
    public static void readData(String child, ValueEventListener valueEventListener) {
        databaseReference.child(child).addListenerForSingleValueEvent(valueEventListener);
        // Attach a single event listener to the specified child node to read its data once
    }

    /**
     * Writes data to the specified child node in the Firebase database.
     *
     * @param child   The child node to write to.
     * @param data    The data to write.
     * @param context The context for showing toast messages.
     */
    public static void writeData(String child, Object data, Context context) {
        DatabaseReference newChildRef = databaseReference.child(child).push();
        // Create a new unique key under the specified child node
        newChildRef.setValue(data)
                .addOnSuccessListener(aVoid -> {
                    // Code to execute on successful write operation
                    Log.d("DatabaseManager", "Data successfully written to the database");
                    Toast.makeText(context, "Book added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Code to execute on failed write operation
                    Log.e("DatabaseManager", "Error writing data to the database", e);
                    Toast.makeText(context, "Failed to add book: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
