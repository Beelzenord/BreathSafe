package com.breathsafe.kth.breathsafe.Search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.R;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{
    private Context context;
    private List<Location> list;


    public FavoritesAdapter(Context context, List<Location> listItems) {
        this.context = context ;
        this.list = listItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context)
                .inflate(R.layout.row_favs,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.ViewHolder viewHolder, int i) {
        Location fingItem = list.get(i);
        viewHolder.textName.setText(fingItem.getName());
        viewHolder.textDesc.setText(fingItem.getCategories().getSingleCategory());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textName;
        public TextView textDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.teeto);
            textDesc = (TextView) itemView.findViewById(R.id.deeto);
        }
    }
}
