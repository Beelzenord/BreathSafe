package com.breathsafe.kth.breathsafe.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.breathsafe.kth.breathsafe.FilterSearchActivity;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.R;

import java.util.ArrayList;
import java.util.List;

public class LocationCategoryAdapter extends ArrayAdapter<LocationCategory> {

    private ArrayList<LocationCategory> list;
    private FilterSearchActivity activity;


    public LocationCategoryAdapter(@NonNull Context context, int resource, @NonNull List<LocationCategory> objects) {
        super(context, resource, objects);
        this.list = (ArrayList<LocationCategory>) objects;
    }
    public LocationCategoryAdapter(@NonNull Context context, int resource, @NonNull List<LocationCategory> objects, Activity currentActivity) {
        super(context, resource, objects);
        this.list = (ArrayList<LocationCategory>) objects;
        this.activity =(FilterSearchActivity) currentActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        return initView(position,convertView,parent);

    }

    static class ViewHolder
    {
        protected LocationCategory locationCategory;
        protected CheckBox check;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getDropDownView(position, convertView, parent);
        return initView(position,convertView,parent);
    }
    public View initView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardlocation,parent,false);
        }


        ViewHolder viewHolder = new ViewHolder();
      //  viewHolder.radioBtn

        viewHolder.check = convertView.findViewById(R.id.checkBox);

        viewHolder.check.setText(getItem(position).getSingularName());

        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println("checked " + compoundButton.getText() + " " + b);
                activity.feedBack(getItem(position),b);
            }
        });

        return convertView;
    }
}
