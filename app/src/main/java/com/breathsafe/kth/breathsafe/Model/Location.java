package com.breathsafe.kth.breathsafe.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "Location")
public class Location {
    @NonNull
    @PrimaryKey
    String id;
    @ColumnInfo(name = "item")
    Categories categories;
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

    @Ignore
    double averageAQI;

    @Ignore
    public Location(String id, List<String> categories, String name, long timeCreated, long timeUpdated, double latitude, double longitude, long retrieved) {
        this.id = id;
        this.categories = new Categories(categories);
        this.name = name;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.latitude = latitude;
        this.longitude = longitude;
        this.retrieved = retrieved;
        averageAQI = -1;
    }

    public Location(@NonNull String id, Categories categories, String name, long timeCreated, long timeUpdated, double latitude, double longitude, long retrieved) {
        this.id = id;
        this.categories = categories;
        this.name = name;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.latitude = latitude;
        this.longitude = longitude;
        this.retrieved = retrieved;
    }

    @Ignore
    public Location(String name) {
        this.name = name;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public void addCategory(String c) {
        if (categories.getCategories() != null)
            categories.getCategories().add(c);
    }

    public boolean containsCategory(String category) {
        for (String c : categories.getCategories()) {
            if (c.equalsIgnoreCase(category))
                return true;
        }
        return false;
    }

    public List<String> getCategory() {
        return categories.getCategories();
    }

    public void setCategory(List<String> category) {
        this.categories.setCategories(category);
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

    public String getFirstCategory() {
        if (categories != null) {
            List<String> category = categories.getCategories();
            if (category != null && category.size() > 0)
                return category.get(0);
        }
        return null;

    }

    @Ignore
    public double getAverageAQI() {
        return averageAQI;
    }

    @Ignore
    public void setAverageAQI(double averageAQI) {
        this.averageAQI = averageAQI;
    }
}
