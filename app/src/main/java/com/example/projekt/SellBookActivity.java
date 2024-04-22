package com.example.projekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellBookActivity extends AppCompatActivity {

    EditText editTextTitel, editTextForfatter, editTextPris, editTextUddannelse, editTextSemester, editTextStand;
    Button btnIndsend;
    ImageButton backButton;

    private static final String FIREBASE_DATABASE_URL = "https://projekt-50207-default-rtdb.europe-west1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);

        editTextTitel = findViewById(R.id.editTextTitel);
        editTextForfatter = findViewById(R.id.editTextForfatter);
        editTextPris = findViewById(R.id.editTextPris);
        editTextUddannelse = findViewById(R.id.editTextUddannelse);
        editTextSemester = findViewById(R.id.editTextSemester);
        editTextStand = findViewById(R.id.editTextStand);
        btnIndsend = findViewById(R.id.btnIndsend);
        backButton = findViewById(R.id.backButton);

        btnIndsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBook();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }

    private void submitBook() {
        String titel = editTextTitel.getText().toString().trim();
        String forfatter = editTextForfatter.getText().toString().trim();
        String prisText = editTextPris.getText().toString().trim();
        String uddannelse = editTextUddannelse.getText().toString().trim();
        String semester = editTextSemester.getText().toString().trim();
        String stand = editTextStand.getText().toString().trim();

        if (titel.isEmpty() || forfatter.isEmpty() || prisText.isEmpty() || uddannelse.isEmpty() || semester.isEmpty() || stand.isEmpty()) {
            Toast.makeText(this, "Udfyld venligst alle felter", Toast.LENGTH_SHORT).show();
            return;
        }

        double pris;
        try {
            pris = Double.parseDouble(prisText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ugyldigt prisformat", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("books");

        Bog bog = new Bog(titel, forfatter, pris, uddannelse, semester, stand);

        databaseReference.push().setValue(bog)
                .addOnSuccessListener(aVoid -> Toast.makeText(SellBookActivity.this, "Bog tilføjet succesfuldt", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(SellBookActivity.this, "Det lykkedes ikke at tilføje bogen", Toast.LENGTH_LONG).show());
    }

    public static class Bog {
        public String titel, forfatter, uddannelse, semester, stand;
        public double pris;

        public Bog() {
            // Default constructor required for Firebase
        }

        public Bog(String titel, String forfatter, double pris, String uddannelse, String semester, String stand) {
            this.titel = titel;
            this.forfatter = forfatter;
            this.pris = pris;
            this.uddannelse = uddannelse;
            this.semester = semester;
            this.stand = stand;
        }
    }
}
