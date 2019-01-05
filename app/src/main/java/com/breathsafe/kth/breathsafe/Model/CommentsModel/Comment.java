package com.breathsafe.kth.breathsafe.Model.CommentsModel;

public class Comment {
    private String locationID;
    private String locationName;
    private String userID;
    private String username;
    private String title;
    private String text;
    private long timestamp;

    public Comment() {
    }

    public Comment(String locationID, String locationName, String userID, String username, String title, String text, long timestamp) {
        this.locationID = locationID;
        this.locationName = locationName;
        this.userID = userID;
        this.username = username;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
