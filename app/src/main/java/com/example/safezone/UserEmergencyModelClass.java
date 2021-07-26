package com.example.safezone;

public class UserEmergencyModelClass {
    private String userName;
    private String userPhoneNumber;
    private double userLatitude;
    private double userLongitude;

    public UserEmergencyModelClass() {

    }

    public UserEmergencyModelClass(String userName, String userPhoneNumber, double userLatitude, double userLongitude) {
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }
}
