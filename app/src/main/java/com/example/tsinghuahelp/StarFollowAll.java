package com.example.tsinghuahelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tsinghuahelp.Follow.FollowUser;
import com.example.tsinghuahelp.Follow.FollowUserAdapter;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;

import java.util.ArrayList;
import java.util.List;

public class StarFollowAll extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private FollowUserAdapter followUserAdapter;
    private SearchResultAdapter proAdapter;
    private List<FollowUser> followUserList = new ArrayList<FollowUser>();
    private List<SearchResult> proList = new ArrayList<SearchResult>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.star_follow_all);
        textView=findViewById(R.id.textView);
        recyclerView = findViewById(R.id.star_or_follow_recycle);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        proList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));
        proList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));

        followUserList.add(new FollowUser("",false,0,"wyq"));
        followUserList.add(new FollowUser("",false,0,"zxt"));


        Intent intent = this.getIntent();
        String type = intent.getStringExtra("type");
        assert type != null;
        if(type.equals("star")){
            //从服务器获取项目
            textView.setText("关注项目");
            proAdapter = new SearchResultAdapter(this,proList);
            recyclerView.setAdapter(proAdapter);
        }
        else if(type.equals("follow")){
            //从服务器获取我的关注
            textView.setText("我的关注");
            followUserAdapter = new FollowUserAdapter(this,followUserList);
            recyclerView.setAdapter(followUserAdapter);
        }
        else{
            //从服务器获取关注我的人
            textView.setText("关注我的");
            followUserAdapter = new FollowUserAdapter(this,followUserList);
            recyclerView.setAdapter(followUserAdapter);
        }

    }
}
