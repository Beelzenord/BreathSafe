package com.breathsafe.kth.breathsafe.Model;

import java.util.ArrayList;
import java.util.List;

public class AirPollutionData {
    private static AirPollutionData airPollusionData;
    private List<AirPollution> list;

    private AirPollutionData() {
        list = new ArrayList<>();
    }

    public static AirPollutionData getInstance() {
        if (airPollusionData == null)
            airPollusionData = new AirPollutionData();
        return airPollusionData;
    }

    public List<AirPollution> getList() {
        return list;
    }

    public void setList(List<AirPollution> list) {
        this.list = list;
    }
}
