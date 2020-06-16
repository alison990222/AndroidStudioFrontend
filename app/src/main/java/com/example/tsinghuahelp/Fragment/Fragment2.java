package com.example.tsinghuahelp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.example.tsinghuahelp.news.PostAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Fragment2 extends Fragment {

    View mView;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private SearchResultAdapter adapter;
    private List<SearchResult> resultsList;
    private String choose = "All";

    public Fragment2() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(getContext(),"后端信息获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Log.e("m_tag","收到更新");
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        get_recommend();

        resultsList = new ArrayList<>();

        resultsList.add(new SearchResult("移动应用与开发","王老师",
                "软件学院", "巨难无比，请谨慎选课","project",0));
        resultsList.add(new SearchResult("Alison","张瑄庭",
                "软件学院", "喜欢德国男友","student",0));
        resultsList.add(new SearchResult("菜鸡本鸡","汪颖祺",
                "软件学院", "亲切的汪老师","teacher",0));
        resultsList.add(new SearchResult("想一台智能机械小车","刘薇",
                "软件学院", "嵌入式课程需要orz","plan",0));

    }

    private void get_recommend() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search2, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);
        recyclerView = mView.findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SearchResultAdapter(getContext(),resultsList);
        recyclerView.setAdapter(adapter);
        spinner = (Spinner) mView.findViewById(R.id.search_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //拿到被选择项的值
                choose = (String) spinner.getSelectedItem();
//                Toast.makeText(getContext(),"选择了"+choose,Toast.LENGTH_SHORT).show();
                Log.e("m","选择了"+choose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return mView;
    }

    private void search() {

    }

}
