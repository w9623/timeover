package com.android.timeoverdue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.bean.BmobCommonProblem;

import java.util.List;

public class CommonProblemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<BmobCommonProblem> mDatas;

    public CommonProblemAdapter(Context context,List<BmobCommonProblem> list){
        this.mContext = context;
        this.mDatas = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_common_problem, parent,false);
        return new CommonProblemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommonProblemHolder commonProblemHolder = (CommonProblemHolder) holder;
        commonProblemHolder.tvProblem.setText(mDatas.get(position).getProblem());
        commonProblemHolder.tvAnswer.setText(mDatas.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class CommonProblemHolder extends RecyclerView.ViewHolder{

        private TextView tvProblem,tvAnswer;

        public CommonProblemHolder(@NonNull View itemView) {
            super(itemView);

            tvProblem = itemView.findViewById(R.id.tv_problem);
            tvAnswer = itemView.findViewById(R.id.tV_answer);
        }
    }
}
