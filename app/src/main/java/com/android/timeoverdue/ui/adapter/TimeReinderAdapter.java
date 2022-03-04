package com.android.timeoverdue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobTimeReinder;

import java.util.List;

import cn.bmob.v3.BmobQuery;

public class TimeReinderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BmobTimeReinder> mDatas;

    public TimeReinderAdapter(Context context, List<BmobTimeReinder> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    public void removePosition(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mDatas.size());
    }

    public void setData(List<BmobTimeReinder> list){
        mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_time_remind, parent,false);
        return new TimeReinderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimeReinderHolder timeReinderHolder = (TimeReinderHolder) holder;
        timeReinderHolder.tvClassify.setText(mDatas.get(position).getClassifyName());
        timeReinderHolder.tvTimeRemind.setText(mDatas.get(position).getReinderDays());
        if (mDatas.get(position).getReinderDays().equals("W2IJKKKU")){

        }
        timeReinderHolder.btnSet.setOnClickListener(v->{
            if (listener != null){
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size() != 0 ? mDatas.size() : 0;
    }

    public class TimeReinderHolder extends RecyclerView.ViewHolder{
        public TextView tvClassify,tvTimeRemind;
        public Button btnSet;

        public TimeReinderHolder(View itemView) {
            super(itemView);

            tvClassify = (TextView) itemView.findViewById(R.id.tv_classify);
            tvTimeRemind = itemView.findViewById(R.id.tv_time_remind);
            btnSet = itemView.findViewById(R.id.btn_set);
        }
    }

    public interface OnSetItemClickListener {
        void onClick(int position);
    }

    private OnSetItemClickListener listener = null;

    public void setOnItemClickListener(OnSetItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    private void getReinderTime(String id){
        BmobQuery<BmobTimeReinder> query = new BmobQuery<BmobTimeReinder>();
//        query.addWhereEqualTo("")
    }
}
