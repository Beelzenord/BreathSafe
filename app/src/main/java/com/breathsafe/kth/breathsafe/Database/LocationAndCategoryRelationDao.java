package com.breathsafe.kth.breathsafe.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.breathsafe.kth.breathsafe.Model.Location;
import com.breathsafe.kth.breathsafe.Model.LocationAndCategoryRelation;
import com.breathsafe.kth.breathsafe.Model.LocationCategory;

import java.util.List;

/**
 * Access to the relation between Locations and LocationCategories.
 */
@Dao
public interface LocationAndCategoryRelationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(LocationAndCategoryRelation locationAndCategoryRelation);


    @Query("SELECT * FROM Location INNER JOIN LocationAndCategoryRelation ON " +
            "Location.id=LocationAndCategoryRelation.locationID WHERE " +
            "LocationAndCategoryRelation.locationCategoryID=:locationCategoryID")
    List<Location> getLocationsForCategories(final String locationCategoryID);

    @Query("SELECT * FROM LocationCategory INNER JOIN LocationAndCategoryRelation ON " +
            "LocationCategory.id=LocationAndCategoryRelation.locationCategoryID WHERE " +
            "LocationAndCategoryRelation.locationID=:locationID")
            List<LocationCategory> getCategoriesForLocations(final String locationID);
}
