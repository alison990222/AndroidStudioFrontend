package com.example.tsinghuahelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.CheckUser.CheckUser;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailActivityTeacher extends Activity implements View.OnClickListener{

    private TextView topic;
    private TextView teacher;
    private TextView time;
    private TextView department;
    private TextView require;
    private EditText researchField;
    private EditText description;
    private Button btnEdit;
    private Button btnCheck;
    private Button btnDelete;
    private Button btnStar;
    private Button btnApply;

    Boolean isEdit=false;
    private CheckBox undergraduate,master, phd;
    private String oldResearchFieldname;
    private String oldDescription;

    private String tchName;
    private String deptName;
    private String desctiptName;
    private String researchName;
    private String reqName;
    private int ID;
    private String topicName;
    private String isRegistered;
    private String isStarred;



    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
//                    Toast.makeText(StarFollowAll.this,"后端信息获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    for (String retval: reqName.split(" ")){
                        if(retval.equals("博士生")){
                            phd.setChecked(true);
                        }
                        if(retval.equals("硕士生")){
                            master.setChecked(true);
                        }
                        if(retval.equals("本科生")){
                            undergraduate.setChecked(true);
                        }
                    }

                    teacher.setText("导师："+ tchName);
                    department.setText("院系："+ deptName);
                    description.setText(desctiptName);
                    researchField.setText(researchName);

                    if(isStarred.equals("true"))
                        btnStar.setText("取消收藏");
                    if(isRegistered.equals("true"))
                        btnApply.setText("取消报名");

                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_teacher);

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
            btnDelete.setVisibility(View.INVISIBLE);
            btnCheck.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
        } else {
            btnApply.setVisibility(View.INVISIBLE);
            btnStar.setVisibility(View.INVISIBLE);
        }

        topic = (TextView)findViewById(R.id.projectName);
        department = (TextView)findViewById(R.id.projectDept);
        description = (EditText)findViewById(R.id.projectDescript);
        researchField = (EditText)findViewById(R.id.projectField);
        require = (TextView)findViewById(R.id.projectRequire);
        time = (TextView)findViewById(R.id.projectTime);
        teacher = (TextView)findViewById(R.id.projectTeacher);
        undergraduate = (CheckBox)findViewById(R.id.checkBox_undergraduate);
        master =(CheckBox)findViewById(R.id.checkBox_master);
        phd = (CheckBox)findViewById(R.id.checkBox_phd);
        undergraduate.setClickable(false);
        master.setClickable(false);
        phd.setClickable(false);


        oldResearchFieldname = researchField.getText().toString();
        oldDescription = description.getText().toString();

        topicName = intent.getStringExtra("title");
        topic.setText(topicName);
        ID = intent.getIntExtra("id",-1);
        fresh_page(ID);

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_star:
                starProject();

