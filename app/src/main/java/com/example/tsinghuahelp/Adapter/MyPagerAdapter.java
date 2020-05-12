package com.example.tsinghuahelp.Adapter;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.RegisterActivity;
import com.example.tsinghuahelp.RegisterTab.RegisterStudent;
import com.example.tsinghuahelp.RegisterTab.RegisterTeacher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import com.example.tsinghuahelp.R;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    private RegisterStudent fragment1 = new RegisterStudent();
    private RegisterTeacher fragment2 = new RegisterTeacher();

    public MyPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fragment1;
            case 1:
                return fragment2;
        }
        return fragment1;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    private String[] mTitles = new String[]{"学生", "老师"};
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}