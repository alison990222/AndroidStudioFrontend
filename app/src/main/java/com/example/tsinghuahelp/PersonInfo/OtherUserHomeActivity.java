package com.example.tsinghuahelp.PersonInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tsinghuahelp.Adapter.InfoAdapter;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OtherUserHomeActivity extends AppCompatActivity {
    com.mikhaellopez.circularimageview.CircularImageView icon;
    TextView other_username;
    TextView is_verify;
    Button btn_verify;
    Button btn_track;
    Button btn_chat;
    TextView signature;
    TabLayout tabLayout;
    RecyclerView myRecyclerView;
    InfoAdapter infoAdapter;
    SearchResultAdapter pAdapter;
    private List<String> infoList;
    private List<SearchResult> pList;
    Boolean type=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personal5);

        tabLayout = findViewById(R.id.tab_layout);
        findViewById(R.id.pro_num).setVisibility(View.INVISIBLE);
        findViewById(R.id.follower_num).setVisibility(View.INVISIBLE);
        findViewById(R.id.follow_num).setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_info_btn).setVisibility(View.GONE);
        other_username=findViewById(R.id.info_username);
        is_verify=findViewById(R.id.verify_info);
        btn_verify=findViewById(R.id.verify_btn);
        signature=findViewById(R.id.info_signature);
        btn_track=findViewById(R.id.follow_btn);
        btn_chat=findViewById(R.id.chat_btn);
        btn_track.setVisibility(View.VISIBLE);
        btn_chat.setVisibility(View.VISIBLE);


        myRecyclerView = findViewById(R.id.info_recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String plan_or_pro;

        if(type){
            plan_or_pro="我的项目";
        }
        else{
            plan_or_pro="我的发文";
        }
        tabLayout.addTab(tabLayout.newTab().setText("关于我"));
        tabLayout.addTab(tabLayout.newTab().setText(plan_or_pro));

        infoList = new ArrayList<>();

        infoList.add("我对软件开发很感兴趣，曾经做过：\n -Cosine大学生竞赛平台\n -“找导师”移动应用开发");
        pList = new ArrayList<>();

        pList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));
        pList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));

        pAdapter = new SearchResultAdapter(this,pList);
        infoAdapter = new InfoAdapter(this,infoList);

        myRecyclerView.setAdapter(infoAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        myRecyclerView.setAdapter(infoAdapter);
                        break;
                    case 1:
                        myRecyclerView.setAdapter(pAdapter);
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


    }
}
