package com.example.tsinghuahelp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.Search.SearchResult;
import com.example.tsinghuahelp.Search.SearchResultAdapter;
import com.example.tsinghuahelp.mainPage;
import com.example.tsinghuahelp.news.PostAdapter;
import com.example.tsinghuahelp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment2 extends Fragment {

    View mView;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private SearchResultAdapter adapter;
    private List<SearchResult> resultsList;
    private String choose = "All";
    TextView select_txt;
    TextView search_txt;
    public Fragment2() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(getContext(),"后端信息获取失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Log.e("m_tag","收到更新");
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        resultsList = new ArrayList<>();
        if(!mainPage.type) {
            get_recommend();
        }

//        resultsList.add(new SearchResult("移动应用与开发","王老师",
//                "软件学院", "巨难无比，请谨慎选课","project",0));

    }

    private void get_recommend() {
        resultsList.clear();
        String url="/api/user/recommend";
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
                        String o_teacher = object.getString("name");
                        String o_department = object.getString("department");
                        String o_description=object.getString("description");
                        String o_type = object.getString("type");
                        int o_id = object.getInteger("id");
                        resultsList.add(new SearchResult(o_title,o_teacher,
                                o_department, o_description,o_type,o_id));
                    }

                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=0;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_search2, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);
        recyclerView = mView.findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_txt=mView.findViewById(R.id.search_et_input);

        adapter = new SearchResultAdapter(getContext(),resultsList);
        recyclerView.setAdapter(adapter);
        spinner = (Spinner) mView.findViewById(R.id.search_spinner);
        select_txt=mView.findViewById(R.id.spinner_select_txt);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //拿到被选择项的值
                choose = (String) spinner.getSelectedItem();
                select_txt.setText(choose);
                Log.e("m","选择了"+choose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button button=mView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                search();
            }
        });

        return mView;
    }

    private void search() {
        String key_word=search_txt.getText().toString();
        String type=select_txt.getText().toString();
        HashMap<String,String> h = new HashMap<>();
        h.put("key_word",key_word);
        h.put("type",type);
        resultsList.clear();
        String url="/api/user/search";
        CommonInterface.sendOkHttpPostRequest(url, new Callback() {
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
                        String o_teacher = object.getString("name");
                        String o_department = object.getString("department");
                        String o_description=object.getString("description");
                        String o_type = object.getString("type");
                        int o_id = object.getInteger("id");
                        resultsList.add(new SearchResult(o_title,o_teacher,
                                o_department, o_description,o_type,o_id));
                    }

                    Message message=new Message();
                    message.what=1;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    Message message=new Message();
                    message.what=0;
                    mHandler.sendMessage(message);
                }
            }
        },h);
    }

}
