package com.example.pallavi.shareit;

import java.io.Serializable;

/**
 * Created by pallavi on 2/20/16.
 */
public class Moment implements Serializable{

    private String momentName;
    private String momentType;
    private int momentID;
    private String userId;
    private String friendId;
    private String momentPath;
    private String momentOwnership;
    private String momentLatidude;
    private String momentLongitude;

    public Moment()  {
    }

    public Moment(String momentName, String momentType, int momentID, String userId,String friendId, String momentPath, String momentOwnership, String momentLatidude, String momentLongitude) {
        this.momentName = momentName;
        this.momentType = momentType;
        this.momentID = momentID;
        this.userId = userId;
        this.friendId=friendId;
        this.momentPath = momentPath;
        this.momentOwnership = momentOwnership;
        this.momentLatidude = momentLatidude;
        this.momentLongitude = momentLongitude;
    }

    public String getMomentName() {
        return momentName;
    }

    public void setMomentName(String momentName) {
        this.momentName = momentName;
    }

    public String getMomentType() {
        return momentType;
    }

    public void setMomentType(String momentType) {
        this.momentType = momentType;
    }

    public int getMomentID() {
        return momentID;
    }

    public void setMomentID(int momentID) {
        this.momentID = momentID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMomentPath() {
        return momentPath;
    }

    public void setMomentPath(String momentPath) {
        this.momentPath = momentPath;
    }

    public String getMomentOwnership() {
        return momentOwnership;
    }

    public void setMomentOwnership(String momentOwnership) {
        this.momentOwnership = momentOwnership;
    }

    public String getMomentLatidude() {
        return momentLatidude;
    }

    public void setMomentLatidude(String momentLatidude) {
        this.momentLatidude = momentLatidude;
    }

    public String getMomentLongitude() {
        return momentLongitude;
    }

    public void setMomentLongitude(String momentLongitude) {
        this.momentLongitude = momentLongitude;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
