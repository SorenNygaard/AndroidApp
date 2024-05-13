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
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterRecycleView.OnItemClickListener {

    SearchView searchbar;
    RecyclerView recyclerView;
    ArrayList<ModelRecyclerView> bookArrayList;
    AdapterRecycleView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseManager
        DatabaseManager.initialize();

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

        // Set item click listener after initializing adapter
        adapter.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(int position) {
        // Handle item click here
        ModelRecyclerView clickedItem = bookArrayList.get(position);

        // Create an intent to launch the buyAndSellActivity
        Intent intent = new Intent(MainActivity.this, buyAndSellActivity.class);

        // Pass necessary data to the buyAndSellActivity
        intent.putExtra("book_title", clickedItem.getTitel());
        intent.putExtra("book_author", clickedItem.getForfatter());
        intent.putExtra("book_education", clickedItem.getUddannelse());
        intent.putExtra("book_semester", clickedItem.getSemester());
        intent.putExtra("book_condition", clickedItem.getStand());
        intent.putExtra("book_price", clickedItem.getPris());

        startActivity(intent);
    }
    private void loadBookItems() {
        // Read data from the "books" node using DatabaseManager
        DatabaseManager.readData("books", new ValueEventListener() {
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
        // Search books using DatabaseManager
        DatabaseManager.searchBooks("books", query, new ValueEventListener() {
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
                Toast.makeText(MainActivity.this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
