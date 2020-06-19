package com.example.tsinghuahelp.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tsinghuahelp.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder> {
    private List<ChatList> chatList;
    private Context mCtx;

    public ChatListAdapter(List<ChatList> chatList, Context mCtx) {
        this.chatList = chatList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ChatListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.chat_layout,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.Holder holder, int position) {

        ChatList chat = chatList.get(position);
        holder.username.setText(chat.getUsername());
        holder.description.setText(chat.getDescription());
        holder.date.setText(chat.getDate());

        Glide.with(mCtx).load(chat.getUrlProfile()).into(holder.profile);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView username, date, description;
        private CircularImageView profile;

        public Holder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.msg_username);
            date = itemView.findViewById(R.id.msg_date);
            description = itemView.findViewById(R.id.msg_description);
            profile = itemView.findViewById(R.id.msg_pic);

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    ChatList chat = chatList.get(pos);

                    Intent chatIntent = new Intent(mCtx, ChatRoom.class);
                    chatIntent.putExtra("title",chat.getUsername());
                    chatIntent.putExtra("id",chat.getUserID());
                    mCtx.startActivity(chatIntent);

                }
            });
        }
    }

}
