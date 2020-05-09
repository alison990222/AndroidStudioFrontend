package com.example.tsinghuahelp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tsinghuahelp.Fragment.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentNavbar  extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigationMenu;

    FragmentManager fm = getSupportFragmentManager();

    Fragment fragment1 = new Fragment1();
    Fragment fragment2 = new Fragment2();
    Fragment fragment3 = new Fragment3();
    Fragment fragment4 = new Fragment4();
    Fragment fragment5 = new Fragment5();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_template);

        // 自动绑定view
        ButterKnife.bind(this);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, fragment1);
        transaction.add(R.id.content, fragment2);
        transaction.add(R.id.content, fragment3);
        transaction.add(R.id.content, fragment4);
        transaction.add(R.id.content, fragment5);
        transaction.show(fragment1).commit();

        navigationMenu.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction trans = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation1:
                    trans.show(fragment1).commit();
                    return true;
                case R.id.navigation2:
                    trans.show(fragment2).commit();
                    return true;
                case R.id.navigation3:
                    trans.show(fragment3).commit();
                    return true;
                case R.id.navigation4:
                    trans.show(fragment4).commit();
                    return true;
                case R.id.navigation5:
                    trans.show(fragment5).commit();
                    return true;
            }
            return false;
        });

    }
}
