package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class LocationData {
    private static LocationData locationData;
    private List<Location> list;

    private LocationData() {
        list = new ArrayList<>();
    }

    public synchronized static LocationData getInstance() {
        if (locationData == null)
            locationData = new LocationData();
        return locationData;
    }

    public synchronized List<Location> getList() {
        return list;
    }

    public synchronized void setList(List<Location> list) {
        this.list = list;
    }
}
