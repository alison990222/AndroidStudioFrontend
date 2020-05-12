package com.example.tsinghuahelp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.tsinghuahelp.DetailActivityTeacher;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.mainPage;

import butterknife.ButterKnife;

public class Fragment3 extends Fragment {
    View mView;
    TextView username;
    EditText interestedField;
    EditText detail;
    Button btnPost;

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
        detail = mView.findViewById(R.id.postDetail);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("是否确定发布此贴文？");
                dialog.setMessage("发布后可在详情页进行修改删除");
                dialog.setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in=new Intent(getContext(), mainPage.class);
                        startActivity(in);
                    }
                });
                dialog.show();
            }
        });

        return mView;
    }
}
