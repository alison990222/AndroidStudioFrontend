package com.example.tsinghuahelp.Chat;

public class ChatList {
    private String username;
    private String userID;
    private String latestChat;
    private String date;
    private String urlProfile;

    public ChatList(){}

    public ChatList(String username, String description, String date, String urlProfile, String userID) {
        this.username = username;
        this.latestChat = description;
        this.date = date;
        this.urlProfile = urlProfile;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return latestChat;
    }

    public String getDate() {
        return date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public String getUserID() { return userID; }
}
