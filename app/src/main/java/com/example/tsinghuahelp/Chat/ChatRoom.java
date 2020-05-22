package com.example.tsinghuahelp.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsinghuahelp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {
    List<MessageChatModel> messageChatModelList =  new ArrayList<>();
    RecyclerView recyclerView;
    MessageChatAdapter adapter ;

    EditText messageET;
    ImageView sendBtn;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_room);

        messageET = (EditText)findViewById(R.id.messageET);
        sendBtn = (ImageView) findViewById(R.id.sendBtn);
        username = (TextView) findViewById(R.id.chat_username);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(ChatRoom.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        Intent intent = this.getIntent();

        username.setText(intent.getStringExtra("title"));

        MessageChatModel model1 = new MessageChatModel(
                "Hello. How are you today?",
                "10:00 PM",
                0
        );
        MessageChatModel model2 = new MessageChatModel(
                "Hey! I'm fine. Thanks for asking!",
                "10:00 PM",
                1
        );
        MessageChatModel model3 = new MessageChatModel(
                "Sweet! So, what do you wanna do today?",
                "10:00 PM",
                0
        );
        MessageChatModel model4 = new MessageChatModel(
                "Nah, I dunno. Play soccer.. or learn more coding perhaps?",
                "10:00 PM",
                1
        );


        messageChatModelList.add(model1);
        messageChatModelList.add(model2);
        messageChatModelList.add(model3);
        messageChatModelList.add(model4);
        messageChatModelList.add(model1);
        messageChatModelList.add(model2);
        messageChatModelList.add(model3);
        messageChatModelList.add(model4);
        messageChatModelList.add(model1);
        messageChatModelList.add(model2);
        messageChatModelList.add(model3);
        messageChatModelList.add(model4);

        recyclerView.smoothScrollToPosition(messageChatModelList.size());
        adapter = new MessageChatAdapter(messageChatModelList, ChatRoom.this );
        recyclerView.setAdapter(adapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageET.getText().toString();

                MessageChatModel model = new MessageChatModel(
                        msg,
                        "10:00 PM",
                        0
                );
                messageChatModelList.add(model);
                recyclerView.smoothScrollToPosition(messageChatModelList.size());
                adapter.notifyDataSetChanged();
                messageET.setText("");


            }
        });

    }
}
