package com.example.tsinghuahelp.Chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.utils.SetImageByUrl;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class MessageChatAdapter extends RecyclerView.Adapter  {
    List<MessageChatModel> messageChatModelList;
    Context context;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public MessageChatAdapter(List<MessageChatModel> messageChatModelList, Context context) {
        this.messageChatModelList = messageChatModelList;
        this.context = context;
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageChatModel message = (MessageChatModel) messageChatModelList.get(position);
        if (message.getViewType() == 0) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
//            Log.e("getItemViewType","1");
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_room_send, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_room_rcv, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessageChatModel message = messageChatModelList.get(position);

        SetImageByUrl getImageByUrl = new SetImageByUrl();

        String url = message.getIconUrl();


        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
//                getImageByUrl.setImage(((SentMessageHolder) holder).profile_image,url);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                getImageByUrl.setImage(((ReceivedMessageHolder) holder).profile_image,url);
        }
    }

    @Override
    public int getItemCount() {
        return messageChatModelList.size();
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder{

        TextView message;
        TextView time;
//        CircularImageView profile_image;


        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.message);
            time = (TextView)itemView.findViewById(R.id.time);
//            profile_image = (CircularImageView)itemView.findViewById(R.id.profile_image);

        }

        void bind(MessageChatModel messageModel) {
            message.setText(messageModel.getText());
            time.setText(messageModel.getTime());

        }

    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView message;
        TextView time;
        CircularImageView profile_image;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            message = (TextView)itemView.findViewById(R.id.message);
            time = (TextView)itemView.findViewById(R.id.time);
            profile_image = (CircularImageView)itemView.findViewById(R.id.profile_image);
        }

        void bind(MessageChatModel messageModel){
            message.setText(messageModel.getText());
            time.setText(messageModel.getTime());


        }
    }
}
