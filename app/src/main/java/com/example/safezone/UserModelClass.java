package com.example.safezone;

import android.widget.ImageView;

public class UserModelClass {
    private String forname;
    private String surname;
    private String email;
    private String phoneNumber;
    private ImageView userIcon;

    public UserModelClass() {

    }

    public UserModelClass(String forname, String surname, String email) {
        this.forname = forname;
        this.surname = surname;
        this.email = email;
    }

    public UserModelClass(String forname, String surname, String email, String phoneNumber) {
        this.forname = forname;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public UserModelClass(String forname, String surname, String email, String phoneNumber, ImageView userIcon) {
        this.forname = forname;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userIcon = userIcon;
    }

    public ImageView getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(ImageView userIcon) {
        this.userIcon = userIcon;
    }

    public String getForname() {
        return forname;
    }

    public void setForname(String forname) {
        this.forname = forname;
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
