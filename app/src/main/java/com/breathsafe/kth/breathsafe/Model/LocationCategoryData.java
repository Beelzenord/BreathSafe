package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class LocationCategoryData {
    private static LocationCategoryData locationCategoryData;
    private List<LocationCategory> list;

    private LocationCategoryData () {
        list = new ArrayList<>();
    }

    public static LocationCategoryData getInstance() {
        if (locationCategoryData == null)
            locationCategoryData = new LocationCategoryData();
        return locationCategoryData;
    }

    public List<LocationCategory> getList() {
        return list;
    }

    public void setList(List<LocationCategory> list) {
        this.list = list;
    }
}
