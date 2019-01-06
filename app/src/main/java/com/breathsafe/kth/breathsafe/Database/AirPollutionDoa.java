package com.breathsafe.kth.breathsafe.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.breathsafe.kth.breathsafe.Model.AirPollution;

import java.util.List;

/**
 * Enables access to the Air Pollution data in the SQLite database.
 */
@Dao
public interface AirPollutionDoa {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(AirPollution airPollusion);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAsList(List<AirPollution> airPollutions);

    @Update
    void update(AirPollution airPollusions);

    @Update
    void updateAsList(List<AirPollution> airPollutions);

    @Delete
    void delete(AirPollution... airPollusions);

    @Query("DELETE FROM AirPollution")
    void deleteAllAirPollution();

    @Query("SELECT * FROM AirPollution")
    List<AirPollution> getAirPollution();

    @Query("SELECT COUNT(*) FROM AirPollution")
    int countNumberOfEntities();

}
