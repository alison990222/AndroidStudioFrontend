package com.example.tsinghuahelp.Follow;

public class FollowUser {
    private String iconUrl;
    private String followUsername;
    private boolean type;
    private int id;

    public FollowUser(String iconUrl,boolean type,int id,String followUsername) {
        this.iconUrl=iconUrl;
        this.type = type;
        this.id = id;
        this.followUsername=followUsername;
    }

    public int getId() {
        return id;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean getType() {
        return type;
    }

    public String getFollowUsername() {
        return followUsername;
    }
}
