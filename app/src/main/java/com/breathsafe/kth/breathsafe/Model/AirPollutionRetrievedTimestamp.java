package com.breathsafe.kth.breathsafe.Model;

public class AirPollutionRetrievedTimestamp {
    private int id;
    private long retrieved;

    public AirPollutionRetrievedTimestamp(long retrieved) {
        this.id = 0;
        this.retrieved = retrieved;
    }

    public long getRetrieved() {
        return retrieved;
    }

    public void setRetrieved(long retrieved) {
        this.retrieved = retrieved;
    }
}
