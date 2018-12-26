package com.breathsafe.kth.breathsafe.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.breathsafe.kth.breathsafe.Model.Location;

import java.util.List;

@Dao
public interface LocationDoa {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Location location);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAsList(List<Location> locations);

    @Update
    void update(Location... location);

    @Update
    void updateAsList(List<Location> locations);

    @Delete
    void delete(Location... locations);

    @Query("SELECT * FROM Location")
    List<Location> getAllLocations();

    @Query("SELECT COUNT(*) FROM Location")
    int countNumberOfEntities();

}
