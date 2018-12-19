package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class AirPollusionData {
    private static AirPollusionData airPollusionData;
    private List<AirPollusion> list;

    private AirPollusionData() {
        list = new ArrayList<>();
    }

    public static AirPollusionData getInstance() {
        if (airPollusionData == null)
            airPollusionData = new AirPollusionData();
        return airPollusionData;
    }

    public List<AirPollusion> getList() {
        return list;
    }

    public void setList(List<AirPollusion> list) {
        this.list = list;
    }
}
