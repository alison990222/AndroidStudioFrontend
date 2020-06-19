package com.example.tsinghuahelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.news.Posts;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView topic;
    private TextView teacher;
    private TextView time;
    private TextView department;
    private TextView require;
    private TextView researchField;
    private TextView description;
    private Button btnStar;
    private Button btnApply;
    private Button btnDelete;
    private Button btnCheck;
    private Button btnEdit;


    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
//                    Toast.makeText(StarFollowAll.this,"后端信息获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Log.e("m_tag","收到我的项目更新");
//                    proAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Log.e("m_tag","收到follow更新");
//                    followUserAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();

        btnStar = (Button) findViewById(R.id.btn_star);
        btnStar.setOnClickListener(this);
        btnApply = (Button) findViewById(R.id.btn_apply);
        btnApply.setOnClickListener(this);

        btnDelete = (Button)findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        btnCheck = (Button)findViewById(R.id.btn_check);
        btnCheck.setOnClickListener(this);
        btnEdit = (Button)findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);

        // if it's the owner of the post
        if (Global.TYPE == false) {  // student

        } else {

        }

        topic = (TextView) findViewById(R.id.projectName);
        department = (TextView) findViewById(R.id.projectDept);
        description = (TextView) findViewById(R.id.projectDescript);
        researchField = (TextView) findViewById(R.id.projectField);
        require = (TextView) findViewById(R.id.projectRequire);
        time = (TextView) findViewById(R.id.projectTime);
        teacher = (TextView) findViewById(R.id.projectTeacher);

        topic.setText(intent.getStringExtra("title"));

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);


        fresh_page(intent.getIntExtra("id",-1));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_star:
                starProject();
//                change=true;
//                Intent intent=new Intent(getContext(),EditInfoActivity.class);
//                intent.putExtra("icon_url",icon_url);
//                intent.putExtra("id",user_id);
//                intent.putExtra("name",name);
//                intent.putExtra("real_name",real_name);
//                intent.putExtra("school",school);
//                intent.putExtra("department",department);
//                intent.putExtra("grade",grade);
//                intent.putExtra("signature",signature);
//                intent.putExtra("verify",verify);
//                intent.putExtra("person_info",person_info);
//                startActivity(intent);
                break;
            case R.id.btn_apply:
                applyProject();
                break;
            case R.id.btn_delete:
                deleteProject();
                break;
            case R.id.btn_check:
                checkProject();
                break;
            case R.id.btn_edit:
                editProject();
                break;
        }
    }

    private void starProject(){

        if(btnApply.getText() == "收藏")
            btnApply.setText("取消收藏");
        else
            btnApply.setText("收藏");
    }

    private void applyProject(){

        if(btnApply.getText() == "报名")
            btnApply.setText("取消报名");
        else
            btnApply.setText("报名");
    }

    private void checkProject(){


    }

    private void deleteProject(){


    }

    private void editProject(){


    }

    private void fresh_page (int id) {
        String url = "/api/user/project_info/" + String.valueOf(id);
        CommonInterface.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                try {
                    JSONObject jsonObject = JSONObject.parseObject(resStr);

//                    jsonObject.get("teacher");
//                    jsonObject.get("research_direction");
//                    jsonObject.get("description");
//                    jsonObject.get("department");
//                    jsonObject.get("requirement");
//
//                    topic = (TextView) findViewById(R.id.projectName);
//                    department = (TextView) findViewById(R.id.projectDept);
//                    description = (TextView) findViewById(R.id.projectDescript);
//                    researchField = (TextView) findViewById(R.id.projectField);
//                    require = (TextView) findViewById(R.id.projectRequire);
//                    time = (TextView) findViewById(R.id.projectTime);
//                    teacher = (TextView) findViewById(R.id.projectTeacher);
//
//                    teacher.setText("导师");
//                    topic.setText(intent.getStringExtra("title"));

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
