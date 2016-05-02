package com.example.pallavi.shareit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Roopashree on 2/17/2016.
 */
public class User implements Serializable{

    private String userID;
    private String firstName;
    private String lastName;
    private String userEmail;
    private String password;
    private String status;
    private String dp;
    HashMap<String,String> friendsList = new HashMap<String,String>();
    HashMap<String,String> momentsList = new HashMap<String,String>();

    public User() {
    }

    public User(String userID, String firstName, String lastName, String userEmail, String password, String status, String dp,HashMap<String,String> friendsList, HashMap<String,String> momentsList) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.password = password;
        this.status = status;
        this.dp = dp;
        this.friendsList = friendsList;
        this.momentsList = momentsList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HashMap<String,String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(HashMap<String,String> friendsList) {
        this.friendsList = friendsList;
    }

    public HashMap<String,String> getMomentsList() {
        return momentsList;
    }

    public void setMomentsList(HashMap<String,String> momentsList) {
        this.momentsList = momentsList;
    }
}
