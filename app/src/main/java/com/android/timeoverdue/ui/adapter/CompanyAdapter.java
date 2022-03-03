package com.android.timeoverdue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobCompany;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BmobCompany> mDatas;

    public CompanyAdapter(Context context, List<BmobCompany> list){
        this.mContext = context;
        this.mDatas = list;
    }

    public void removePosition(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mDatas.size());
    }

    public void setData(List<BmobCompany> list){
        mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_company, parent,false);
        return new CompanyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CompanyHolder companyHolder = (CompanyHolder) holder;
        companyHolder.tvCompany.setText(mDatas.get(position).getName());
        if (mDatas.get(position).getSystem()){
            companyHolder.ivDel.setVisibility(View.GONE);
        }else {
            companyHolder.ivDel.setVisibility(View.VISIBLE);
        }
        //item点击事件
        companyHolder.itemView.setOnClickListener(v->{
            if (listener!= null) {
                listener.onClick(position);
            }
        });
        //item每个删除按钮点击事件
        companyHolder.ivDel.setOnClickListener(v->{
            if (delClickListener != null) {
                delClickListener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size() != 0 ? mDatas.size() : 0;
    }

    public class CompanyHolder extends RecyclerView.ViewHolder{
        public TextView tvCompany;
        public ImageView ivDel;

        public CompanyHolder(View itemView) {
            super(itemView);

            tvCompany = (TextView) itemView.findViewById(R.id.tv_company);
            ivDel = itemView.findViewById(R.id.iv_del);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private ClassifyAdapter.OnItemClickListener listener = null;

    public void setOnItemClickListener(ClassifyAdapter.OnItemClickListener onItemClickListener) {
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
