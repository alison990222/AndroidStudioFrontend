package com.example.tsinghuahelp.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.MainActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.mainPage;
import com.example.tsinghuahelp.news.PostAdapter;
import com.example.tsinghuahelp.news.Posts;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.MyDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class Fragment1 extends Fragment  {

    private View mView;

    private RecyclerView recyclerView;
    private PostAdapter adapter;

    private List<Posts> postsList;
    private int listSize;
//    public static Handler msgHandler;

    public Fragment1() {

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Log.d("error","backend not connected");
                    break;
                case 1:
                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        postsList = new ArrayList<>();

        listSize = 0;
        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_main1, container, false);
            // 绑定视图
        ButterKnife.bind(this, mView);

        recyclerView = mView.findViewById(R.id.recyclerView4);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fresh_page();

        adapter = new PostAdapter(getContext(),postsList);

        recyclerView.setAdapter(adapter);

        return mView;
    }

    private void fresh_page(){
        postsList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonInterface.sendOkHttpGetRequest("/api/user/projects", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.e("response", resStr);
                        try {
                            JSONObject jsonObject = JSONObject.parseObject(resStr);
                            JSONArray data = jsonObject.getJSONArray("data");

                            for (int i = 0; i < data.size(); i++) {
                                JSONObject object = (JSONObject) data.get(i);
                                postsList.add(
                                        new Posts(
                                                object.getString("teacher"),
                                                object.getString("project_title"),
                                                object.getString("researchDirection"),
                                                object.getString("department"),
                                                "",
                                                object.getString("requirement"),
                                                object.getInteger("project_id"),
                                                object.get("teacher_id").toString()));
                            }
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);

                        } catch (Exception e) {
                            Log.e("error", e.toString());
                            Message message = new Message();
                            message.what = 0;
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();


    }


}
