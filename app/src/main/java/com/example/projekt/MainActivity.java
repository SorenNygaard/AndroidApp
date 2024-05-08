package com.example.projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SearchView searchbar;
    RecyclerView recyclerView;
    ArrayList<ModelRecyclerView> bookArrayList;
    AdapterRecycleView adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Set up SearchView
        searchbar = findViewById(R.id.searchBar);
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchBooks(newText);
                return false;
            }
        });

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookArrayList = new ArrayList<>();
        adapter = new AdapterRecycleView(this, bookArrayList);
        recyclerView.setAdapter(adapter);

        // Set up buttons
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

        Button buttonuser = findViewById(R.id.menu3);
        buttonuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserSettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Load book items
        loadBookItems();
    }

    private void loadBookItems() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelRecyclerView model = ds.getValue(ModelRecyclerView.class);
                    if (model != null) {
                        bookArrayList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load books: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchBooks(String query) {
        // Convert the query to lowercase
        String lowercaseQuery = query.toLowerCase();

        // Convert the query to uppercase
        String uppercaseQuery = query.toUpperCase();

        // Get the reference to the "books" node in the Firebase Realtime Database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("books");

        // Construct the Firebase query for searching by the "titel" field
        Query searchQuery = reference.orderByChild("titel")
                .startAt(uppercaseQuery)
                .endAt(lowercaseQuery + "\uf8ff");

        // Add a ValueEventListener to the query
        searchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing bookArrayList
                bookArrayList.clear();

                // Iterate through the DataSnapshot to retrieve the matching book items
                for (DataSnapshot ds : snapshot.getChildren()) {
                    // Convert the DataSnapshot into a ModelRecyclerView object
                    ModelRecyclerView model = ds.getValue(ModelRecyclerView.class);
                    if (model != null) {
                        // Add the ModelRecyclerView object to the bookArrayList
                        bookArrayList.add(model);
                    }
                }

                // Notify the adapter that the dataset has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the onCancelled event, if needed
                Toast.makeText(MainActivity.this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

