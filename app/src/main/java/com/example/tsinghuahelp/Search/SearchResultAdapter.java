package com.example.tsinghuahelp.Search;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tsinghuahelp.DetailActivityTeacher;
import com.example.tsinghuahelp.PersonInfo.OtherUserHomeActivity;
import com.example.tsinghuahelp.R;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {
    private Context mCtx;
    private List<SearchResult> resultsList;

    public SearchResultAdapter(Context mCtx, List<SearchResult> resultsList) {
        this.mCtx = mCtx;
        this.resultsList = resultsList;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_layout,null);
        SearchResultViewHolder holder = new SearchResultViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        SearchResult result = resultsList.get(position);
        holder.title.setText(result.getTitle());
        holder.teacherOrStudent.setText(result.getTeacherOrStudent());
        holder.department.setText(result.getDepartment());
        holder.descriptionOrPersonalInfo.setText(result.getDescriptionOrPersonalInfo());
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder{

        TextView title, teacherOrStudent, department, descriptionOrPersonalInfo;

        public SearchResultViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textViewTitle);
            teacherOrStudent = itemView.findViewById(R.id.textViewTeacher);
            department = itemView.findViewById(R.id.textViewDepartment);
            descriptionOrPersonalInfo = itemView.findViewById(R.id.textViewResearchField);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                SearchResult result = resultsList.get(pos);
                String type=result.getType();
                int id = result.getId();
                if(type.equals("project")){
                    //去往项目详情页面
                    System.out.println("去往项目详情页面");
                    Intent detailIntent = new Intent(mCtx, DetailActivityTeacher.class);//ActivityDetail.class
                    detailIntent.putExtra("title",result.getTitle());
                    mCtx.startActivity(detailIntent);
                }
                else if(type.equals("plan")){
                    //去往计划详情页面
                    System.out.println("去往计划详情页面");
                }
                else{
                    //去往个人主页详情页面
                    System.out.println("去往个人主页详情页面");
                    Intent detailIntent = new Intent(mCtx, OtherUserHomeActivity.class);//ActivityDetail.class
//                    detailIntent.putExtra("title",result.getTitle());
                    mCtx.startActivity(detailIntent);
                }
            });
        }
    }
}
