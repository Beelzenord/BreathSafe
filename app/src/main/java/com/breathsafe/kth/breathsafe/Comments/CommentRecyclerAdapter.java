package com.breathsafe.kth.breathsafe.Comments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.Model.CommentsModel.Comment;
import com.breathsafe.kth.breathsafe.R;

import java.util.Calendar;
import java.util.List;

/**
 * Recycler adapter to show all the comments of a Location.
 */
public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.MyViewHolder> {
    private static final String TAG = "CommentRecyclerAdapter";
    private Activity activity;
    private List<Comment> mDataset;
    private CommentRecyclerAdapter.ItemClickListener mClickListener;

    public CommentRecyclerAdapter(Activity Activity, List<Comment> myDataset) {
        this.activity = Activity;
        this.mDataset = myDataset;
    }

    /**
     * Returns the comment name selected at position i.
     * @param i The position selected.
     * @return The location at position i.
     */
    public Comment getItem(int i) {
        return mDataset.get(i);
    }

    /**
     * When created, finds the view name to be used in each item.
     * @param viewGroup The view group of a certain layout.
     * @param i The position.
     * @return The viewholder created from the view items.
     */
    @NonNull
    @Override
    public CommentRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.name_date, viewGroup, false);

        CommentRecyclerAdapter.MyViewHolder vh = new CommentRecyclerAdapter.MyViewHolder(v);
        vh.title = v.findViewById(R.id.teeto);
        vh.author = v.findViewById(R.id.deeto);
        vh.time = v.findViewById(R.id.deeto_timestamp);
        return vh;
    }

    /**
     * Sets new values for the viewholder when a user scrolls.
     * @param myViewHolder The viewholder to be updated.
     * @param i The position of the list.
     */
    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.MyViewHolder myViewHolder, int i) {
        try {
            Comment comment = mDataset.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(comment.getTimestamp());
            myViewHolder.title.setText(comment.getTitle());
            myViewHolder.author.setText(comment.getUsername());
            myViewHolder.time.setText(calendar.getTime().toString());

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the listener to be used when a user clicks an name.
     * @param itemClickListener The listener to be used.
     */
    void setClickListener(CommentRecyclerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setList(List<Comment> list) {
        this.mDataset = list;
    }

    public List<Comment> getList() {
        return mDataset;
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
     * The view holder from every item in the list.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView title;
        public TextView author;
        public TextView time;
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
