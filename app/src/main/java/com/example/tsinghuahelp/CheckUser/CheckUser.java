package com.example.tsinghuahelp.CheckUser;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CheckUser extends AppCompatActivity {

    ListView listview;
    private List<String> userList = new ArrayList<String>();

    private int ID;
    private ArrayAdapter adapter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
//                    Toast.makeText(StarFollowAll.this,"后端信息获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_applicants);

        Intent intent = this.getIntent();
        ID = intent.getIntExtra("id",-1);
        fresh_page(ID);


        listview = (ListView) findViewById(R.id.listview);

        adapter = new ArrayAdapter<>(this , R.layout.user_layout ,userList);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CheckUser.this, "點選第 " + (i + 1) + " 個 \n內容：" + userList.get(i), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fresh_page (int id) {
        String url = "/api/teacher/get_signin_student/" + String.valueOf(id);

        CommonInterface.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.d("data",resStr);
                try {
                    JSONObject jsonObject = JSONObject.parseObject(resStr);
                    JSONArray data = jsonObject.getJSONArray("data");

                    for (int i = 0; i < data.size(); i++) {
                        JSONObject object = (JSONObject) data.get(i);
                        userList.add(object.get("name").toString());
                    }
                    // {"grade":"","id":1,"name":"汪","real_name":"","school":"","type":false}
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
}
