package com.example.tsinghuahelp.Follow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;

import com.example.tsinghuahelp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FollowUser {
    private String iconUrl;
    private String followUsername;
    private boolean type;
    private int id;

    public FollowUser(boolean type,int id,String followUsername) {
        this.iconUrl="http://47.94.145.111:8080/api/user/getIcon/"+id;
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