//                Intent intent=new Intent(getContext(),EditInfoActivity.class);
//                intent.putExtra("icon_url",icon_url);
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

        if(btnStar.getText().equals("收藏")){
            String url = "/api/student/upload_star/";

            HashMap<String,String> h = new HashMap<>();
            h.put("id", String.valueOf(ID));

            CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.d("msg",resStr);
                    try {
                        Log.d("SUCCESS", "success收藏");
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
            },h);
            isStarred= "true";
            btnStar.setText("取消收藏");
        }
        else{
            String url = "/api/student/cancel_star/";

            HashMap<String,String> h = new HashMap<>();
            h.put("id", String.valueOf(ID));

            CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.d("msg",resStr);
                    try {
                        Log.d("SUCCESS", "success取消收藏");
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
            },h);
            isStarred= "false";
            btnStar.setText("收藏");
        }

    }

    private void applyProject(){

        if(btnApply.getText().equals("报名") ){
            String url = "/api/student/sign_in/";

            HashMap<String,String> h = new HashMap<>();
            h.put("project_id", String.valueOf(ID));

            CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.d("msg",resStr);
                    try {
                        Log.d("SUCCESS", "success报名");
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
            },h);
            isRegistered = "true";
            btnApply.setText("取消报名");
        }
        else{
            String url = "/api/student/sign_out/";

            HashMap<String,String> h = new HashMap<>();
            h.put("project_id", String.valueOf(ID));

            CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.d("msg",resStr);
                    try {
                        Log.d("SUCCESS", "success取消报名");
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
            },h);
            isRegistered = "false";
            btnApply.setText("报名");
        }

    }

    private void checkProject(){

        Intent in=new Intent(DetailActivityTeacher.this, CheckUser.class);
        in.putExtra("id",ID);
        startActivity(in);
    }

    private void deleteProject(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailActivityTeacher.this);
        dialog.setTitle("是否确定删除此发布？");
        dialog.setMessage("若删除则无法再恢复");

        HashMap<String,String> h = new HashMap<>();
        h.put("id", String.valueOf(ID));

        dialog.setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "/api/teacher/cancel_recruit/";

                CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        try {
                        } catch (Exception e) {
                            Log.e("error", e.toString());
                        }
                    }
                },h);
                Intent in=new Intent(DetailActivityTeacher.this, mainPage.class);
                startActivity(in);
            }
        });
        dialog.show();

    }

    private void editProject(){

        if(!isEdit){
            isEdit=true;
            researchField.setFocusableInTouchMode(true);
            researchField.setFocusable(true);
            researchField.requestFocus();
            description.setFocusableInTouchMode(true);
            description.setFocusable(true);
            description.requestFocus();
            master.setFocusableInTouchMode(true);
            master.setFocusable(true);
            master.requestFocus();
            master.setClickable(true);
            phd.setFocusableInTouchMode(true);
            phd.setFocusable(true);
            phd.requestFocus();
            phd.setClickable(true);
            undergraduate.setFocusableInTouchMode(true);
            undergraduate.setFocusable(true);
            undergraduate.requestFocus();
            undergraduate.setClickable(true);
            btnEdit.setText("提交修改");
        }
        else{
            isEdit =false;
            researchField.setFocusableInTouchMode(false);
            researchField.setFocusable(false);
            description.setFocusableInTouchMode(false);
            description.setFocusable(false);
            undergraduate.setFocusableInTouchMode(false);
            undergraduate.setFocusable(false);
            undergraduate.setClickable(false);
            phd.setFocusableInTouchMode(false);
            phd.setFocusable(false);
            phd.setClickable(false);
            master.setFocusableInTouchMode(false);
            master.setFocusable(false);
            master.setClickable(false);

            String requirements = "";
            if(undergraduate.isChecked())
                requirements += "本科生 ";
            if(master.isChecked())
                requirements += "硕士生 ";
            if(phd.isChecked())
                requirements += "博士生 ";

            String newResearchField = researchField.getText().toString();
            if(!newResearchField.equals(oldResearchFieldname)){
                oldResearchFieldname = newResearchField;
//                System.out.println("新的研究方向："+newResearchField);
            }
            String newDescription = description.getText().toString();
            if(!newDescription.equals(oldDescription)){
                oldDescription = newDescription;
//                System.out.println("新的项目描述："+ newDescription);
            }
            btnEdit.setText("编辑");

            // store to server
            HashMap<String,String> h = new HashMap<>();
            h.put("id", String.valueOf(ID));
            h.put("requirement",requirements);
            h.put("description",newDescription);
            h.put("research_direction",newResearchField);
            h.put("title",topicName);

            String url = "/api/teacher/update_recruit/";
            CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    try {

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
            },h);
        }
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
                Log.d("d",resStr);
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resStr);
                    JSONObject data = jsonObject.getJSONObject("data");

                    reqName = data.get("requirement").toString();
                    tchName = data.get("teacher").toString();
                    deptName = data.get("department").toString();
                    desctiptName = data.get("description").toString();
                    researchName = data.get("research_direction").toString();
                    isRegistered = data.get("isRegistered").toString();
                    isStarred = data.get("isStarred").toString();

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
