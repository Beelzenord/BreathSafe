package com.breathsafe.kth.breathsafe.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "LocationCategory")
public class LocationCategory implements Parcelable{

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    String id;

    @ColumnInfo(name="singularName")
    String singularName;
    @ColumnInfo(name = "timeCreated")
    long timeCreated;
    @ColumnInfo(name = "timeUpdated")
    long timeUpdated;
    @ColumnInfo(name = "groupId")
    int groupId;
    @ColumnInfo(name = "groupName")
    String groupName;
    @ColumnInfo(name = "retrieved")
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


    protected LocationCategory(Parcel in) {
        id = in.readString();
        singularName = in.readString();
        timeCreated = in.readLong();
        timeUpdated = in.readLong();
        groupId = in.readInt();
        groupName = in.readString();
        retrieved = in.readLong();
    }

    public static final Creator<LocationCategory> CREATOR = new Creator<LocationCategory>() {
        @Override
        public LocationCategory createFromParcel(Parcel in) {
            return new LocationCategory(in);
        }

        @Override
        public LocationCategory[] newArray(int size) {
            return new LocationCategory[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(singularName);
        parcel.writeLong(timeCreated);
        parcel.writeLong(timeUpdated);
        parcel.writeInt(groupId);
        parcel.writeString(groupName);
        parcel.writeLong(retrieved);
    }
}
