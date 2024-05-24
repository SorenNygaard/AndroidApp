package com.example.projekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * Adapter for the RecyclerView that displays book items.
 * It binds data to the views and handles item click events.
 */
public class AdapterRecycleView extends RecyclerView.Adapter<AdapterRecycleView.HolderBook> {
    private final Context context;
    private final ArrayList<ModelRecyclerView> bookArrayList;
    private OnItemClickListener mListener;

    /**
     * Interface for item click listener.
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    /**
     * Sets the item click listener.
     * @param listener The listener to set.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Constructor for the adapter.
     * @param context The context in which the adapter is used.
     * @param bookArrayList The list of books to display.
     */
    public AdapterRecycleView(Context context, ArrayList<ModelRecyclerView> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder.
     * @param parent The ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new HolderBook.
     */
    @NonNull
    @Override
    public HolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the book_item layout and create a new HolderBook
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new HolderBook(view);
    }

    /**
     * Called to display the data at the specified position.
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull HolderBook holder, int position) {
        // Get the current book model
        ModelRecyclerView model = bookArrayList.get(position);

        // Bind the data to the ViewHolder
        holder.bindData(model);

        // Load the book image using Picasso
        Picasso.get().load(model.getImageUrl()).into(holder.imageBook);
    }

    /**
     * Returns the total number of items in the dataset.
     * @return The size of the bookArrayList.
     */
    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    /**
     * ViewHolder for the RecyclerView that represents a single book item.
     */
    class HolderBook extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView standTextView;
        TextView prisTextView;
        ImageView imageBook;

        /**
         * Constructor for the HolderBook.
         * @param itemView The view of the item.
         */
        public HolderBook(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            titleTextView = itemView.findViewById(R.id.itemtitle);
            authorTextView = itemView.findViewById(R.id.itemforfatter);
            // uddannelseTextView = itemView.findViewById(R.id.itemuddannelse); // Uncomment if needed
            standTextView = itemView.findViewById(R.id.itemstand);
            // semesterTextView = itemView.findViewById(R.id.itemsemester); // Uncomment if needed
            prisTextView = itemView.findViewById(R.id.itempris);
            imageBook = itemView.findViewById(R.id.imageBook);

            // Set click listener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        /**
         * Binds data to the views.
         * @param model The book model to bind.
         */
        public void bindData(ModelRecyclerView model) {
            // Set the text for each view based on the book model
            titleTextView.setText(model.getTitel());
            authorTextView.setText(model.getForfatter());
            // uddannelseTextView.setText(model.getUddannelse()); // Uncomment if needed
            standTextView.setText(model.getStand());
            // semesterTextView.setText(model.getSemester()); // Uncomment if needed
            prisTextView.setText(model.getPris() + "kr");
        }
    }
}
