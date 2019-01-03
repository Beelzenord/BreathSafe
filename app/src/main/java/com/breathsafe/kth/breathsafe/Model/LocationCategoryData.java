package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class LocationCategoryData {
    private static LocationCategoryData locationCategoryData;
    private List<LocationCategory> list;

    private LocationCategoryData () {
        list = new ArrayList<>();
    }

    public synchronized static LocationCategoryData getInstance() {
        if (locationCategoryData == null)
            locationCategoryData = new LocationCategoryData();
        return locationCategoryData;
    }

    public synchronized List<LocationCategory> getList() {
        return list;
    }

    public synchronized void setList(List<LocationCategory> list) {
        this.list = list;
    }
}
