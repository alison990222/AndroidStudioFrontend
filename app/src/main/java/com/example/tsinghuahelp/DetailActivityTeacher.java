package com.example.tsinghuahelp;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.example.tsinghuahelp.CheckUser.CheckUser;
import com.example.tsinghuahelp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailActivityTeacher extends AppCompatActivity {

    TextView topic;
    TextView teacher;
    TextView time;
    TextView department;
    TextView require;
    EditText researchField;
    EditText description;
    Button btnEdit;
    Button btnCheck;
    Button btnDelete;
    Boolean isEdit=false;
    private CheckBox undergraduate,master, phd;
    private String oldResearchFieldname;
    private String oldDescription;
    public static Handler msgHandler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_teacher);

        Intent intent = this.getIntent();

        btnEdit = (Button)findViewById(R.id.btn_edit);
        btnCheck = (Button)findViewById(R.id.btn_check);
        btnDelete = (Button)findViewById(R.id.btn_delete);

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
        topic.setText(intent.getStringExtra("title"));

        oldResearchFieldname = researchField.getText().toString();
        oldDescription = description.getText().toString();

        // 消息处理
        msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(DetailActivityTeacher.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };

        // 关键权限必须动态申请
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        System.out.println("新的研究方向："+newResearchField);
                    }
                    String newDescription = description.getText().toString();
                    if(!newDescription.equals(oldDescription)){
                        oldDescription = newDescription;
                        System.out.println("新的项目描述："+ newDescription);
                    }
                    btnEdit.setText("编辑");
                }
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(DetailActivityTeacher.this, CheckUser.class);
                startActivity(in);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailActivityTeacher.this);
                dialog.setTitle("是否确定删除此发布？");
                dialog.setMessage("若删除则无法再恢复");
                dialog.setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in=new Intent(DetailActivityTeacher.this, mainPage.class);
                        startActivity(in);
                    }
                });
                dialog.show();
            }
        });


    }
}
