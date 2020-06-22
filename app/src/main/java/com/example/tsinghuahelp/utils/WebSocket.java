package com.example.tsinghuahelp.utils;

import android.os.Message;
import android.util.Log;

import com.example.tsinghuahelp.Chat.ChatRoom;
import com.example.tsinghuahelp.RegisterActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class WebSocket {

    private static int RE_TIME = 5;  // 发起重连的时间

    private static WebSocketClient socketClient = null;
    
    private String SOCKET_URL;

    public static WebSocketClient getSocketClient() {
        return socketClient;
    }

    public static void initSocket(String SOCKET_URL) {
        try {
            URI u = new URI(SOCKET_URL);
            socketClient = new WebSocketClient(u) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.d("Socket", "成功连接！");
                }

                @Override
                public void onMessage(String message) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = message;
                    ChatRoom.msgHandler.sendMessage(msg);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    if (code != 1000)   // 1000为正常关闭，不是意外关闭
                        WebSocket.reconnect(SOCKET_URL);
                }

                @Override
                public void onError(Exception ex) {
                    socketClient = null;
                }
            };
            socketClient.connect();
        } catch (Exception e) {

        }
    }

    public static void socketClose() {
        Log.d("close","i'm closing");
        socketClient.close();
    }


    public static void reconnect(String SOCKET_URL) {
        new Thread(() -> {
            try {
                while (socketClient == null || !socketClient.isOpen()) {
                    initSocket(SOCKET_URL);
                    Thread.sleep(RE_TIME * 1000);
                    Log.e("socket", "服务器连接错误！" + RE_TIME + "秒后重连。");
                }
            } catch (Exception e) {

            }
        }).start();
    }

    public static boolean send(JSONObject sendmsg) {
        if (socketClient.isOpen()) {
            Log.e("send msg", sendmsg.toString());
            socketClient.send(sendmsg.toString());
            return true;
        } else {
            return false;
        }
    }
}
