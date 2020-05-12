package com.example.tsinghuahelp.CheckUser;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tsinghuahelp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CheckUser extends AppCompatActivity {

    ListView listview;
    private List<String> userList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_applicants);

        userList.add("zxt");
        userList.add("liuwei");
        userList.add("wyq");

        listview = (ListView) findViewById(R.id.listview);

        ListAdapter adapter = new ArrayAdapter<>(this , R.layout.user_layout ,userList);
        listview.setAdapter(adapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CheckUser.this, "點選第 " + (i + 1) + " 個 \n內容：" + userList.get(i), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
