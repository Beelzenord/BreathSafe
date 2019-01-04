package com.breathsafe.kth.breathsafe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breathsafe.kth.breathsafe.Database.StoreToDatabase;
import com.breathsafe.kth.breathsafe.Maps.MapActivity;
import com.breathsafe.kth.breathsafe.Model.DisplayOnMapList;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationData;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{
    private static final String TAG = "FavoritesAdapter";
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
        final Location fingItem = list.get(i);
        viewHolder.textName.setText(fingItem.getName());
        viewHolder.textDesc.setText(fingItem.getFirstCategory());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(context,view,fingItem);


            }
        });

    }

    private void showPopup(final Context context, View view, final Location position) {

        PopupMenu popupMenu = new PopupMenu(context,view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.item_remover:
                        removeFromFavorite(context,position); return true;

                       // System.out.println("Clicked Removed " + position.getName()); return true;
                    case R.id.item_find:
                        findFavorites(context,position);
                        System.out.println("Clicked find"); return true;
                    default : return false;
                }

            }

            private void findFavorites(Context context, Location position) {

             //   Intent intent = new Intent(context, WelcomeActivity.class);
               // intent.putExtra("MAP", mapIndex);  // Pass the selected mapIndex
               // intent.putExtra("MAX", m.numLevels());
               // context.startActivity(intent);
                /*
                *
                *  case Constants.SEARCH_ACTIVITY_CALLBACK_MAPACTIVITY:
                intent = new Intent(this, MapActivity.class);
                break;
                * */
                DisplayOnMapList displayOnMapList = DisplayOnMapList.getInstance();
                displayOnMapList.add(position);
                Intent intent = new Intent(context, MapActivity.class);
                context.startActivity(intent);
            }

            private void removeFromFavorite(Context context, Location position) {
//                RemoveFromDatabase removeFromDatabase = new RemoveFromDatabase(context,position);
//                System.out.println("PRE-REMOVE " + position.getName());
//                removeFromDatabase.execute();
                list.remove(position);
                tryUpdateList(position);
                position.setFavorite(false);
                StoreToDatabase.UpdateLocation updateLocation = new StoreToDatabase.UpdateLocation(context, position);
                updateLocation.execute();

                notifyDataSetChanged();
               /* try {
                    if(removeFromDatabase.execute().get()){
                        list.remove(position);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }

    private void tryUpdateList(Location position) {
        List<Location> list = LocationData.getInstance().getList();
        if (list.size() > 0) {
            Log.i(TAG, "tryUpdateList: size > 0");
            for (Location l : list) {
                if (l.getId().equals(position.getId())) {
                    l.setFavorite(false);
                    break;
                }
            }
        }
        else
            Log.i(TAG, "tryUpdateList: ");
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
