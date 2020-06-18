package com.example.tsinghuahelp.Fragment;

import android.Manifest;
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


public class Fragment1 extends Fragment {

    private View mView;

    private RecyclerView recyclerView;
    private PostAdapter adapter;

    private List<Posts> postsList;
    public static Handler msgHandler;

    public Fragment1() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        postsList = new ArrayList<>();

        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        CommonInterface.sendOkHttpGetRequest("/api/user/projects", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                try {
                    JSONObject jsonObject = JSONObject.parseObject(resStr);
                    JSONArray data = jsonObject.getJSONArray("data");
                    Log.d("in res",resStr);

                    for (int i = 0; i < data.size(); i++) {
                        JSONObject object = (JSONObject) data.get(i);
                        postsList.add(
                                new Posts(
                                        object.getString("teacher"),
                                        object.getString("project_title"),
                                        "移动应用",
                                        object.getString("department"),
                                        "60000",
                                        object.getString("requirement")));
                    }
                    Log.d("in res22",postsList.toString());

                } catch (Exception e) {
                    Log.e("error", e.toString());
                }
            }
        });
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

        try {
            Thread.sleep(500); //1000為1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        adapter = new PostAdapter(getContext(),postsList);

        recyclerView.setAdapter(adapter);

        fresh_page();
        Log.d("oncvieeeew","bbb");
        Log.d("oncvi",postsList.toString());

        return mView;
    }



    public void onStart() {

        super.onStart();
    }

    private void fresh_page(){

    }
}
