package com.example.tsinghuahelp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Fragment5 extends Fragment {
    View mView;
    TabLayout tabLayout;
    RecyclerView myRecyclerView;
    InfoAdapter infoAdapter;
    SearchResultAdapter proAdapter;
    SearchResultAdapter planAdapter;
    private List<String> infoList;
    private List<SearchResult> proList;
    private List<SearchResult> planList;


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


        //根据老师和学生信息判断，这里需要从后端提供数据，个人信息，我的报名，我的发文之类的
        //关于查看别人的主页，建议新建一个activity利用这个布局文件
        tabLayout.addTab(tabLayout.newTab().setText("关于我"));
        tabLayout.addTab(tabLayout.newTab().setText("我的报名"));
        tabLayout.addTab(tabLayout.newTab().setText("我的发文"));

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
                EditText editText = layout.findViewById(R.id.personal_info_show);
                editText.setFocusableInTouchMode(true);
                editText.setFocusable(true);
                editText.requestFocus();
//                editText.setText("asdasda");
                System.out.println("hihihi"+position);
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


}
