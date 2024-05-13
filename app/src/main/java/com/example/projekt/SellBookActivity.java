package com.example.projekt;

import android.content.ContentResolver;
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
import com.example.projekt.Bog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SellBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Uri imageUri;
    ImageButton backButton;
    private ImageView imageViewBog;
    private Button btnUploadImage, btnIndsend;
    private EditText editTextTitel, editTextForfatter, editTextPris, editTextUddannelse, editTextSemester, editTextStand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);

        // Initialize DatabaseManager
        DatabaseManager.initialize();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://projekt-50207.appspot.com");


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

        // Set OnClickListener for the backButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellBookActivity.this, MainActivity.class));
                finish();
            }
        });
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

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
            imageViewBog.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase(final Uri imageUri) {
        final StorageReference fileReference = storageReference.child("uploads/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                submitBook(imageUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SellBookActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void submitBook(String imageUrl) {
        // Get the current user's ID from DatabaseManager
        String userId = DatabaseManager.getCurrentUserId();

        if (userId != null) {
            String titel = editTextTitel.getText().toString().trim();
            String forfatter = editTextForfatter.getText().toString().trim();
            String prisText = editTextPris.getText().toString().trim();
            String uddannelse = editTextUddannelse.getText().toString().trim();
            String semester = editTextSemester.getText().toString().trim();
            String stand = editTextStand.getText().toString().trim();

            // Create a new instance of Bog class
            Bog bog = new Bog(titel, forfatter, Double.parseDouble(prisText), uddannelse, semester, stand, imageUrl);

            // Set the user ID for the book
            bog.setUserId(userId);

            // Write data to the database using DatabaseManager
            DatabaseManager.writeData("books", bog);
        } else {
            // Handle the case when the user ID is not available
            Toast.makeText(SellBookActivity.this, "Unable to get current user ID", Toast.LENGTH_LONG).show();
        }

    }

}
