package com.example.tsinghuahelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.tsinghuahelp.Adapter.MyFragmentAdapter;
import com.example.tsinghuahelp.Fragment.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class mainPage extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.navigation)
    BottomNavigationView navigationMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        // 自动绑定view
        ButterKnife.bind(this);

        // 以下为fragment相关设置
        FragmentManager fm = getSupportFragmentManager();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());
        fragments.add(new Fragment5());

        viewPager.setAdapter(new MyFragmentAdapter(fm, fragments));

        navigationMenu.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation1:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation2:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation3:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation4:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.navigation5:
                    viewPager.setCurrentItem(4);
                    return true;
            }
            return false;
        });
        // 以上为fragment相关设置

    }
}
