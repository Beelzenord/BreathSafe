package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class LocationData {
    private static LocationData locationData;
    private List<Location> list;

    private LocationData() {
        list = new ArrayList<>();
    }

    public static LocationData getInstance() {
        if (locationData == null)
            locationData = new LocationData();
        return locationData;
    }

    public List<Location> getList() {
        return list;
    }

    public void setList(List<Location> list) {
        this.list = list;
    }
}
