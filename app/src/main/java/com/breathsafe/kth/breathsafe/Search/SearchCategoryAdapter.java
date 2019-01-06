package com.breathsafe.kth.breathsafe.Search;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.R;

import java.util.List;

/**
 * Recycler view adapter for the Search for Category Fragment.
 */
public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.MyViewHolder> {
    private Activity activity;
    private List<LocationCategory> mDataset;
    private ItemClickListener mClickListener;

    public SearchCategoryAdapter(Activity Activity, List<LocationCategory> myDataset) {
        this.activity = Activity;
        this.mDataset = myDataset;
    }

    /**
     * Returns the location category selected at position i.
     * @param i The position selected.
     * @return The location at position i.
     */
    public LocationCategory getLocationCategory(int i) {
        return mDataset.get(i);
    }

    /**
     * When created, finds the view name to be used in each name.
     * @param viewGroup The view group of a certain layout.
     * @param i The position.
     * @return The viewholder created from the view items.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_in_recycler, viewGroup, false);

        MyViewHolder vh = new MyViewHolder(v);
        vh.category = v.findViewById(R.id.item_in_recycler);
        return vh;
    }

    /**
     * Sets new values for the viewholder when a user scrolls.
     * @param myViewHolder The viewholder to be updated.
     * @param i The position of the list.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        LocationCategory locationCategory = mDataset.get(i);
        myViewHolder.category.setText(locationCategory.getSingularName());
    }

    /**
     * Sets the listener to be used when a user clicks an item.
     * @param itemClickListener The listener to be used.
     */
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setList(List<LocationCategory> list) {
        this.mDataset = list;
    }

    /**
     * Implement this interface to receive notification when an item is clicked.
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * The number of items in the list.
     * @return The number of items in the list.
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * The view holder for every item in the list.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView category;
        public MyViewHolder(View v) {
            super(v);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
