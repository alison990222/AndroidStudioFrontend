package com.example.tsinghuahelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.Fragment.Fragment5;
import com.example.tsinghuahelp.PersonInfo.OtherUserHomeActivity;
import com.example.tsinghuahelp.news.Posts;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DetailActivity extends Activity implements View.OnClickListener {

    private TextView topic;
    private TextView ownerName;
    private TextView time;
    private TextView department;
    private TextView identity;
    private EditText interestField;
    private EditText description;

    private Button btnDelete;
    private Button btnEdit;
    private int planID;
    Boolean isEdit=false;
    private String topicName;

    private String deptName;
    private String plan_direction;
    private String desctiptDetail;
    private String studentType;
    private String createTime;
    private String studentName;

    private String oldInterestField;
    private String oldDescription;

    private String studentID;

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
//                    Toast.makeText(StarFollowAll.this,"后端信息获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    ownerName.setText("姓名："+ studentName);
                    department.setText("院系："+ deptName);
                    description.setText(desctiptDetail);
                    interestField.setText(plan_direction);
                    time.setText(createTime);
                    topic.setText(topicName);
                    identity.setText(studentType);


                    // if it's the owner of the post
                    if (studentID.equals(String.valueOf(Global.CURRENT_ID))) {  // student
                        btnDelete.setVisibility(View.VISIBLE);
                        btnEdit.setVisibility(View.VISIBLE);
                    }

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();
        // intent
        planID = intent.getIntExtra("id",-1);

        btnDelete = (Button)findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);

        btnEdit = (Button)findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);

        ownerName = (TextView) findViewById(R.id.ownerName);
        ownerName.setOnClickListener(this);

        topic = (TextView) findViewById(R.id.projectName);
        time = (TextView) findViewById(R.id.projectTime);
        interestField = (EditText) findViewById(R.id.interestField);

        department = (TextView) findViewById(R.id.departmentName);
        description = (EditText) findViewById(R.id.projectDescript);
        identity = (TextView) findViewById(R.id.identity);

        btnDelete.setVisibility(View.INVISIBLE);
        btnEdit.setVisibility(View.INVISIBLE);
        fresh_page();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                deleteProject();
                break;

            case R.id.btn_edit:
                editProject();
                break;

            case R.id.ownerName:
                Intent in=new Intent(DetailActivity.this, OtherUserHomeActivity.class);
                in.putExtra("id",studentID);
                startActivity(in);
                break;
        }
    }

    private void deleteProject(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailActivity.this);
        dialog.setTitle("是否确定删除此发布？");
        dialog.setMessage("若删除则无法再恢复");

        HashMap<String,String> h = new HashMap<>();
        h.put("id", String.valueOf(planID));

        dialog.setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "/api/student/cancel_plan/";

                CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resStr);

                        try {
                            if(jsonObject.get("response").equals("valid")){
                                DetailActivity.this.runOnUiThread(() -> Toast.makeText(DetailActivity.this, "删除成功", Toast.LENGTH_LONG).show());
                            }
                            else{
                                DetailActivity.this.runOnUiThread(() -> Toast.makeText(DetailActivity.this, jsonObject.get("response").toString(), Toast.LENGTH_LONG).show());
                            }
                        } catch (Exception e) {
                            DetailActivity.this.runOnUiThread(() ->Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_LONG).show());
                        }
                    }
                },h);
                Intent in=new Intent(DetailActivity.this, mainPage.class);
                startActivity(in);
            }
        });
        dialog.show();
    }

    private void editProject(){
        if(!isEdit){
            isEdit=true;
            interestField.setFocusableInTouchMode(true);
            interestField.setFocusable(true);
            interestField.requestFocus();
            description.setFocusableInTouchMode(true);
            description.setFocusable(true);
            description.requestFocus();

            btnEdit.setText("提交修改");
        }
        else{
            isEdit =false;
            interestField.setFocusableInTouchMode(false);
            interestField.setFocusable(false);
            description.setFocusableInTouchMode(false);
            description.setFocusable(false);


            String newResearchField = interestField.getText().toString();
            if(!newResearchField.equals(oldInterestField)){
                oldInterestField = newResearchField;
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
            h.put("id", String.valueOf(planID));
            h.put("type",studentType);
            h.put("description",newDescription);
            h.put("plan_direction",newResearchField);
            h.put("title",topicName);


            String url = "/api/student/update_plan/";
            CommonInterface.sendOkHttpPostRequest(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("error", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resStr);

                    try {
                        if(jsonObject.get("response").equals("valid")){
                            DetailActivity.this.runOnUiThread(() -> Toast.makeText(DetailActivity.this, "修改成功", Toast.LENGTH_LONG).show());
                        }
                        else{
                            DetailActivity.this.runOnUiThread(() -> Toast.makeText(DetailActivity.this, jsonObject.get("response").toString(), Toast.LENGTH_LONG).show());
                        }

                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);

                    } catch (Exception e) {
                        DetailActivity.this.runOnUiThread(() -> Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_LONG).show());

                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                    }
                }
            },h);
        }

    }

    private void fresh_page () {
        String url = "/api/user/plan_info/" + String.valueOf(planID);
        CommonInterface.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.d("studentdata",resStr);
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resStr);
                    JSONObject data = jsonObject.getJSONObject("data");

                    deptName = data.get("department").toString();
                    desctiptDetail = data.get("description").toString();
                    plan_direction = data.get("plan_direction").toString();
                    studentType = data.get("type").toString();
                    createTime = data.get("createTime").toString();
                    studentID = data.get("student_id").toString();
                    topicName = data.get("plan_title").toString();
                    studentName = data.get("student_name").toString();

                    oldInterestField = plan_direction;
                    oldDescription = desctiptDetail;

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
