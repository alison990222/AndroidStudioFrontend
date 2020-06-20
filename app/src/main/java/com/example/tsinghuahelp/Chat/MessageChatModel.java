package com.example.tsinghuahelp.Chat;

import com.mikhaellopez.circularimageview.CircularImageView;

public class MessageChatModel {
    private String text;
    private String time;
    private int viewType;
    private String iconUrl;

    public MessageChatModel(String text, String time, int viewType,String userID) {
        this.text = text;
        this.time = time;
        this.viewType = viewType;
        this.iconUrl="http://47.94.145.111:8080/api/user/getIcon/"+userID;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public int getViewType() {
        return viewType;
    }
    public String getIconUrl() {
        return iconUrl;
    }
}