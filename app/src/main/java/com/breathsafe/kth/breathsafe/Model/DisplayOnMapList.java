package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class DisplayOnMapList {
    private static DisplayOnMapList instance;
    private List<Location> list;

    private DisplayOnMapList() {
        list = new ArrayList<>();
    }

    public static DisplayOnMapList getInstance() {
        if (instance == null)
            instance = new DisplayOnMapList();
        return instance;
    }

    public void add(Location location) {
        if (!list.contains(location))
            list.add(location);
    }

    public List<Location> getList() {
        return list;
    }

    public void setList(List<Location> list) {
        this.list = list;
    }

    public void clearList() {
        for (Location l : list)
            l.setOnMap(false);
        list = new ArrayList<>();
    }

    public void setOnMapFalse() {
        for (Location l : list)
            l.setOnMap(false);
    }
}
