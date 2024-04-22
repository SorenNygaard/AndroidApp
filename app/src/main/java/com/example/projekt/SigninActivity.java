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

public class SigninActivity extends AppCompatActivity {
    public EditText signupEmail, signupPassword;
    Button btnSignUp;
    TextView loginRedirectText;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //
        firebaseAuth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.Email);
        signupPassword = findViewById(R.id.Password);
        btnSignUp = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.txtSignIn);
        //
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = signupEmail.getText().toString();
                String Password = signupPassword.getText().toString();
                if (Email.isEmpty()) {
                    signupEmail.setError("Provide your Email first!");
                    signupEmail.requestFocus();
                }
                if (Password.isEmpty()) {
                    signupPassword.setError("Set your password");
                    signupPassword.requestFocus();
                }
                    firebaseAuth.createUserWithEmailAndPassword(Email,Password)
                            .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SigninActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SigninActivity.this, MainActivity.class));
                                        finish(); // Finish this activity to prevent going back with back button
                                    } else {
                                        Toast.makeText(SigninActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SigninActivity.this, LoginActivity.class));
                    finish();
                }
            });
        }
    }