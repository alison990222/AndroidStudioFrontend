package com.example.tsinghuahelp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.tsinghuahelp.Adapter.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import java.net.HttpURLConnection;


public class RegisterActivity extends FragmentActivity {

    private HttpURLConnection conn;

    private ViewPager viewPager;
    private TabLayout mlayout;

    private TabLayout.Tab one;
    private TabLayout.Tab two;

    public static Handler msgHandler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewPager = (ViewPager)findViewById(R.id.pager);
        mlayout = (TabLayout)findViewById(R.id.tab_layout);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),2));

        mlayout.setupWithViewPager(viewPager);

        one = mlayout.getTabAt(0);
        two = mlayout.getTabAt(1);
    }

}
