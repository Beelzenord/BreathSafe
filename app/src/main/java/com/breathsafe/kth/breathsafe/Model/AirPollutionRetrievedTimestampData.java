package com.breathsafe.kth.breathsafe.Model;

public class AirPollutionRetrievedTimestampData {
    private long retrieved;
    private static AirPollutionRetrievedTimestampData instance;

    private AirPollutionRetrievedTimestampData() {
        retrieved = 0;
    }

    public static AirPollutionRetrievedTimestampData getInstance() {
        if (instance == null)
            instance = new AirPollutionRetrievedTimestampData();
        return instance;
    }

    public long getRetrieved() {
        return retrieved;
    }

    public void setRetrieved(long retrieved) {
        this.retrieved = retrieved;
    }
}
