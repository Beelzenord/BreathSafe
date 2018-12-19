package com.breathsafe.kth.breathsafe.Model;

public class LocationCategory {
    String singularName;
    String id;
    long timeCreated;
    long timeUpdated;
    int groupId;
    String groupName;
    long retrieved;

    public LocationCategory(String singularName, String id, long timeCreated, long timeUpdated, int groupId, String groupName, long retrieved) {
        this.singularName = singularName;
        this.id = id;
        this.timeCreated = timeCreated;
        this.timeUpdated = timeUpdated;
        this.groupId = groupId;
        this.groupName = groupName;
        this.retrieved = retrieved;
    }

    public String getSingularName() {
        return singularName;
    }

    public void setSingularName(String singularName) {
        this.singularName = singularName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getRetrieved() {
        return retrieved;
    }

    public void setRetrieved(long retrieved) {
        this.retrieved = retrieved;
    }
}
