package com.example.tsinghuahelp.Adapter;

//public class InfoAdapter {
//}
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tsinghuahelp.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.InfoViewHolder> {
    private Context mCtx;
    private List<String> resultsList;
    private OnitemClick onitemClick;

    public interface OnitemClick {
        void onItemClick(int position);
    }

    public void setOnitemClickLintener (OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    public InfoAdapter(Context mCtx, List<String> resultsList) {
        this.mCtx = mCtx;
        this.resultsList = resultsList;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.personal_info_list,null);
        InfoViewHolder holder = new InfoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {
        String result = resultsList.get(position);
        holder.info.setText(result);
        if (onitemClick != null) {
            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //在TextView的地方进行监听点击事件，并且实现接口
                    onitemClick.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }


    class InfoViewHolder extends RecyclerView.ViewHolder{

        public TextView info;

        public InfoViewHolder(View itemView) {
            super(itemView);
           info=itemView.findViewById(R.id.personal_info_show);

        }
    }
}
