package com.example.tsinghuahelp.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.tsinghuahelp.Chat.ChatList;
import com.example.tsinghuahelp.Chat.ChatListAdapter;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.news.Posts;
import com.example.tsinghuahelp.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment4 extends Fragment {

    private View mView;

    private List<ChatList> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    public Fragment4() {
        // Required empty public constructor
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Log.d("error","backend not connected");
                    break;
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        list.add(new ChatList("zxt","i don't have description","2020/5/1","",""));
//        list.add(new ChatList("wyq","send me the code","2020/5/2","",""));
//        list.add(new ChatList("lw","ok","2020/5/5","",""));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_msg4, container, false);
        // 绑定视图
        ButterKnife.bind(this, mView);



        recyclerView = mView.findViewById(R.id.recyclerView4);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fresh_page();

        adapter = new ChatListAdapter(list,getContext());
        recyclerView.setAdapter(adapter);


        return mView;
    }

    private void fresh_page(){
        list.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonInterface.sendOkHttpGetRequest("/api/chat/get_chatter/", new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e("error", e.toString());
                        Message message = new Message();
                        message.what = 0;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resStr = response.body().string();
                        Log.d("chatdata", resStr);
                        try {
                            JSONObject jsonObject = JSONObject.parseObject(resStr);
                            JSONArray data = jsonObject.getJSONArray("data");

                            list.clear();
                            for (int i = 0; i < data.size(); i++) {
                                JSONObject object = (JSONObject) data.get(i);
                                list.add(new ChatList(
                                        object.get("name").toString(),
                                        object.get("latest_content").toString(),
                                        object.get("time").toString(),
                                        "",
                                        object.get("from_id").toString(),
                                        object.getBoolean("real_all")));
                            }
                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);

                        } catch (Exception e) {
                            Log.e("error", e.toString());
                            Message message = new Message();
                            message.what = 0;
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
        }).start();

    }

}
