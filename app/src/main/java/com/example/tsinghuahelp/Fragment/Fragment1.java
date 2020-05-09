package com.example.tsinghuahelp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.news.PostAdapter;
import com.example.tsinghuahelp.news.Posts;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Fragment1 extends Fragment {

    View mView;

    RecyclerView recyclerView;
    PostAdapter adapter;

    List<Posts> postsList;
    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        postsList = new ArrayList<>();




        postsList.add(
                new Posts(
                        "alison",
                        "helllollo android",
                        "13.3 infdsssfjdiosajioajsdoifjasoifjsaoifjdsoijfois",
                        "4.3",
                        "60000",
                       "require"));

        postsList.add(
                new Posts(
                        "alison",
                        "helllollo android",
                        "13.3 inch, Silver, 1.35 kg",
                        "4.3",
                        "60000",
                        "require"));

        postsList.add(
                new Posts(
                        "alison",
                        "helllollo android",
                        "13.3 inch, Silver, 1.35 kg",
                        "4.3",
                        "60000",
                        "require"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_main1, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);

        recyclerView = mView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PostAdapter(getContext(),postsList);
        recyclerView.setAdapter(adapter);

        return mView;
    }

}
