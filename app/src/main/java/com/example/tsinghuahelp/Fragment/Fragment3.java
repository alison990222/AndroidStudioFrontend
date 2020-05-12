package com.example.tsinghuahelp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tsinghuahelp.R;

import butterknife.ButterKnife;

public class Fragment3 extends Fragment {
    View mView;
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

        return mView;
    }

}
