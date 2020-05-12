package com.example.tsinghuahelp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class DetailActivity extends AppCompatActivity {

    TextView topic;
    TextView teacher;
    TextView time;
    TextView department;
    TextView require;
    TextView researchField;
    TextView description;
    Button btnStar;
    Button btnApply;

    public static Handler msgHandler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();

        btnStar = (Button)findViewById(R.id.btn_star);
        btnApply = (Button)findViewById(R.id.btn_apply);

        topic = (TextView)findViewById(R.id.projectName);
        department = (TextView)findViewById(R.id.projectDept);
        description = (TextView)findViewById(R.id.projectDescript);
        researchField = (TextView)findViewById(R.id.projectField);
        require = (TextView)findViewById(R.id.projectRequire);
        time = (TextView)findViewById(R.id.projectTime);
        teacher = (TextView)findViewById(R.id.projectTeacher);

        topic.setText(intent.getStringExtra("title"));

        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(DetailActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnApply.getText() == "报名")
                    btnApply.setText("取消");
                else
                    btnApply.setText("报名");
            }
        });

        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                HashMap<String,String> h = new HashMap<>();
//                // username
//                h.put("username",mUsername.getText().toString());
//                h.put("password",mPassword.getText().toString());
//                h.put("type","true");
//
//                CommonInterface.sendOkHttpPostRequest("/api/user/register", new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        Log.e("error", e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        String resStr = response.body().string();
//                        DetailActivity.this.runOnUiThread(() -> Toast.makeText(DetailActivity.this, resStr, Toast.LENGTH_LONG).show());
//                        Log.e("response", resStr);
////                        try {
////                            // 解析json，然后进行自己的内部逻辑处理
////                            JSONObject jsonObject = new JSONObject(resStr);
////                        } catch (JSONException e) {
////
////                        }
//                    }
//                },h);
            }
        });
    }
}
