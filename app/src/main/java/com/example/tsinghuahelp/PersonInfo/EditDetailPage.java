package com.example.tsinghuahelp.PersonInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.Global;
import com.example.tsinghuahelp.utils.MyDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditDetailPage extends Activity {
    private TextView tl_title;
    private EditText edit;
    private EditText edit_realname;
    private EditText edit_school;
    private EditText edit_department;
    private EditText edit_grade;
    private EditText edit_old_password;
    private EditText edit_new_password;
    int msg;


    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Global.FAIL_CODE:
                    Toast.makeText(EditDetailPage.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case Global.UPDATE_PASS_CODE:
                    Log.e("m_tag","更新密码成功");
                    finish();
                    break;
                case Global.UPDATE_FAIL_CODE:
                    showWarningInfo("密码错误或更新失败！");
                    break;
                case Global.FINISH_CODE:
                    finish();
                    break;
            }
        }
    };


    private void showWarningInfo(String detail) {
        String message=detail;
        final MyDialog dialog = new MyDialog(this);
        dialog.setMessage(message)
                .setTitle("提示")
                .setPositive("我知道了")
                .setNegtive("我要退出")
                .setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
                        dialog.dismiss();
                        Message message=new Message();
                        message.what=Global.FINISH_CODE;
                        mHandler.sendMessage(message);
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail_page);

        tl_title = findViewById(R.id.tv_title);
        edit = findViewById(R.id.et_edit);
        edit_department=findViewById(R.id.et_edit_department);
        edit_grade=findViewById(R.id.et_edit_grade);
        edit_realname=findViewById(R.id.et_edit_name);
        edit_school=findViewById(R.id.et_edit_school);
        edit_new_password=findViewById(R.id.et_edit_new_password);
        edit_old_password=findViewById(R.id.et_edit_old_password);

        Intent intent = getIntent();
        msg=intent.getIntExtra("msg",0);
        switch (msg){
            case 0:
                tl_title.setText("编辑昵称");
                edit.setText(intent.getStringExtra("str"));
                edit.setHint("不超过14个字符");
                edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
                break;
            case 1:
                tl_title.setText("验证信息");
                edit_realname.setText(intent.getStringExtra("realname"));
                edit_school.setText(intent.getStringExtra("school"));
                edit_department.setText(intent.getStringExtra("department"));
                edit_grade.setText(intent.getStringExtra("grade"));
                findViewById(R.id.oneline).setVisibility(View.GONE);
                findViewById(R.id.fourline).setVisibility(View.VISIBLE);
                break;
            case 2:
                tl_title.setText("编辑签名");
                edit.setText(intent.getStringExtra("str"));
                edit.setHint("不超过30个字符");
                edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                break;
            case 3:
                tl_title.setText("编辑个人信息");
                edit.setText(intent.getStringExtra("str"));
                break;
            case 4:
                tl_title.setText("修改密码");
                findViewById(R.id.oneline).setVisibility(View.GONE);
                findViewById(R.id.twoline).setVisibility(View.VISIBLE);
                break;

        }

        findViewById(R.id.backward_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent();
                if(msg==1) {
                    back.putExtra("realname", edit_realname.getText().toString());
                    back.putExtra("school", edit_school.getText().toString());
                    back.putExtra("department", edit_department.getText().toString());
                    back.putExtra("grade", edit_grade.getText().toString());
                    setResult(RESULT_OK, back);
                    finish();
                }
                else if(msg==4){
                    update_password();
                }
                else {
                    Log.d("返回", edit.getText().toString());
                    back.putExtra("msg", edit.getText().toString());
                    setResult(RESULT_OK, back);
                    finish();
                }

            }
        });
    }

    private void update_password() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "/api/user/update_password";
                HashMap<String, String> h = new HashMap<>();
                h.put("old_password", edit_old_password.getText().toString());
                h.put("new_password", edit_new_password.getText().toString());
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
                            }
                            Message message = new Message();
                            message.what = Global.UPDATE_PASS_CODE;
                            mHandler.sendMessage(message);
                        } catch (Exception e) {
                            Message message = new Message();
                            message.what = Global.UPDATE_FAIL_CODE;
                            mHandler.sendMessage(message);
                        }
                    }
                }, h);
            }
        }).start();

    }
}
