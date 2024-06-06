package com.example.projekt;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//Definer en ny class/activity for at sælge en bog
public class SellBookActivity extends AppCompatActivity {

    // Konstant for at vælge billedanmodning
    private static final int PICK_IMAGE_REQUEST = 1;
    // Deklarerer variabler for Firebase storage of reference
    private FirebaseStorage storage;
    private StorageReference storageReference;
    // Deklarerer variabler for UI komponenter og billede URI
    private Uri imageUri;
    ImageButton backButton;
    private ImageView imageViewBog;
    private Button btnUploadImage, btnIndsend;
    private EditText editTextTitel, editTextForfatter, editTextPris, editTextUddannelse, editTextSemester, editTextStand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);

        // Initialiserer DatabaseManager
        DatabaseManager.initialize();
        // Initialiserer Firebase storage og references
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://projekt-50207.appspot.com");

        // Initialiserer UI komponenter
        imageViewBog = findViewById(R.id.imageViewBog);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        backButton = findViewById(R.id.backButton);
        btnIndsend = findViewById(R.id.btnIndsend);
        editTextTitel = findViewById(R.id.editTextTitel);
        editTextForfatter = findViewById(R.id.editTextForfatter);
        editTextPris = findViewById(R.id.editTextPris);
        editTextUddannelse = findViewById(R.id.editTextUddannelse);
        editTextSemester = findViewById(R.id.editTextSemester);
        editTextStand = findViewById(R.id.editTextStand);

        // Sætter en OnClickListener for tilbage knappen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellBookActivity.this, MainActivity.class));
                finish();
            }
        });
        // Sætter en OnClickListener på upload billede knappen
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            // Åbner filvælgeren for at vælge et billede
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Sætter en OnClickListener på indsend knappeb
        btnIndsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    uploadImageToFirebase(imageUri);
                } else {
                    Toast.makeText(SellBookActivity.this, "Vælg et billede først", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Åbner Filechooser for at vælge et billede
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // Indlæser og viser det valgte billde ved hjælp af glide bib
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.baseline_insert_photo_24)
                    .into(imageViewBog);

        }
    }
    // Uploader billede til firebase storage
    private void uploadImageToFirebase(final Uri imageUri) {
        final StorageReference fileReference = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Får download URL for billedet
                                String imageUrl = uri.toString();
                                // Indsender bogoplysninger med billede URL
                                submitBook(imageUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Viser fejlbesked hvis fejl opstår
                        Toast.makeText(SellBookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Henter filtypen URI
    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(getContentResolver().getType(uri));
    }
    // Indsender bogoplysninger til databasen
    private void submitBook(String imageUrl) {
        // Henter nuværende Bruger-ID fra DatabaseManager
        String userId = DatabaseManager.getCurrentUserId();

        if (userId != null) {
            // Henter tekst fra input felter
            String titel = editTextTitel.getText().toString().trim();
            String forfatter = editTextForfatter.getText().toString().trim();
            String prisText = editTextPris.getText().toString().trim();
            String uddannelse = editTextUddannelse.getText().toString().trim();
            String semester = editTextSemester.getText().toString().trim();
            String stand = editTextStand.getText().toString().trim();

            // Opretter en ny instans af bog klassen
            Bog bog = new Bog(titel, forfatter, Double.parseDouble(prisText), uddannelse, semester, stand, imageUrl);

            // Sætter UserId for bogen
            bog.setUserId(userId);

            // Skriver data til databasen ved hjælp af databaseManager
            DatabaseManager.writeData("books", bog, SellBookActivity.this);
        } else {
            // Håndterer tilfælde hvor bruger ID ikke er tilgængelig
            Toast.makeText(SellBookActivity.this, "Unable to get current user ID", Toast.LENGTH_LONG).show();
        }

    }

}
