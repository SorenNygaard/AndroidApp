package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    TextView textview;
    FirebaseUser user;
    RecyclerView recyclerView;
    ArrayList<ModelRecyclerView> bookArrayList;

    // Declaring adapter at the class level
    AdapterRecycleView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        textview = findViewById(R.id.infotexther);
        user = auth.getCurrentUser();


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            textview.setText(user.getEmail());
        }

        Button buttonLogout = findViewById(R.id.logud);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button addbookbtn = findViewById(R.id.menu2);
        addbookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SellBookActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // RecyclerView setup
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookArrayList = new ArrayList<>();
        adapter = new AdapterRecycleView(this, bookArrayList); // Initializing adapter
        recyclerView.setAdapter(adapter);
        Log.d("RVLoad", "RecyclerView Adapter set up");
        loadBookItems();

    }

    private void loadBookItems() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("books");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelRecyclerView model = ds.getValue(ModelRecyclerView.class);
                    if (model != null) {
                        bookArrayList.add(model);
                    } else {
                        Log.e("RVLoad", "Model is null");
                    }
                }
                adapter.notifyDataSetChanged();
                Log.d("RVLoad", "Data loaded successfully");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load books: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("RVLoad", "Failed to load books: " + error.getMessage());
            }
        });
    }
}


