package com.breathsafe.kth.breathsafe.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.CategoriesConverter;
import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationAndCategoryRelation;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;
import com.breathsafe.kth.breathsafe.R;
import com.google.android.gms.location.places.Place;

/**
 * SQLite database to locally store data about Location, LocationCategory and AirPollution.
 */
@Database(entities = {AirPollution.class,Location.class, LocationCategory.class, LocationAndCategoryRelation.class}, version = 6 )
@TypeConverters({CategoriesConverter.class})
public abstract class Repository extends RoomDatabase{

    public abstract AirPollutionDoa airPollutionDoa();
    public abstract LocationCategoryDoa locationCategoryDoa();
    public abstract LocationDoa locationDoa();
    public abstract LocationAndCategoryRelationDao locationAndCategoryRelationDao();
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

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.beginTransaction();
            database.execSQL("DROP TABLE Location");
            database.execSQL("CREATE TABLE Location(" +
                    "id"+
                    "categories"+
                    "name"+
                    "timeCreated"+
                    "ted"+
                    "latitude"+
                    "longitude"+
                    "retrieved)");
            database.endTransaction();
        }
    };
}
