package com.breathsafe.kth.breathsafe.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

@Entity(tableName = "LocationAndCategoryRelation",
        primaryKeys = {"locationID", "locationCategoryID"},
        foreignKeys = {
            @ForeignKey(entity = Location.class,
                parentColumns = "id",
                childColumns = "locationID"),
            @ForeignKey(entity = LocationCategory.class,
                parentColumns = "id",
                childColumns = "locationCategoryID")
        })
public class LocationAndCategoryRelation {
    @NonNull
    @ColumnInfo (name = "locationID")
    private String locationID;
    @NonNull
    @ColumnInfo (name = "locationCategoryID")
    private String locationCategoryID;

    public LocationAndCategoryRelation(String locationID, String locationCategoryID) {
        this.locationID = locationID;
        this.locationCategoryID = locationCategoryID;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationCategoryID() {
        return locationCategoryID;
    }

    public void setLocationCategoryID(String locationCategoryID) {
        this.locationCategoryID = locationCategoryID;
    }
}
