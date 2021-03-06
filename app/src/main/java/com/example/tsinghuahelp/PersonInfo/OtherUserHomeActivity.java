package com.example.tsinghuahelp.PersonInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.Adapter.InfoAdapter;
import com.example.tsinghuahelp.Chat.ChatRoom;
import com.example.tsinghuahelp.Fragment.Fragment4;
import com.example.tsinghuahelp.Fragment.Fragment5;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.example.tsinghuahelp.mainPage;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;
import com.example.tsinghuahelp.utils.MyDialog;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OtherUserHomeActivity extends Activity implements View.OnClickListener{
    com.mikhaellopez.circularimageview.CircularImageView icon;
    TextView other_username;
    TextView is_verify;
    Button btn_verify;
    Button btn_track;
    Button btn_chat;
    TextView signature;
    TabLayout tabLayout;
    RecyclerView myRecyclerView;
    InfoAdapter infoAdapter;
    SearchResultAdapter pAdapter;
    private List<String> infoList;
    private List<SearchResult> pList;
    Boolean type=false;
    Integer user_id;
    String icon_url;
    String name;
    String real_name;
    String school;
    String department;
    String grade;
    String signature_str;
    String person_info;
    boolean verify;
    Bitmap bitmap;
    boolean relation;



    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Global.FAIL_CODE:
                    Toast.makeText(OtherUserHomeActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case Global.FRESH_HOME_CODE:
                    Log.e("m_tag","收到信息更新页");
                    fresh_prolist();
                    if(verify){is_verify.setText("已验证");}
                    else{is_verify.setText("未验证");}
                    if(relation) {btn_track.setText("取消追踪");}
                    other_username.setText(name);
                    signature.setText(signature_str);
                    infoList.clear();
                    infoList.add(person_info);
                    infoAdapter.notifyDataSetChanged();
                    if(type){
                        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("我的项目");
                    }
                    break;
                case Global.FRESH_ICON_CODE:
                    Log.e("m_tag","收到信息更新图片");
                    icon.setImageBitmap(bitmap);
                    break;
                case Global.FRESH_PROJ_CODE:
                    Log.e("m_tag","收到我的项目更新");
                    pAdapter.notifyDataSetChanged();
                    break;
                case Global.FRESH_BUTT_CODE:
                    Log.e("m_tag","按钮更新");
                    Fragment5.change=true;
                    StarFollowAll.change=true;
                    if(relation){relation=false;btn_track.setText("追踪");}
                    else{
                        relation=true;
                        btn_track.setText("取消追踪");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal5);

        tabLayout = findViewById(R.id.tab_layout);
        findViewById(R.id.pro_num).setVisibility(View.INVISIBLE);
        findViewById(R.id.follower_num).setVisibility(View.INVISIBLE);
        findViewById(R.id.follow_num).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_info_btn).setVisibility(View.GONE);
        findViewById(R.id.toolbar).setVisibility(View.GONE);
        findViewById(R.id.title).setVisibility(View.VISIBLE);
        other_username=findViewById(R.id.info_username);
        is_verify=findViewById(R.id.verify_info);
        btn_verify=findViewById(R.id.verify_btn);
        signature=findViewById(R.id.info_signature);
        btn_track=findViewById(R.id.follow_btn);
        btn_chat=findViewById(R.id.chat_btn);
        btn_track.setVisibility(View.VISIBLE);
        btn_chat.setVisibility(View.VISIBLE);
        icon=findViewById(R.id.info_pic);

        btn_verify.setOnClickListener(this);
        btn_track.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        findViewById(R.id.backward_btn).setOnClickListener(this);


        myRecyclerView = findViewById(R.id.info_recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String plan_or_pro;

        Intent intent=getIntent();
        user_id=intent.getIntExtra("id",0);

        plan_or_pro="我的发文";

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.layout_divider_vertical));

        tabLayout.addTab(tabLayout.newTab().setText("关于我"));
        tabLayout.addTab(tabLayout.newTab().setText(plan_or_pro));

        infoList = new ArrayList<>();

        pList = new ArrayList<>();

        pAdapter = new SearchResultAdapter(this,pList);
        infoAdapter = new InfoAdapter(this,infoList);

        myRecyclerView.setAdapter(infoAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        myRecyclerView.setAdapter(infoAdapter);
                        break;
                    case 1:
                        myRecyclerView.setAdapter(pAdapter);
                        break;
                    default:
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        fresh_page();


    }


    private void fresh_page(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonInterface.sendOkHttpGetRequest("/api/user/user_info/" + user_id, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                        Message message = new Message();
                        message.what = Global.FAIL_CODE;
                        message.obj = e.toString();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            // 解析json，然后进行自己的内部逻辑处理
                            JSONObject jsonObject = JSONObject.parseObject(resStr);
                            JSONObject data = jsonObject.getJSONObject("data");
                            icon_url = data.getString("icon_url");
                            name = data.getString("username");
                            real_name = data.getString("real_name");
                            school = data.getString("school");
                            department = data.getString("department");
                            grade = data.getString("grade");
                            signature_str = data.getString("signature");
                            person_info = data.getString("personal_info");
                            verify = data.getBoolean("verification");
                            type = data.getBoolean("type");
                            relation = data.getBoolean("relation");

                            Message message = new Message();
                            message.what = Global.FRESH_HOME_CODE;
                            mHandler.sendMessage(message);
                            fresh_icon();
                        } catch (Exception e) {
                            Message message = new Message();
                            message.what = Global.FAIL_CODE;
                            message.obj = "页面信息获取失败！";
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();
    }

    private void fresh_icon(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(icon_url);
                    URL url = new URL(icon_url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("charset","UTF-8");
                    if (connection.getResponseCode()==200){
                        InputStream in = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(in);
                        Message message=new Message();
                        message.what=Global.FRESH_ICON_CODE;
                        mHandler.sendMessage(message);
                    }
                    else{
                        Message message=new Message();
                        message.what=Global.FAIL_CODE;
                        message.obj="获取头像失败！";
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    Log.e("error", e.toString());
                    Message message = new Message();
                    message.what = Global.FAIL_CODE;
                    message.obj = e.toString();
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private void fresh_prolist(){
        pList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "/api/user/get_plan_or_pro/" + user_id;
                CommonInterface.sendOkHttpGetRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                        Message message = new Message();
                        message.what = Global.FAIL_CODE;
                        message.obj = e.toString();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            // 解析json，然后进行自己的内部逻辑处理
                            JSONObject jsonObject = JSONObject.parseObject(resStr);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                String o_title = object.getString("title");
                                String o_name = object.getString("name");
                                String o_department = object.getString("department");
                                String o_description = object.getString("description");
                                String o_type = object.getString("type");
                                int o_id = object.getInteger("id");
                                pList.add(new SearchResult(o_title, o_name,
                                        o_department, o_description, o_type, o_id));
                            }

                            Message message = new Message();
                            message.what = Global.FRESH_PROJ_CODE;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            Message message = new Message();
                            message.what = Global.FAIL_CODE;
                            message.obj = "项目计划信息获取失败！";
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.follow_btn:
                goFollow();
                break;
            case R.id.verify_btn:
                showVerifyInfo();
                break;
            case R.id.chat_btn:
                if(user_id==Global.CURRENT_ID){Toast.makeText(this,"不可以私信自己哦！",Toast.LENGTH_SHORT).show();return;}
                Fragment4.change=true;
                Intent chatIntent = new Intent(this, ChatRoom.class);
                chatIntent.putExtra("title",name);
                chatIntent.putExtra("id",user_id.toString());
                startActivity(chatIntent);
                break;
            case R.id.backward_btn:
                finish();
                break;
        }
    }


    public void showVerifyInfo() {
        String message="姓名："+real_name+"\n学校："+school+"\n院系："+department+"\n年级："+grade;
        final MyDialog dialog = new MyDialog(this);
        dialog.setMessage(message)
                .setTitle("验证信息")
                .setSingle(true).setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
            }
        }).show();
    }


    public void goFollow(){
        if(user_id==Global.CURRENT_ID){Toast.makeText(this,"不可以追踪自己哦！",Toast.LENGTH_SHORT).show();return;}
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> h = new HashMap<>();
                h.put("user_id", user_id.toString());
                String url = "/api/user/go_follow";
                if (relation) {
                    url = "/api/user/cancel_follow";
                }
                CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                        Message message = new Message();
                        message.what = Global.FAIL_CODE;
                        message.obj = e.toString();
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            // 解析json，然后进行自己的内部逻辑处理
                            JSONObject jsonObject = JSONObject.parseObject(resStr);
                            String resp = jsonObject.getString("response");
                            if (!resp.equals("valid")) {
                                throw new Exception();
                            } else {
                                Message message = new Message();
                                message.what = Global.FRESH_BUTT_CODE;
                                mHandler.sendMessage(message);
                            }

                        } catch (Exception e) {
                            System.out.println(e);
                            Message message = new Message();
                            message.what = Global.FAIL_CODE;
                            message.obj = "追踪失败！";
                            mHandler.sendMessage(message);
                        }
                    }
                }, h);
            }
        }).start();
    }

}
