package com.example.projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * AdapterRecycleView is a class responsible for managing the data within a RecyclerView.
 * It binds data to individual items in the RecyclerView.
 */
public class AdapterRecycleView extends RecyclerView.Adapter<AdapterRecycleView.HolderBook> {
    private Context context;
    private ArrayList<ModelRecyclerView> bookArrayList;// ArrayList to hold the data to be displayed

    /**
     * Constructor for the AdapterRecycleView class.
     *
     * @param context       Context of the application/activity
     * @param bookArrayList ArrayList containing ModelRecyclerView objects
     */
    public AdapterRecycleView(Context context, ArrayList<ModelRecyclerView> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }
    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it's bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new HolderBook that holds a View of the given view type.
     */
    @NonNull
    @Override
    public HolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        // R.layout.book_item refers to the XML layout file book_item.xml,
        // which defines the layout for each item in the RecyclerView.
        // The inflated View is then assigned to the view variable,
        // which is used to create a new instance of the HolderBook class.
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new HolderBook(view);
    }
    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The HolderBook which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull HolderBook holder, int position) {
        // Bind data at the specified position to the ViewHolder
        holder.bindData(bookArrayList.get(position));
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the data set.
     */
    @Override
    public int getItemCount() {
        return bookArrayList.size(); // Return the actual size of the data list
    }
    /**
     * ViewHolder class for holding UI views for the RecyclerView in activity_main.xml.
     * Represents an individual item in the RecyclerView.
     */
    class HolderBook extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView uddannelseTextView;
        TextView standTextView;
        TextView semesterTextView;
        TextView prisTextView;

        /**
         * Constructor for HolderBook.
         *
         * @param itemView The root view of the item layout.
         */
        public HolderBook(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemtitle);
            authorTextView = itemView.findViewById(R.id.itemforfatter);
            uddannelseTextView = itemView.findViewById(R.id.itemuddannelse);
            standTextView = itemView.findViewById(R.id.itemstand);
            semesterTextView = itemView.findViewById(R.id.itemsemester);
            prisTextView = itemView.findViewById(R.id.itempris);
        }

        /**
         * Method to bind data to the ViewHolder.
         *
         * @param model ModelRecyclerView object containing data to be bound
         */
        public void bindData(ModelRecyclerView model) {
            // Set data to respective TextViews
            titleTextView.setText(model.getTitel());
            authorTextView.setText(model.getForfatter());
            uddannelseTextView.setText(model.getUddannelse());
            standTextView.setText(model.getStand());
            semesterTextView.setText(model.getSemester());
            prisTextView.setText(String.valueOf(model.getPris()));
        }
    }}
