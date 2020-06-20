package com.example.tsinghuahelp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tsinghuahelp.Fragment.*;

import com.example.tsinghuahelp.utils.Global;
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
    Fragment fragment3teacher = new Fragment3Teacher();
    Fragment fragment4 = new Fragment4();
    Fragment fragment5 = new Fragment5();
    Fragment pos3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_template);

        // 自动绑定view
        ButterKnife.bind(this);

        FragmentTransaction transaction = fm.beginTransaction();


        if(Global.TYPE == false){  // student
            transaction.add(R.id.content, fragment1);
            transaction.add(R.id.content, fragment2);
            transaction.add(R.id.content, fragment3);  // post page for student
            transaction.add(R.id.content, fragment4);
            transaction.add(R.id.content, fragment5);
        }
        else{
            transaction.add(R.id.content, fragment1);
            transaction.add(R.id.content, fragment2);
            transaction.add(R.id.content, fragment3teacher);
            transaction.add(R.id.content, fragment4);
            transaction.add(R.id.content, fragment5);
        }

        navigationMenu.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction trans = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation1:
                    trans.hide(fragment2).hide(fragment3teacher).
                            hide(fragment4).hide(fragment5).show(fragment1).commit();
                    return true;
                case R.id.navigation2:
                    trans.hide(fragment1).hide(fragment3teacher).
                            hide(fragment4).hide(fragment5).show(fragment2).commit();
                    return true;
                case R.id.navigation3:
                    if(Global.TYPE == false)
                        trans.hide(fragment2).hide(fragment1).
                                hide(fragment4).hide(fragment5).show(fragment3).commit();
                    else
                        trans.hide(fragment2).hide(fragment1).
                                hide(fragment4).hide(fragment5).show(fragment3teacher).commit();
                    return true;
                case R.id.navigation4:
                    trans.hide(fragment2).hide(fragment3teacher).
                            hide(fragment1).hide(fragment5).show(fragment4).commit();
                    return true;
                case R.id.navigation5:
                    trans.hide(fragment2).hide(fragment3teacher).
                            hide(fragment4).hide(fragment1).show(fragment5).commit();
                    return true;
            }
            return false;
        });

    }
}
