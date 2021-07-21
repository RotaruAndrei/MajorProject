package com.example.safezone;

public class UserVideosModelClass {

    private String userName;
    private String videoTitle;
    private String videoUrl;

    public UserVideosModelClass() {

    }

    public UserVideosModelClass(String userName, String videoTitle, String videoUrl) {
        this.userName = userName;
        this.videoTitle = videoTitle;
        this.videoUrl = videoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
