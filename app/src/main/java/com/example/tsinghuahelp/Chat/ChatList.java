package com.example.tsinghuahelp.Chat;

public class ChatList {
    private String username;
    private String description;
    private String date;
    private String urlProfile;

    public ChatList(){}

    public ChatList(String username, String description, String date, String urlProfile) {
        this.username = username;
        this.description = description;
        this.date = date;
        this.urlProfile = urlProfile;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getUrlProfile() {
        return urlProfile;
    }
}
