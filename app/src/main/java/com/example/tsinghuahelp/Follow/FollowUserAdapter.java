package com.example.tsinghuahelp.Follow;


import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tsinghuahelp.PersonInfo.OtherUserHomeActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.utils.SetImageByUrl;

import java.util.List;

public class FollowUserAdapter extends RecyclerView.Adapter<FollowUserAdapter.FollowUserViewHolder> {
    private Context mCtx;
    private List<FollowUser> userList;

    public FollowUserAdapter(Context mCtx, List<FollowUser> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public FollowUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.follow_layout,null);
        FollowUserViewHolder holder = new FollowUserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowUserViewHolder holder, int position) {
        FollowUser user = userList.get(position);
        SetImageByUrl getImageByUrl = new SetImageByUrl();

        String url = user.getIconUrl();
        getImageByUrl.setImage(holder.icon,url);
        holder.followName.setText(user.getFollowUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class FollowUserViewHolder extends RecyclerView.ViewHolder{

        com.mikhaellopez.circularimageview.CircularImageView icon;
        TextView followName;

        public FollowUserViewHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.follow_pic);
            followName=itemView.findViewById(R.id.follow_name);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                FollowUser result = userList.get(pos);
                int id = result.getId();
                System.out.println("去往个人主页详情页面");
                Intent detailIntent = new Intent(mCtx, OtherUserHomeActivity.class);//ActivityDetail.class
                detailIntent.putExtra("id",result.getId());
                mCtx.startActivity(detailIntent);

            });
        }
    }
}
