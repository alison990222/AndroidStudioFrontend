package com.example.tsinghuahelp.RegisterTab;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tsinghuahelp.MainActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.utils.CommonInterface;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterStudent extends Fragment {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mcnfPassword;
    private Button mButtonRegister;
    private TextView mTextview_Login;
    private View mView;
    public static Handler msgHandler;

//    @SuppressLint("HandlerLeak")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.register_student, container, false);
        mUsername = mView.findViewById(R.id.username);
        mPassword = mView.findViewById(R.id.password);
        mcnfPassword = mView.findViewById(R.id.confirm_password);
        mButtonRegister = mView.findViewById(R.id.button_register);
        mTextview_Login = mView.findViewById(R.id.textview_login);

        // 初始化websocket
//        WebSocket.initSocket();

        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);


        mTextview_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getContext(), MainActivity.class);
                startActivity(loginIntent);
            }
        });


        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> h = new HashMap<>();
                h.put("username",mUsername.getText().toString());
                h.put("password",mPassword.getText().toString());
                h.put("type","false"); // false means = student

                CommonInterface.sendOkHttpPostRequest("/api/user/register", new Callback() {

                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                    }

                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        getActivity().runOnUiThread(() -> Toast.makeText(getContext(), resStr, Toast.LENGTH_LONG).show());
                        Log.e("response", resStr);

                    }
                },h);
            }
        });

        return mView;
    }
}
