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

public class AdapterRecycleView extends RecyclerView.Adapter<AdapterRecycleView.HolderBook> {
    private final Context context;
    private final ArrayList<ModelRecyclerView> bookArrayList;
    private OnItemClickListener mListener;

    // Interface for click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AdapterRecycleView(Context context, ArrayList<ModelRecyclerView> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }
    @NonNull
    @Override
    public HolderBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new HolderBook(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBook holder, int position) {
        ModelRecyclerView model = bookArrayList.get(position);
        holder.bindData(model);
        Picasso.get().load(model.getImageUrl()).into(holder.imageBook);

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    class HolderBook extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView uddannelseTextView;
        TextView standTextView;
        TextView semesterTextView;
        TextView prisTextView;
        ImageView imageBook;

        public HolderBook(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemtitle);
            authorTextView = itemView.findViewById(R.id.itemforfatter);
            uddannelseTextView = itemView.findViewById(R.id.itemuddannelse);
            standTextView = itemView.findViewById(R.id.itemstand);
            semesterTextView = itemView.findViewById(R.id.itemsemester);
            prisTextView = itemView.findViewById(R.id.itempris);
            imageBook = itemView.findViewById(R.id.imageBook);

            // Set click listener
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

        public void bindData(ModelRecyclerView model) {
            titleTextView.setText(model.getTitel());
            authorTextView.setText(model.getForfatter());
            uddannelseTextView.setText(model.getUddannelse());
            standTextView.setText(model.getStand());
            semesterTextView.setText(model.getSemester());
            prisTextView.setText(String.valueOf(model.getPris()));
        }
    }
}
