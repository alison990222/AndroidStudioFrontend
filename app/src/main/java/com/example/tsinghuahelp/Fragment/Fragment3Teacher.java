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

import com.example.tsinghuahelp.MainActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.mainPage;
import com.example.tsinghuahelp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment3Teacher extends Fragment {
    private View mView;
    private TextView username;
    private EditText researchField,projectName, detail;
    private CheckBox undergraduate,master, phd;
    private Button btnPost;
    public static Handler msgHandler;

    public Fragment3Teacher() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_post3_teacher, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);

        btnPost = mView.findViewById(R.id.btn_post);
        username = mView.findViewById(R.id.username);
        researchField = mView.findViewById(R.id.researchField);
        projectName = mView.findViewById(R.id.projectName);
        detail = mView.findViewById(R.id.postDetail);
        undergraduate = mView.findViewById(R.id.checkBox_undergraduate);
        master = mView.findViewById(R.id.checkBox_master);
        phd = mView.findViewById(R.id.checkBox_phd);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String requirements = "";
                if(undergraduate.isChecked())
                    requirements += "本科生 ";
                if(master.isChecked())
                    requirements += "硕士生 ";
                if(phd.isChecked())
                    requirements += "博士生 ";


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


                String finalRequirements = requirements;
                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HashMap<String,String> h = new HashMap<>();
                        h.put("title",projectName.getText().toString());
                        h.put("research_direction",researchField.getText().toString());
                        h.put("requirement", finalRequirements);
                        h.put("description",detail.getText().toString());

                        CommonInterface.sendOkHttpPostRequest("/api/teacher/upload_recruit", new Callback() {

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

//                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                dialog.setTitle("是否确定发布此贴文？");
//                dialog.setMessage("发布后可在详情页进行修改删除");
//                dialog.setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent in=new Intent(getContext(), mainPage.class);
//                        startActivity(in);
//                    }
//                });
//                dialog.show();
            }
        });

        return mView;
    }
}
