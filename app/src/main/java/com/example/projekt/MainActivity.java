package com.example.projekt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * The MainActivity class is the main entry point for the application.
 * It handles the user interface for searching and displaying a list of books,
 * and provides navigation to other activities such as login, selling books, and user settings.
 */
public class MainActivity extends AppCompatActivity implements AdapterRecycleView.OnItemClickListener {

    // UI components
    SearchView searchbar;
    RecyclerView recyclerView;
    ArrayList<ModelRecyclerView> bookArrayList;
    AdapterRecycleView adapter;
    DatabaseReference databaseReference;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then
     *                          this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle)(dette betyder at den gemmer
     *                          en instance så hvis appen f.eks. crasher kan den loade denne og bruge den til at genoprette den tilstand der var før lukning/crash ).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DatabaseManager
        DatabaseManager.initialize();

        // Initialize database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Set up SearchView
        searchbar = findViewById(R.id.searchBar);

        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setIconified(false);
            }
        });
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
        //Tilføj bog knap
        Button addbookbtn = findViewById(R.id.menu2);
        addbookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellBookActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //Bruger indstillinger
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


    /**
     * Handles item clicks in the RecyclerView.
     * @param position The position of the clicked item.
     */
    @Override
    public void onItemClick(int position) {
        // Handle item click here
        ModelRecyclerView clickedItem = bookArrayList.get(position);

        // Create an intent to launch the buyAndSellActivity
        Intent intent = new Intent(MainActivity.this, buyAndSellActivity.class);

        // Pass necessary data to the buyAndSellActivity
        intent.putExtra("book_image", clickedItem.getImageUrl());
        intent.putExtra("book_title", clickedItem.getTitel());
        intent.putExtra("book_author", clickedItem.getForfatter());
        intent.putExtra("book_education", clickedItem.getUddannelse());
        intent.putExtra("book_semester", clickedItem.getSemester());
        intent.putExtra("book_condition", clickedItem.getStand());
        intent.putExtra("book_price", clickedItem.getPris());

        startActivity(intent);
    }

    /**
     * Loads book items from the Firebase database.
     */
    private void loadBookItems() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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

    /**
     * Searches for books in the Firebase database based on the given query.
     * @param query The search query.
     */
    private void searchBooks(String query) {
        Log.d("MainActivity", "Searching for books with query: " + query); // Log the search query

        Query searchQuery = databaseReference.orderByChild("titel")
                .startAt(query.toUpperCase())
                .endAt(query.toLowerCase() + "\uf8ff");

        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("MainActivity", "Received search results"); // Log when search results are received
                bookArrayList.clear(); // Clear the existing dataset

                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelRecyclerView model = ds.getValue(ModelRecyclerView.class);
                    if (model != null) {
                        // Check if the query matches the beginning of the title, regardless of case
                        if (model.getTitel().toUpperCase().startsWith(query.toUpperCase())) {
                            bookArrayList.add(model); // Add the model to the dataset if it matches
                        }
                    }
                }

                adapter.notifyDataSetChanged(); // Notify the RecyclerView adapter of dataset changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
