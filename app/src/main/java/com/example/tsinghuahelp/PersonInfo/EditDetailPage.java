package com.example.tsinghuahelp.PersonInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tsinghuahelp.R;

public class EditDetailPage extends AppCompatActivity {
    private TextView tl_title;
    private EditText edit;
    private EditText edit_realname;
    private EditText edit_school;
    private EditText edit_department;
    private EditText edit_grade;
    int msg;


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

        }

        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent();
                if(msg!=1){
                    Log.d("返回", edit.getText().toString());
                    back.putExtra("msg",edit.getText().toString());}
                else {
                    back.putExtra("realname",edit_realname.getText().toString());
                    back.putExtra("school",edit_school.getText().toString());
                    back.putExtra("department",edit_department.getText().toString());
                    back.putExtra("grade",edit_grade.getText().toString());
                }
                setResult(RESULT_OK, back);
                finish();
            }
        });
    }
}
