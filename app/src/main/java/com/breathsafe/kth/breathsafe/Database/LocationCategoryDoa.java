package com.breathsafe.kth.breathsafe.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import java.util.List;

@Dao
public interface LocationCategoryDoa {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(LocationCategory locationCategory);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAsList(List<LocationCategory> locationCategories);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LocationCategory locationCategories);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAsList(List<LocationCategory> locationCategories);

    @Delete
    void delete(LocationCategory... locationCategories);

    @Query("SELECT * FROM LocationCategory")
    List<LocationCategory> getAllLocationCategory();

    @Query("SELECT COUNT(*) FROM LocationCategory")
    int countNumberOfEntities();
}
