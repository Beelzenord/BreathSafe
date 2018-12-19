package com.breathsafe.kth.breathsafe.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.R;
import com.google.android.gms.location.places.Place;

@Database(entities = {AirPollution.class,Location.class, LocationCategory.class}, version = 1 )
public abstract class Repository extends RoomDatabase{

    public abstract AirPollutionDoa airPollutionDoa();
    public abstract LocationCategoryDoa locationCategoryDoa();
    public abstract LocationDoa locationDoa();
    public static Repository instance;

    public static Repository getInstance(final Context con) {
        if(instance==null){
            synchronized (Repository.class){
                if(instance==null){
                    instance = Room.databaseBuilder(con,Repository.class,con.getResources().getString(R.string.database))
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
