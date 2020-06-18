package com.example.tsinghuahelp.Fragment;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tsinghuahelp.Adapter.InfoAdapter;
import com.example.tsinghuahelp.PersonInfo.EditInfoActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.example.tsinghuahelp.PersonInfo.StarFollowAll;
import com.example.tsinghuahelp.mainPage;
import com.example.tsinghuahelp.utils.CommonInterface;
import com.example.tsinghuahelp.utils.MyDialog;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    public static boolean change=false;
    Button btn_edit;
    Button btn_verify;
    TextView edit_user;
    TextView edit_signature;
    TextView is_verify;
    com.mikhaellopez.circularimageview.CircularImageView edit_pic;
    TextView num_star_or_proj;
    TextView num_follow;
    TextView num_follower;
    Bitmap bitmap;
    int user_id;


    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case 0:
                     Toast.makeText(getContext(),"后端信息获取失败",Toast.LENGTH_SHORT).show();
                     break;
                 case 1:
                     Log.e("m_tag","收到信息更新页");
                     if(verify){is_verify.setText("已验证");}
                     else{is_verify.setText("未验证");}
                     edit_user.setText(name);
                     edit_signature.setText(signature);
                     num_follow.setText(follow_num);
                     num_star_or_proj.setText(star_pro_num);
                     num_follower.setText(follower_num);
                     infoList.clear();
                     if(person_info==null||person_info.length()==0){infoList.add("无");}
                     else{infoList.add(person_info);}
                     infoAdapter.notifyDataSetChanged();
                     break;
                 case 2:
                     Log.e("m_tag","收到信息更新图片");
                     edit_pic.setImageBitmap(bitmap);
                     break;
                 case 3:
                     Log.e("m_tag","收到我的项目更新");
                     proAdapter.notifyDataSetChanged();
                     break;
                 case 4:
                     Log.e("m_tag","收到我的计划更新");
                     planAdapter.notifyDataSetChanged();
                     break;

             }
            }
    };

    public Fragment5() {
        // Required empty public constructor
    }

    @Override
    public void onResume(){
        super.onResume();
        if(change){
            fresh_page();
            change=false;
        }
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

        fresh_page();

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.layout_divider_vertical));


        if(!mainPage.type){
            tabLayout.addTab(tabLayout.newTab().setText("关于我"));
            tabLayout.addTab(tabLayout.newTab().setText("我的报名"));
            tabLayout.addTab(tabLayout.newTab().setText("我的发文"));
            starORpro.setText("Star");
        }
        else{
            tabLayout.addTab(tabLayout.newTab().setText("关于我"));
            tabLayout.addTab(tabLayout.newTab().setText("我的项目"));
            starORpro.setText("Project");
        }


        infoList = new ArrayList<>();



        proList = new ArrayList<>();

        fresh_prolist();

        planList = new ArrayList<>();

        if(!mainPage.type){fresh_planlist();}

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
                change=true;
                Intent intent=new Intent(getContext(),EditInfoActivity.class);
                intent.putExtra("icon_url",icon_url);
                intent.putExtra("id",user_id);
                intent.putExtra("name",name);
                intent.putExtra("real_name",real_name);
                intent.putExtra("school",school);
                intent.putExtra("department",department);
                intent.putExtra("grade",grade);
                intent.putExtra("signature",signature);
                intent.putExtra("verify",verify);
                intent.putExtra("person_info",person_info);
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

    private void fresh_page(){
        CommonInterface.sendOkHttpGetRequest("/api/user/home", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = JSONObject.parseObject(resStr);
                    JSONObject data=jsonObject.getJSONObject("data");
                    icon_url = data.getString("icon_url");
                    name=data.getString("username");
                    real_name=data.getString("real_name");
                    school=data.getString("school");
                    department=data.getString("department");
                    grade=data.getString("grade");
                    signature=data.getString("signature");
                    person_info=data.getString("personal_info");
                    user_id=data.getIntValue("id");
                    verify=data.getBoolean("verification");
                    follow_num=data.getInteger("follow_num").toString();
                    star_pro_num=data.getInteger("star_or_pro_num").toString();
                    follower_num=data.getInteger("followee_num").toString();

                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);
                    fresh_icon();
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=0;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void fresh_icon(){
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
                    if (connection.getResponseCode()==200){
                        InputStream in = connection.getInputStream();
                        bitmap = BitmapFactory.decodeStream(in);
                        Message message=new Message();
                        message.what=2;
                        mHandler.sendMessage(message);
                    }
                    else{
                        Message message=new Message();
                        message.what=0;
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void fresh_prolist(){
        proList.clear();
        String url="/api/student/get_my_project";
        if(mainPage.type){url="/api/teacher/get_my_project";}
        CommonInterface.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = JSONObject.parseObject(resStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.size();i++){
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        String o_title = object.getString("title");
                        String o_teacher = object.getString("teacher");
                        String o_department = object.getString("department");
                        String o_description=object.getString("description");
                        int o_id = object.getInteger("id");
                        proList.add(new SearchResult(o_title,o_teacher,
                                o_department, o_description,"project",o_id));
                    }

//                    proList.add(new SearchResult("移动应用与开发","王老师",
//                            "软件学院", "巨难无比，请谨慎选课","project",0));


                    Message message=new Message();
                    message.what=3;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=0;
                    mHandler.sendMessage(message);
                }
            }
        });

    }

    private void fresh_planlist(){
        planList.clear();
        String url="/api/student/get_my_plan";
        CommonInterface.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resStr = response.body().string();
                Log.e("response", resStr);
                try {
                    // 解析json，然后进行自己的内部逻辑处理
                    JSONObject jsonObject = JSONObject.parseObject(resStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i=0;i<jsonArray.size();i++){
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        String o_title = object.getString("title");
                        String o_student = object.getString("student");
                        String o_department = object.getString("department");
                        String o_description=object.getString("description");
                        int o_id = object.getInteger("id");
                        planList.add(new SearchResult(o_title,o_student,
                                o_department, o_description,"plan",o_id));
                    }

//                    planList.add(new SearchResult("想一台智能机械小车","刘薇",
//                            "软件学院", "嵌入式课程需要orz","plan",0));


                    Message message=new Message();
                    message.what=4;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=0;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

}
