package com.example.tsinghuahelp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tsinghuahelp.Adapter.InfoAdapter;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Fragment5 extends Fragment implements View.OnClickListener {
    View mView;
    TabLayout tabLayout;
    RecyclerView myRecyclerView;
    InfoAdapter infoAdapter;
    SearchResultAdapter proAdapter;
    SearchResultAdapter planAdapter;
    private List<String> infoList;
    private List<SearchResult> proList;
    private List<SearchResult> planList;
    Boolean isEdit=false;
    Boolean iconChange = false;
    Boolean usernameChange=false;
    Boolean signatureChange = false;
    Boolean personalChange = false;
    Button btn_edit;
    Button btn_verify;
    EditText edit_user;
    EditText edit_signature;
    EditText edit_personal=null;
    TextView is_verify;
    com.mikhaellopez.circularimageview.CircularImageView edit_pic;
    String oldUsername;
    String oldSignature;
    String oldPersonInfo;



    public Fragment5() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_personal5, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);
        tabLayout = mView.findViewById(R.id.tab_layout);
        TextView starORpro=mView.findViewById(R.id.star_or_proj);
        myRecyclerView = mView.findViewById(R.id.info_recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btn_edit=mView.findViewById(R.id.edit_info_btn);
        is_verify=mView.findViewById(R.id.is_verify);
        edit_user=mView.findViewById(R.id.info_username);
        edit_signature=mView.findViewById(R.id.info_signature);
        btn_verify=mView.findViewById(R.id.info_verify);
        btn_verify.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        edit_signature.setOnClickListener(this);
        edit_user.setOnClickListener(this);
        edit_pic = mView.findViewById(R.id.info_pic);
        edit_pic.setOnClickListener(this);
        mView.findViewById(R.id.follow_num).setOnClickListener(this);
        mView.findViewById(R.id.follower_num).setOnClickListener(this);
        mView.findViewById(R.id.pro_num).setOnClickListener(this);



        //根据老师和学生信息判断，这里需要从后端提供数据，个人信息，我的报名，我的发文之类的
        //关于查看别人的主页，建议新建一个activity利用这个布局文件
        tabLayout.addTab(tabLayout.newTab().setText("关于我"));
        tabLayout.addTab(tabLayout.newTab().setText("我的报名"));
        tabLayout.addTab(tabLayout.newTab().setText("我的发文"));
        oldUsername=edit_user.getText().toString();
        oldSignature=edit_signature.getText().toString();

        infoList = new ArrayList<>();

        infoList.add("我对软件开发很感兴趣，曾经做过：\n -Cosine大学生竞赛平台\n -“找导师”移动应用开发");

        proList = new ArrayList<>();

        proList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));
        proList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));
        proList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));
        proList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));

        planList = new ArrayList<>();

        planList.add(new SearchResult("想一台智能机械小车","刘薇",
                "软件学院", "嵌入式课程需要orz","plan",0));
        planList.add(new SearchResult("想一台智能机械小车","刘薇",
                "软件学院", "嵌入式课程需要orz","plan",0));

        proAdapter = new SearchResultAdapter(getContext(),proList);
        planAdapter = new SearchResultAdapter(getContext(),planList);
        infoAdapter = new InfoAdapter(getContext(),infoList);


        myRecyclerView.setAdapter(infoAdapter);
        infoAdapter.setOnitemClickLintener(new InfoAdapter.OnitemClick() {
            @Override
            public void onItemClick(int position) {
                LinearLayout layout = (LinearLayout) myRecyclerView.getChildAt(position);
                edit_personal = layout.findViewById(R.id.personal_info_show);
                if(isEdit){
                    oldPersonInfo=edit_personal.getText().toString();
                    personalChange=true;
                    edit_personal.setFocusableInTouchMode(true);
                    edit_personal.setFocusable(true);
                    edit_personal.requestFocus();
                    System.out.println("hi, personal info!");
                }

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        myRecyclerView.setAdapter(infoAdapter);
                        break;
                    case 1:
                        myRecyclerView.setAdapter(proAdapter);
                        break;
                    case 2:
                        myRecyclerView.setAdapter(planAdapter);
                        break;
                    default:
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return mView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_info_btn:
                if(!isEdit){
                    isEdit=true;
                    edit_user.setFocusableInTouchMode(true);
                    edit_user.setFocusable(true);
                    edit_user.requestFocus();
                    edit_signature.setFocusableInTouchMode(true);
                    edit_signature.setFocusable(true);
                    edit_signature.requestFocus();
                    btn_edit.setText("提交修改");
                }
                else{
                    isEdit =false;
                    edit_user.setFocusableInTouchMode(false);
                    edit_user.setFocusable(false);
                    edit_signature.setFocusableInTouchMode(false);
                    edit_signature.setFocusable(false);
                    if(edit_personal!=null) {
                        edit_personal.setFocusableInTouchMode(false);
                        edit_personal.setFocusable(false);
                        String newPersonalInfo = edit_personal.getText().toString();
                        if(!newPersonalInfo.equals(oldPersonInfo)){
                            //todo
                            personalChange=false;
                            oldPersonInfo=newPersonalInfo;
                            System.out.println("新的个人信息："+newPersonalInfo);
                        }
                    }
                    if(iconChange){
                        //todo
                        iconChange=false;
                        System.out.println("新的图片");
                    }
                    String newName = edit_user.getText().toString();
                    if(!newName.equals(oldUsername)){
                        //todo
                        usernameChange=false;
                        oldUsername=newName;
                        System.out.println("新的名字："+newName);
                    }
                    String newSignature = edit_signature.getText().toString();
                    if(!newSignature.equals(oldSignature)){
                        //todo
                        signatureChange=false;
                        oldSignature=newSignature;
                        System.out.println("新的签名："+newSignature);
                    }
                    btn_edit.setText("编辑");
                }
                break;
            case R.id.info_pic:
                if(isEdit){
                    iconChange=true;
                    System.out.println("hi, pic info");
                }
                break;
            case R.id.info_username:
                if(isEdit){
                    usernameChange=true;
                    System.out.println("hi, username info");
                }
                break;
            case R.id.info_signature:
                if(isEdit){
                    signatureChange=true;
                    System.out.println("hi,signature info");
                }
                break;
            case R.id.info_verify:
                break;
            case R.id.pro_num:
                System.out.println("hi,pro_num");
                break;
            case R.id.follow_num:
                System.out.println("hi,follow_num");
                break;
            case R.id.follower_num:
                System.out.println("hi,follower_num");
                break;

        }
    }
}
