package com.example.pallavi.shareit;

/**
 * Created by pallavi on 3/2/16.
 */
public class FriendDetails {

    private String friendID;
    private String friendFirstName;
    private String friendLastName;
    private String friendEmail;
    private String friendStatus;
    private String friendDp;
    private boolean isSelected=false;

    public FriendDetails(String friendID, String friendFirstName, String friendLastName, String friendEmail, String friendStatus, String friendDp,boolean isSelected) {
        this.friendID = friendID;
        this.friendFirstName = friendFirstName;
        this.friendLastName = friendLastName;
        this.friendEmail = friendEmail;
        this.friendStatus = friendStatus;
        this.friendDp = friendDp;
        this.isSelected= isSelected;
    }

    public FriendDetails() {
    }

    public String getFriendID() {
        return friendID;
    }

    public void setFriendID(String friendID) {
        this.friendID = friendID;
    }

    public String getFriendFirstName() {
        return friendFirstName;
    }

    public void setFriendFirstName(String friendFirstName) {
        this.friendFirstName = friendFirstName;
    }

    public String getFriendLastName() {
        return friendLastName;
    }

    public void setFriendLastName(String friendLastName) {
        this.friendLastName = friendLastName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String getFriendDp() {
        return friendDp;
    }

    public void setFriendDp(String friendDp) {
        this.friendDp = friendDp;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
