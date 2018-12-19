package com.breathsafe.kth.breathsafe.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.breathsafe.kth.breathsafe.Model.AirPollution;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import java.util.List;

@Dao
public interface AirPollutionDoa {

    @Insert
    void Insert(AirPollution... airPollusion);

    @Update
    void Update(AirPollution... airPollusions);

    @Delete
    void Delete(AirPollution... airPollusions);

    @Query("SELECT * FROM AirPollution")
    List<AirPollution> getAirPollution();

    @Query("SELECT COUNT(*) FROM AirPollution")
    int countNumberOfEntities();

}
