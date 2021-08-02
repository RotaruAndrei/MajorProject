package com.example.safezone;


public class UserModelClass {

    private String forename;
    private String surname;
    private String email;
    private String phoneNumber;
    private String userIcon = "";


    public UserModelClass() {

    }

    public UserModelClass(String userIcon) {
        this.userIcon = userIcon;
    }

    public UserModelClass(String phoneNumber, String imgUrl) {
        this.phoneNumber = phoneNumber;
        this.userIcon = imgUrl;
    }

    public UserModelClass(String forename, String surname, String email, String phoneNumber) {
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UserModelClass(String forename, String surname, String email, String phoneNumber, String userIcon) {
        this.forename = forename;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userIcon = userIcon;

    }


    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
