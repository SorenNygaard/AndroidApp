package com.example.projekt;



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
import com.google.firebase.auth.FirebaseUser;
// Definerer en ny Class/Aktivitet for bruger login
public class LoginActivity extends AppCompatActivity {
// Deklarerer virabler for inputfelter, knap tekstvisning og Firebase aut
    public EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView registerRedirectText;
    FirebaseAuth firebaseAuth;

    // Deklarer en AuthStateListener for at se efter ændringer i brugernes autentificeringstilstand
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialiserer Firebase auth instans og UI komponenter
        firebaseAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.Email);
        loginPassword = findViewById(R.id.Password);
        btnLogin = findViewById(R.id.btnLogIn);
        registerRedirectText = findViewById(R.id.txtRegister);
        // Definerer adfæren for authStateLuatener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Tjekker om brugeren er logget ind
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Hvis brugern er logget ind, vis en besked og start "mainactivity"
                    Toast.makeText(LoginActivity.this, "User logged in ", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(I);
                    finish();
                } else {
                    // Hvis brugeren fejler, hvis en besked
                    Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Sætter en "setOnClickListener" på registrerings-omdirigeringsteksten
        registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starter Sign up activity, og afslutter login activity
                Intent I = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(I);
                finish();
            }
        });

        // Sætter en "OnClickListener" på login klappen
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Henter email og password fra inputfelterne
                String Email = loginEmail.getText().toString().trim();
                String Password = loginPassword.getText().toString().trim();
                if (Email.isEmpty()) {
                    loginEmail.setError("Provide your Email first!");
                    loginEmail.requestFocus();
                }
                if (Password.isEmpty()) {
                    loginPassword.setError("Enter Password!");
                    loginPassword.requestFocus();

                }
                // Logger brugeren und med email og password i firebase
                firebaseAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Tjekker om login er succesfukd
                                    if (task.isSuccessful()) {
                                        // Viser en succesbesked
                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Viser en fejlbesked
                                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Tilføjer AuthstateListener når aktiviteten starter
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}