package com.example.projekt;

import android.content.Intent;
import android.os.Bundle;
import com.squareup.picasso.Picasso;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class buyAndSellActivity extends AppCompatActivity {
    ImageButton backButton;
    Button contactbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_buyer);
        backButton = findViewById(R.id.backButton);
        contactbutton = findViewById(R.id.contact_seller_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(buyAndSellActivity.this, MainActivity.class));
                finish();
            }
        });
        contactbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(buyAndSellActivity.this,"Information om sælger her", Toast.LENGTH_SHORT).show();
            }
        });


        // Retrieve the information from the intent
        String bookTitle = getIntent().getStringExtra("book_title");
        String bookAuthor = getIntent().getStringExtra("book_author");
        String bookEducation = getIntent().getStringExtra("book_education");
        String bookSemester = getIntent().getStringExtra("book_semester");
        String bookCondition = getIntent().getStringExtra("book_condition");
        double price = getIntent().getDoubleExtra("book_price", 0);
        // Retrieve image URI from the intent
        String imageUrl = getIntent().getStringExtra("book_image");

        // Load the image into the ImageView
        ImageView imageView = findViewById(R.id.book_image);
        Picasso.get().load(imageUrl).into(imageView);

        // Display the information in TextViews
        TextView titleTextView = findViewById(R.id.book_title);
        titleTextView.setText(bookTitle);

        TextView authorTextView = findViewById(R.id.book_author);
        authorTextView.setText(bookAuthor);

        TextView educationTextView = findViewById(R.id.book_education);
        educationTextView.setText(bookEducation);

        TextView semesterTextView = findViewById(R.id.book_semester);
        semesterTextView.setText(bookSemester);

        TextView conditionTextView = findViewById(R.id.book_condition);
        conditionTextView.setText(bookCondition);

        TextView priceTextView = findViewById(R.id.book_price);
        priceTextView.setText(String.valueOf(price));
    }

}

