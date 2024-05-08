package com.example.projekt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class buyAndSellActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_buyer);

        // Her kan du hente data om bogen fra din database og vise det på forsiden
        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Tilføj en knap til at kontakte sælgeren
        Button contactSellerButton = findViewById(R.id.contact_seller_button);
        contactSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Her kan du implementere logikken til at kontakte sælgeren, f.eks. åbne en ny aktivitet eller vise en dialog

            }
        });
    }
}