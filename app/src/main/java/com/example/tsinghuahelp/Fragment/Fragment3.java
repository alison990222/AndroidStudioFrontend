package com.example.tsinghuahelp.Fragment;

import android.Manifest;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.tsinghuahelp.DetailActivityTeacher;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.mainPage;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.MyDialog;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment3 extends Fragment {
    private View mView;
    private TextView username;
    private EditText interestedField;
    private EditText detail;
    private Button btnPost;

    private EditText projectName;
    private CheckBox undergraduate,master, phd;
    public static Handler msgHandler;

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_post3, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);

        btnPost = mView.findViewById(R.id.btn_post);
        username = mView.findViewById(R.id.username);
        interestedField = mView.findViewById(R.id.interestField);
        projectName = mView.findViewById(R.id.projectName);
        detail = mView.findViewById(R.id.postDetail);
        undergraduate = mView.findViewById(R.id.checkBox_undergraduate);
        master = mView.findViewById(R.id.checkBox_master);
        phd = mView.findViewById(R.id.checkBox_phd);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String studentType = "";
                if(undergraduate.isChecked())
                    studentType += "本科生 ";
                if(master.isChecked())
                    studentType += "硕士生 ";
                if(phd.isChecked())
                    studentType += "博士生 ";

                String finalstudentType = studentType;

                HashMap<String,String> h = new HashMap<>();
                h.put("title",projectName.getText().toString());
                h.put("plan_direction", interestedField.getText().toString());
                h.put("type", finalstudentType);
                h.put("description",detail.getText().toString());


                final MyDialog dialog = new MyDialog(getContext());
                dialog.setMessage("发布后可在详情页进行修改删除")
                        .setTitle("是否确定发布此贴文？")
                        .setSingle(true).setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        dialog.dismiss();
                        CommonInterface.sendOkHttpPostRequest("/api/student/upload_plan", new Callback() {

                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Log.e("error", e.toString());
                            }

                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String resStr = response.body().string();
                                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resStr);
                                try{
                                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show());
                                    Intent in=new Intent(getContext(), mainPage.class);
                                    startActivity(in);

                                }
                                catch (Exception e){
                                    Log.e("error",e.toString());
                                }
                            }
                        },h);
                    }

                    @Override
                    public void onNegtiveClick() {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        return mView;
    }
}
