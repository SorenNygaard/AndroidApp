package com.example.projekt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellBookActivity extends AppCompatActivity {

    EditText editTextTitle, editTextAuthor, editTextPrice;
    Button btnSubmit;

    // Firebase Realtime Database URL for the Europe West (europe-west1) region
    private static final String FIREBASE_DATABASE_URL = "https://projekt-50207-default-rtdb.europe-west1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextPrice = findViewById(R.id.editTextPrice);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBook();
            }
        });
    }

    private void submitBook() {
        String title = editTextTitle.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();
        String priceText = editTextPrice.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Getting the Firebase database instance with specific URL
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("books");
        Book book = new Book(title, author, price);
        databaseReference.push().setValue(book)
                .addOnSuccessListener(aVoid -> Toast.makeText(SellBookActivity.this, "Book added successfully", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(SellBookActivity.this, "Failed to add book", Toast.LENGTH_LONG).show());
    }

    public static class Book {
        public String title, author;
        public double price;

        public Book() {
            // Default constructor required for calls to DataSnapshot.getValue(Book.class)
        }

        public Book(String title, String author, double price) {
            this.title = title;
            this.author = author;
            this.price = price;
        }
    }
}
