package com.breathsafe.kth.breathsafe.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;


//@Entity(tableName = "Location",foreignKeys = @ForeignKey(entity = LocationCategory.class,
//        parentColumns = "id",childColumns = "childId",
//        onDelete = CASCADE))
@Entity(tableName = "Location")
public class Location {
    @NonNull
    @PrimaryKey
    String id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "timeCreated")
    long timeCreated;
    @ColumnInfo(name = "timeUpdated")
    long timeUpdated;
    @ColumnInfo(name = "latitude")
    double latitude;
    @ColumnInfo(name = "longitude")
    double longitude;
    @ColumnInfo(name = "retrieved")
    long retrieved;
    @ColumnInfo(name = "favorite")
    boolean favorite;

    @ColumnInfo(name = "childId")
    String childId;

    @Ignore
    private double averageAQI;
    @Ignore
    private boolean isOnMap;
    @Ignore
    private List<String> categoryNames;
    @Ignore
    private List<String> tmpLCID;

    @Ignore
    public Location(String id, List<String> categoryNames, List<String> tmpLCID, String name, long timeCreated, long timeUpdated, double latitude, double longitude, long retrieved) {
        this.id = id;
        this.name = name;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.latitude = latitude;
        this.longitude = longitude;
        this.retrieved = retrieved;
        this.categoryNames = categoryNames;
        this.tmpLCID = tmpLCID;
        this.favorite = false;
        averageAQI = -1;
        isOnMap = false;
    }

    public Location(@NonNull String id, String name, long timeCreated, long timeUpdated, double latitude, double longitude, long retrieved, boolean favorite) {
        this.id = id;
//        this.categories = categories;
        this.name = name;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.latitude = latitude;
        this.longitude = longitude;
        this.retrieved = retrieved;
        this.favorite = favorite;
        averageAQI = -1;
        isOnMap = false;
        categoryNames = new ArrayList<>();
        tmpLCID = new ArrayList<>();
    }

    @Ignore
    public Location(String name) {
        this.name = name;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public void addCategoryNames(String name) {
        categoryNames.add(name);
    }

    public List<String> getTmpLCID() {
        return tmpLCID;
    }

    public void setTmpLCID(List<String> tmpLCID) {
        this.tmpLCID = tmpLCID;
    }

    //    public Categories getCategories() {
//        return categories;
//    }

//    public void setCategories(Categories categories) {
//        this.categories = categories;
//    }

//    public List<String> getTmpLCID() {
//        return tmpLCID;
//    }

    public void setCategories(List<String> tmpLCID) {
        this.tmpLCID = tmpLCID;
    }

    /*public void addCategory(String c) {
            if (categories.getCategories() != null)
                categories.getCategories().add(c);
        }*/
    public void addTmpLCID(String c) {
        if (tmpLCID != null)
            tmpLCID.add(c);
    }

    /*public boolean containsCategory(String category) {
        for (String c : categories.getCategories()) {
            if (c.equalsIgnoreCase(category))
                return true;
        }
        return false;
    }*/
    public boolean containsTmpLCID(String category) {
        for (String c : tmpLCID) {
            if (c.equalsIgnoreCase(category))
                return true;
        }
        return false;
    }

    public List<String> setTmpLCID() {
//        return categories.getCategories();
        return tmpLCID;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(long timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getRetrieved() {
        return retrieved;
    }

    public void setRetrieved(long retrieved) {
        this.retrieved = retrieved;
    }

    /*public String getFirstCategory() {
        if (categories != null) {
            List<String> category = categories.getCategories();
            if (category != null && category.size() > 0)
                return category.get(0);
        }
        return null;
    }*/

    public String getFirstTmpLCID() {
        if (tmpLCID != null) {
            if (tmpLCID.size() > 0)
                return tmpLCID.get(0);
        }
        return null;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    @Ignore
    public double getAverageAQI() {
        return averageAQI;
    }

    @Ignore
    public void setAverageAQI(double averageAQI) {
        this.averageAQI = averageAQI;
    }

    @Ignore
    public boolean isOnMap() {
        return isOnMap;
    }

    @Ignore
    public void setOnMap(boolean onMap) {
        isOnMap = onMap;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", categories=" + categoryNames +
                ", name='" + name + '\'' +
                ", timeCreated=" + timeCreated +
                ", timeUpdated=" + timeUpdated +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", retrieved=" + retrieved +
                ", childId='" + childId + '\'' +
                ", averageAQI=" + averageAQI +
                '}';
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean containsCategoryName(String category) {
        return categoryNames.contains(category);
    }

    public String getFirstCategory() {
        if (categoryNames != null) {
            if (categoryNames.size() > 0)
                return categoryNames.get(0);
        }
        return "";
    }
}
