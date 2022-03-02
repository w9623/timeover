package com.android.timeoverdue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.bean.BmobClassify;

import java.util.List;

public class ClassifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BmobClassify> mDatas;

    public ClassifyAdapter(Context context,List<BmobClassify> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    public void removePosition(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mDatas.size());
    }

    public void setData(List<BmobClassify> list){
        mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_classify, parent,false);
        return new ClassifyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ClassifyHolder classifyHolder = (ClassifyHolder) holder;
        classifyHolder.tvClassify.setText(mDatas.get(position).getName());
        if (mDatas.get(position).getSystem()){
            classifyHolder.ivDel.setVisibility(View.GONE);
        }else {
            classifyHolder.ivDel.setVisibility(View.VISIBLE);
        }
        //item点击事件
        classifyHolder.itemView.setOnClickListener(v->{
            if (listener!= null) {
                listener.onClick(position);
            }
        });
        //item每个删除按钮点击事件
        classifyHolder.ivDel.setOnClickListener(v->{
            if (delClickListener != null) {
                delClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size() != 0 ? mDatas.size() : 0;
    }

    public class ClassifyHolder extends RecyclerView.ViewHolder{
        public TextView tvClassify;
        public ImageView ivDel;

        public ClassifyHolder(View itemView) {
            super(itemView);

            tvClassify = (TextView) itemView.findViewById(R.id.tv_classify);
            ivDel = itemView.findViewById(R.id.iv_del);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public interface DelClickListener{
        void onClick(int position);
    }

    private DelClickListener delClickListener = null;

    public void setDelClickListener(DelClickListener delClickListener){
        this.delClickListener = delClickListener;
    }

}
