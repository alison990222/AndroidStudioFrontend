package com.example.tsinghuahelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mPassword;
    private Button mButtonLogin;
    private TextView mTextview_register;
    private RadioGroup radioGroup;
    private RadioButton radioBtn;
    public static Handler msgHandler;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = (EditText)findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mButtonLogin = (Button)findViewById(R.id.button_login);
        mTextview_register = (TextView)findViewById(R.id.textview_register);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);


        mTextview_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selected = radioGroup.getCheckedRadioButtonId();
                Log.e("", String.valueOf(selected));
                radioBtn = (RadioButton) findViewById(selected);
                Toast.makeText(MainActivity.this, radioBtn.getText(), Toast.LENGTH_SHORT).show();
                if(radioBtn.getText().equals("学生")){
                    mainPage.type=false;
                }
                else{mainPage.type=true;}

                HashMap<String,String> h = new HashMap<>();
                h.put("username",mUsername.getText().toString());
                h.put("password",mPassword.getText().toString());

                CommonInterface.sendOkHttpPostRequest("/api/user/login", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this, resStr, Toast.LENGTH_LONG).show());
                        Log.e("response", resStr);
                        try {
                            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resStr);
                            JSONObject data=jsonObject.getJSONObject("data");
                            mainPage.type = data.getBoolean("type");
                            // 解析json，然后进行自己的内部逻辑处理
//                            JSONObject jsonObject = new JSONObject(resStr);
//                            String chocolateChip = CookieManager.getInstance().getCookie(response);

                        } catch (Exception e) {

                        }
                    }
                },h);

                Intent mainIntent = new Intent(MainActivity.this, mainPage.class);
                startActivity(mainIntent);
            }
        });
    }


}
