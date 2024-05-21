package com.example.projekt;


// Importer alle de nødvendige klasser fra Android og Firebase bib
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Definer en ny "class"/"Aktivitet" for bruger oprettelse
public class SigninActivity extends AppCompatActivity {
    //Deklarere variabler for inputfelter, knap og tekstvisning og firebase autentificering
    public EditText signupEmail, signupPassword;
    Button btnSignUp;
    TextView loginRedirectText;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // Initaliserer Firebase autentificering instans og UI komponenter
        firebaseAuth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.Email);
        signupPassword = findViewById(R.id.Password);
        btnSignUp = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.txtSignIn);
        // Sætter en OnClicklistener op "sign up" knappen
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Henter email og password fra inputfelterne
                String Email = signupEmail.getText().toString();
                String Password = signupPassword.getText().toString();
                // Tjekker om Email-feltet er tomt
                if (Email.isEmpty()) {
                    signupEmail.setError("Provide your Email first!");
                    signupEmail.requestFocus();
                }
                // Tjekker om password-feltet er tomt
                if (Password.isEmpty()) {
                    signupPassword.setError("Set your password");
                    signupPassword.requestFocus();
                }
                // Opretter en ny bruger i Firebase med ovenstående
                    firebaseAuth.createUserWithEmailAndPassword(Email,Password)
                            .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Tjekker om brugeroprettelsen var succesfuld
                                    if (task.isSuccessful()) {
                                        // Viser succesbesked
                                        Toast.makeText(SigninActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        // Starter hovedaktiviteten "SigninActivity" og afslutter tilmedingsaktiviteten
                                        startActivity(new Intent(SigninActivity.this, MainActivity.class));
                                        finish(); //
                                    } else {
                                        // Ellers vises fejlbesked
                                        Toast.makeText(SigninActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

        // Sætter en "OnClickListener" på login omdirigeringsteksten
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Stater loginaktiviteten og afslutter tilmeldingsaktiviteten
                    startActivity(new Intent(SigninActivity.this, LoginActivity.class));
                    finish();
                }
            });
        }
    }