package com.example.tsinghuahelp.news;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tsinghuahelp.DetailActivity;
import com.example.tsinghuahelp.DetailActivityTeacher;
import com.example.tsinghuahelp.MainActivity;
import com.example.tsinghuahelp.R;
import com.example.tsinghuahelp.RegisterActivity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mCtx;
    private List<Posts> postsList;

    public PostAdapter(Context mCtx, List<Posts> postsList) {
        this.mCtx = mCtx;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout,null);
        PostViewHolder holder = new PostViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Posts posts = postsList.get(position);
        holder.title.setText(posts.getTitle());
        holder.teacher.setText(posts.getTeacher());
        holder.department.setText(posts.getDepartment());
        holder.researchDirection.setText(posts.getResearchDirection());
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        TextView title, department, researchDirection, teacher;

        public PostViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewTitle);
            department = itemView.findViewById(R.id.textViewDepartment);
            researchDirection = itemView.findViewById(R.id.textViewResearchField);
            teacher = itemView.findViewById(R.id.textViewTeacher);



            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Posts post = postsList.get(pos);

                    Intent detailIntent = new Intent(mCtx, DetailActivityTeacher.class);//ActivityDetail.class
                    detailIntent.putExtra("title",post.getTitle());
                    detailIntent.putExtra("tchid",post.getTchID());
                    detailIntent.putExtra("id",post.getID());
                    mCtx.startActivity(detailIntent);

                }
            });
        }
    }
}
