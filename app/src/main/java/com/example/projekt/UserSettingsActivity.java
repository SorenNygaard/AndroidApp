package com.example.projekt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserSettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings); // Link XML layout file

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        backButton = findViewById(R.id.backButton);
        Button deleteButton = findViewById(R.id.button3);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
        // Set OnClickListener for the backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSettingsActivity.this, MainActivity.class));
                finish();
            }
        });
        Button changePasswordButton = findViewById(R.id.changePasswordbtn);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the new password from the input field
                EditText newPasswordEditText = findViewById(R.id.passwordChange);
                String newPassword = newPasswordEditText.getText().toString();

                // Get the confirm password from the input field
                EditText confirmPasswordEditText = findViewById(R.id.passwordChangeConfirm);
                String confirmPassword = confirmPasswordEditText.getText().toString();

                // Call the changePassword function with both passwords
                changePassword(newPassword, confirmPassword);
            }
        });
    }

    private void changePassword(String newPassword, String confirmPassword) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (newPassword.equals(confirmPassword)) {
                // Prompt the user to re-enter their current password for reauthentication
                showReauthenticationPrompt(currentUser, newPassword);
            } else {
                Toast.makeText(UserSettingsActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(UserSettingsActivity.this, "No user signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void showReauthenticationPrompt(FirebaseUser user, String newPassword) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reauthentication");
        builder.setMessage("Please enter your current password to proceed");

        final EditText inputPassword = new EditText(this);
        builder.setView(inputPassword);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = inputPassword.getText().toString();
                reauthenticateUser(user, password, newPassword);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void reauthenticateUser(FirebaseUser user, String password, String newPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Reauthentication successful, proceed with password update
                            updatePassword(user, newPassword);
                        } else {
                            // Reauthentication failed
                            Toast.makeText(UserSettingsActivity.this, "Reauthentication failed. Please enter the correct password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updatePassword(FirebaseUser user, String newPassword) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Password updated successfully
                            Toast.makeText(UserSettingsActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Password update failed
                            Toast.makeText(UserSettingsActivity.this, "Failed to change password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete your account?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showPasswordPrompt(); // Prompt user for password
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showPasswordPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        final EditText inputPassword = new EditText(this);
        builder.setView(inputPassword);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = inputPassword.getText().toString();
                deleteAccount(password);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteAccount(String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // User re-authenticated successfully, proceed with account deletion
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Account deleted successfully
                                        navigateToSignInActivity();
                                    } else {
                                        // Failed to delete account
                                        // Display appropriate error message or handle the error
                                        Toast.makeText(UserSettingsActivity.this, "Failed to delete account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            // Re-authentication failed
                            // Display appropriate error message or handle the error
                            Toast.makeText(UserSettingsActivity.this, "Re-authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void loadUserBooks() {
        String currentUserId = DatabaseManager.getCurrentUserId();
        if (currentUserId != null) {
            // Read data from the "books" node using DatabaseManager
            DatabaseManager.readData("books", new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<ModelRecyclerView> userBooks = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ModelRecyclerView model = ds.getValue(ModelRecyclerView.class);
                        // Add book to the list if it exists and doesn't require user filtering
                        if (model != null) {
                            userBooks.add(model);
                        }
                    }
                    // Display user's books in the "Min Historik" section
                    displayUserBooks(userBooks);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserSettingsActivity.this, "Failed to load user books: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UserSettingsActivity.this, "No user signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUserBooks(ArrayList<ModelRecyclerView> userBooks) {
        // Update UI with user's books
        // For example, you can create a RecyclerView to display the books
        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterRecycleView adapter = new AdapterRecycleView(this, userBooks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load user's books when the activity resumes
        loadUserBooks();
    }
    private void navigateToSignInActivity() {
        Intent intent = new Intent(UserSettingsActivity.this, SigninActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
        startActivity(intent);
        finish(); // Finish the current activity to prevent going back to it on back press
    }
}

