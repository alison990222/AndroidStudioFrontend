package com.example.tsinghuahelp.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.tsinghuahelp.Adapter.InfoAdapter;
import com.example.tsinghuahelp.PersonInfo.EditInfoActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.example.tsinghuahelp.StarFollowAll;
import com.example.tsinghuahelp.utils.MyDialog;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;

public class Fragment5 extends Fragment implements View.OnClickListener{
    View mView;
    TabLayout tabLayout;
    RecyclerView myRecyclerView;
    InfoAdapter infoAdapter;
    SearchResultAdapter proAdapter;
    SearchResultAdapter planAdapter;
    private List<String> infoList;
    private List<SearchResult> proList;
    private List<SearchResult> planList;
    String icon_url;
    String name;
    String real_name;
    String school;
    String department;
    String grade;
    String signature;
    String star_pro_num;
    String follow_num;
    String follower_num;
    String person_info;
    boolean verify;
    Button btn_edit;
    Button btn_verify;
    TextView edit_user;
    TextView edit_signature;
    TextView is_verify;
    com.mikhaellopez.circularimageview.CircularImageView edit_pic;
    TextView num_star_or_proj;
    TextView num_follow;
    TextView num_follower;


    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) { // TODO Auto-generated method stub
            // super.handleMessage(msg);
             Log.e("m_tag","收到信息度1");
            Bitmap bitmap=(Bitmap) msg.obj;
            edit_pic.setImageBitmap(bitmap);
            }
    };

    public Fragment5() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();
        icon_url = "http://a2.att.hudong.com/36/48/19300001357258133412489354717.jpg";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(icon_url);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestProperty("charset","UTF-8");
                    StringBuilder s = new StringBuilder();
                    String str;
                    Bitmap bitmap1;
                    if (connection.getResponseCode()==200){
                        InputStream in = connection.getInputStream();
                        bitmap1 = BitmapFactory.decodeStream(in);
                        System.out.println("返回200");
                        Message message=new Message();
                        message.obj=bitmap1;
                        assert bitmap1 != null;
                        System.out.println(bitmap1.toString());
                        //ivImage.setImageBitmap(bitmap);
                        mHandler.sendMessage(message);
                    }
                    System.out.println("没返回返回200");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        name="lw";
        real_name="真实姓名";
        school="Tsinghua";
        department="Software";
        grade="本科";
        signature="小说真好看";
        verify=true;
        follow_num="1";
        star_pro_num="2";
        follower_num="10";
        person_info="红红火火恍恍惚惚\n哈哈哈哈哈哈哈哈哈哈哈哈哈\n这里都是乱写的";
        is_verify.setText("已验证");
        edit_user.setText(name);
        edit_signature.setText(signature);
        num_follow.setText(follow_num);
        num_star_or_proj.setText(star_pro_num);
        num_follower.setText(follower_num);
//        Glide.with(Objects.requireNonNull(getContext())).load(icon_url).into(edit_pic);
        infoList.clear();
        infoList.add(person_info);
        infoAdapter.notifyDataSetChanged();
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

        btn_edit=mView.findViewById(R.id.edit_info_btn);
        is_verify=mView.findViewById(R.id.verify_info);
        edit_user=mView.findViewById(R.id.info_username);
        num_star_or_proj=mView.findViewById(R.id.star_or_proj_num);
        num_follow=mView.findViewById(R.id.my_follow_num);
        num_follower=mView.findViewById(R.id.my_follower_num);

        edit_pic=mView.findViewById(R.id.info_pic);
        edit_signature=mView.findViewById(R.id.info_signature);
        btn_verify=mView.findViewById(R.id.verify_btn);
        btn_verify.setOnClickListener(this);
        btn_edit.setOnClickListener(this);

        mView.findViewById(R.id.follow_num).setOnClickListener(this);
        mView.findViewById(R.id.follower_num).setOnClickListener(this);
        mView.findViewById(R.id.pro_num).setOnClickListener(this);


        //根据老师和学生信息判断，这里需要从后端提供数据，个人信息，我的报名，我的发文之类的
        //关于查看别人的主页，建议新建一个activity利用这个布局文件
//        icon_url = "http://a2.att.hudong.com/36/48/19300001357258133412489354717.jpg";
//        name="lw";
//        real_name="真实姓名";
//        school="Tsinghua";
//        department="Software";
//        grade="本科";
//        signature="小说真好看";
//        verify=true;
//        follow_num="1";
//        star_pro_num="2";
//        follower_num="10";
//        person_info="红红火火恍恍惚惚\n哈哈哈哈哈哈哈哈哈哈哈哈哈\n这里都是乱写的";
//        is_verify.setText("已验证");
//        edit_user.setText(name);
//        edit_signature.setText(signature);
//        num_follow.setText(follow_num);
//        num_star_or_proj.setText(star_pro_num);
//        num_follower.setText(follower_num);
//        infoList.clear();
//        infoList.add(person_info);
//        infoAdapter.notifyDataSetChanged();
//

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_info_btn:
                Intent intent=new Intent(getContext(),EditInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.verify_btn:
                showVerifyInfo();
                break;
            case R.id.pro_num:
                System.out.println("hi,pro_num");
                Intent proIntent = new Intent(getContext(), StarFollowAll.class);
                proIntent.putExtra("type","star");
                Objects.requireNonNull(getContext()).startActivity(proIntent);
                break;
            case R.id.follow_num:
                System.out.println("hi,follow_num");
                Intent followIntent = new Intent(getContext(), StarFollowAll.class);
                followIntent.putExtra("type","follow");
                Objects.requireNonNull(getContext()).startActivity(followIntent);
                break;
            case R.id.follower_num:
                System.out.println("hi,follower_num");
                Intent followerIntent = new Intent(getContext(), StarFollowAll.class);
                followerIntent.putExtra("type","follower");
                Objects.requireNonNull(getContext()).startActivity(followerIntent);
                break;

        }
    }


    public void showVerifyInfo() {
        String message="姓名："+real_name+"\n学校："+school+"\n院系："+department+"\n年级："+grade;
        final MyDialog dialog = new MyDialog(getContext());
        dialog.setMessage(message)
                .setTitle("验证信息")
                .setSingle(true).setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
                Toast.makeText(getContext(),"xxxx",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNegtiveClick() {
                dialog.dismiss();
                Toast.makeText(getContext(),"ssss",Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

}
