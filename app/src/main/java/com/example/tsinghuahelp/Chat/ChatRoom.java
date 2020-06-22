package com.example.tsinghuahelp.Chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.example.tsinghuahelp.Fragment.Fragment4;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.news.Posts;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;
import com.example.tsinghuahelp.utils.SetImageByUrl;
import com.example.tsinghuahelp.utils.WebSocket;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.tsinghuahelp.utils.WebSocket.getSocketClient;


public class ChatRoom extends Activity {
    private static List<MessageChatModel> messageChatModelList =  new ArrayList<>();

    private RecyclerView recyclerView;
    static MessageChatAdapter adapter ;

    private EditText messageET;
    private ImageView sendBtn;
    private TextView username;
    private static String userID;
    private CircularImageView profile_image;

    private String iconUrl;
    @SuppressLint("HandlerLeak")
    public static Handler msgHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Global.FAIL_CODE:
                    Log.d("error","backend not connected");
                    break;
                case  Global.FRESH_HOME_CODE:
                    adapter.notifyDataSetChanged();
                    break;
            }

            if(msg.obj!=null && !msg.obj.toString().equals("连接成功！")){
                messageChatModelList.add( new MessageChatModel(
                        msg.obj.toString(),
                        getTime(),
                        1,
                        userID
                ));
                adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_room);
        Fragment4.change=true;

        messageET = (EditText)findViewById(R.id.messageET);
        sendBtn = (ImageView) findViewById(R.id.sendBtn);
        username = (TextView) findViewById(R.id.chat_username);
        profile_image = (CircularImageView) findViewById(R.id.profile_image);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(ChatRoom.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        manager.setStackFromEnd(true);

        Intent intent = this.getIntent();

        username.setText(intent.getStringExtra("title"));
        userID = intent.getStringExtra("id").toString();

        if(userID.equals("0")){ // system msg
            messageET.setFocusableInTouchMode(false);
            messageET.setFocusable(false);
            sendBtn.setEnabled(false);
        }

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        this.iconUrl="http://47.94.145.111:8080/api/user/getIcon/"+userID;
        SetImageByUrl getImageByUrl = new SetImageByUrl();
        getImageByUrl.setImage(profile_image,iconUrl);

        fresh_page(userID);

        recyclerView.smoothScrollToPosition(messageChatModelList.size());
        adapter = new MessageChatAdapter(messageChatModelList, ChatRoom.this );
        recyclerView.setAdapter(adapter);

        findViewById(R.id.backward_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocket.socketClose();
                finish();
            }
        });
        
        String socketURL = "ws://47.94.145.111:8080/websocket/" +  String.valueOf(Global.CURRENT_ID);
        Log.d("m",socketURL);
        WebSocket.initSocket(socketURL);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageET.getText().toString();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("message", msg);
                    obj.put("to",userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                WebSocket.send(obj);

                SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
                Date date = new Date();// 获取当前时间

                MessageChatModel model = new MessageChatModel(
                        msg,
                        sdf.format(date),
                        0,
                        userID
                );

                messageChatModelList.add(model);
                recyclerView.smoothScrollToPosition(messageChatModelList.size());
                adapter.notifyDataSetChanged();
                messageET.setText("");
            }
        });

}
    private void fresh_page(String userID){
        String url = "/api/chat/get_chat_content/" + String.valueOf(userID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonInterface.sendOkHttpGetRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.d("chatmsg",resStr);
                        try {
                            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resStr);
                            JSONArray data = jsonObject.getJSONArray("data");

                            messageChatModelList.clear();
                            int viewType = 0;
                            for (int i = 0; i < data.size(); i++) {
                                com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) data.get(i);
                                if(object.get("type").toString().equals("sender"))
                                    viewType = 1;
                                else
                                    viewType = 0;
                                messageChatModelList.add( new MessageChatModel(
                                        object.get("content").toString(),
                                        object.get("time").toString(),
                                        viewType,
                                        userID
                                ));

                            }
                            Message message=new Message();
                            message.what= Global.FRESH_HOME_CODE;
                            msgHandler.sendMessage(message);

                        } catch (Exception e) {
                            Log.e("error", e.toString());
                            Message message=new Message();
                            message.what=Global.FAIL_CODE;
                            msgHandler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();

    }
    private static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }
}
