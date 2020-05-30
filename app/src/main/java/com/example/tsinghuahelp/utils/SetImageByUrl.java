package com.example.tsinghuahelp.utils;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 根据图片url路径获取图片
 *
 * @author LeoLeoHan
 *
 */
public class SetImageByUrl {

    private PicHandler pic_hdl;
    private com.mikhaellopez.circularimageview.CircularImageView imgView;
    private String url;


    /**
     * 通过图片url路径获取图片并显示到对应控件上
     *
     * @param imgView
     * @param url
     */
    public void setImage(com.mikhaellopez.circularimageview.CircularImageView imgView, String url) {
        this.url = url;
        this.imgView = imgView;
        pic_hdl = new PicHandler();
        Thread t = new LoadPicThread();
        t.start();
    }


    class LoadPicThread extends Thread {
        @Override
        public void run() {
            Bitmap img = getUrlImage(url);
            System.out.println(img + "---");
            Message msg = pic_hdl.obtainMessage();
            msg.what = 0;
            msg.obj = img;
            pic_hdl.sendMessage(msg);
        }
    }

    class PicHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bitmap myimg = (Bitmap) msg.obj;
            imgView.setImageBitmap(myimg);
        }

    }

    public Bitmap getUrlImage(String url) {
        Bitmap img = null;
        try {
            URL picurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) picurl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("charset","UTF-8");
            conn.connect();
            InputStream is = conn.getInputStream();
            if (conn.getResponseCode()==200){
                InputStream in = conn.getInputStream();
                img = BitmapFactory.decodeStream(in);
            }
            else{
                img=null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
}
