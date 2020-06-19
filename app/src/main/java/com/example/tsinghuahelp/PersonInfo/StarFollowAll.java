package com.example.tsinghuahelp.PersonInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.Follow.FollowUser;
import com.example.tsinghuahelp.Follow.FollowUserAdapter;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.example.tsinghuahelp.mainPage;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StarFollowAll extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private FollowUserAdapter followUserAdapter;
    private SearchResultAdapter proAdapter;
    private List<FollowUser> followUserList = new ArrayList<FollowUser>();
    private List<SearchResult> proList = new ArrayList<SearchResult>();
    int mtype;
    public static boolean change=false;
    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Global.FAIL_CODE:
                    Toast.makeText(StarFollowAll.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case Global.FRESH_PROJ_CODE:
                    Log.e("m_tag","收到我的项目更新");
                    proAdapter.notifyDataSetChanged();
                    break;
                case Global.FRESH_FOLL_CODE:
                    Log.e("m_tag","收到follow更新");
                    followUserAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        if(change){
            fresh();
            change=false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_follow_all);
        textView=findViewById(R.id.textView);
        recyclerView = findViewById(R.id.star_or_follow_recycle);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.backward_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = this.getIntent();
        String type = intent.getStringExtra("type");
        assert type != null;
        if(type.equals("star")){
            //从服务器获取项目
            mtype=0;
            textView.setText("关注项目");
            if(Global.TYPE){textView.setText("我的项目");}
            proAdapter = new SearchResultAdapter(this,proList);
            recyclerView.setAdapter(proAdapter);
        }
        else if(type.equals("follow")){
            //从服务器获取我的关注
            mtype=1;
            textView.setText("我的关注");
            followUserAdapter = new FollowUserAdapter(this,followUserList);
            recyclerView.setAdapter(followUserAdapter);
        }
        else{
            //从服务器获取关注我的人
            mtype=2;
            textView.setText("关注我的");
            followUserAdapter = new FollowUserAdapter(this,followUserList);
            recyclerView.setAdapter(followUserAdapter);
        }
        fresh();

    }

    public void fresh(){
        switch (mtype){
            case 0:
                String url="/api/user/get_star";
                if(Global.TYPE){url="/api/teacher/get_my_project";}
                fresh_pro(url);
                break;
            case 1:
                url="/api/user/get_follower";
                fresh_user(url);
                break;
            case 2:
                url="/api/user/get_followed";
                fresh_user(url);
                break;
        }

    }

    public void fresh_pro(String url){
        proList.clear();
        CommonInterface.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                Message message=new Message();
                message.what=Global.FAIL_CODE;
                message.obj=e.toString();
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
                    for (int i=0;i<jsonArray.size();i++){
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        String o_title = object.getString("title");
                        String o_teacher = object.getString("teacher");
                        String o_department = object.getString("department");
                        String o_description=object.getString("description");
                        int o_id = object.getInteger("id");
                        proList.add(new SearchResult(o_title,o_teacher,
                                o_department, o_description,"project",o_id));
                    }

                    Message message=new Message();
                    message.what=Global.FRESH_PROJ_CODE;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=Global.FAIL_CODE;
                    message.obj="获取项目失败！";
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    public void fresh_user(String url){
        followUserList.clear();
        CommonInterface.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
                Message message=new Message();
                message.what=Global.FAIL_CODE;
                message.obj=e.toString();
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
                    for (int i=0;i<jsonArray.size();i++){
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        String o_username = object.getString("username");
                        Boolean o_type = object.getBoolean("type");
                        int o_id = object.getInteger("id");
                        followUserList.add(new FollowUser(o_type,o_id,o_username));
                    }

                    Message message=new Message();
                    message.what=Global.FRESH_FOLL_CODE;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=Global.FAIL_CODE;
                    message.obj="获取follow用户失败！";
                    mHandler.sendMessage(message);
                }
            }
        });
    }

}
