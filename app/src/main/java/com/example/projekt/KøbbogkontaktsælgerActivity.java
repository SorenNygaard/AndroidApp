package com.example.projekt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KøbbogkontaktsælgerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Her kan du hente data om bogen fra din database og vise det på forsiden

        // Tilføj en knap til at kontakte sælgeren
        Button contactSellerButton = findViewById(R.id.kontaktSælger_button);
        kontaktSælger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Her kan du implementere logikken til at kontakte sælgeren, f.eks. åbne en ny aktivitet eller vise en dialog
                Toast.makeText(BookDetailActivity.this, "Kontakt sælgeren implementeres her", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
