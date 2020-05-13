package com.example.tsinghuahelp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsinghuahelp.MainActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.news.PostAdapter;
import com.example.tsinghuahelp.news.Posts;
import com.example.tsinghuahelp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment1 extends Fragment {

    private View mView;

    private RecyclerView recyclerView;
    private PostAdapter adapter;

    private List<Posts> postsList;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        postsList = new ArrayList<>();

        postsList.add(
                new Posts(
                        "陈老师",
                        "一起做安卓",
                        "移动应用",
                        "软件学院",
                        "60000",
                       "require"));

        postsList.add(
                new Posts(
                        "王老师",
                        "机器人。。。",
                        "硬件。。。",
                        "自动化",
                        "60000",
                        "require"));

        postsList.add(
                new Posts(
                        "田老师",
                        "计算机视觉",
                        "人工智能计算机视觉",
                        "计算机",
                        "60000",
                        "require"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_main1, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);

        recyclerView = mView.findViewById(R.id.recyclerView4);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PostAdapter(getContext(),postsList);
        recyclerView.setAdapter(adapter);


        return mView;
    }

}
