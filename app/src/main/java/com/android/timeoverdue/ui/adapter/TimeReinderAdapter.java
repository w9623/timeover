package com.android.timeoverdue.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.timeoverdue.R;
import com.android.timeoverdue.bean.BmobTimeReminder;

import java.util.List;

public class TimeReinderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BmobTimeReminder> mDatas;

    public TimeReinderAdapter(Context context, List<BmobTimeReminder> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    public void removePosition(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mDatas.size());
    }

    public void setData(List<BmobTimeReminder> list){
        mDatas.clear();
        this.mDatas = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_time_reminder, parent,false);
        return new TimeReinderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TimeReinderHolder timeReinderHolder = (TimeReinderHolder) holder;
        timeReinderHolder.tvClassify.setText(mDatas.get(position).getClassifyName());
        if (mDatas.get(position).getReminderDays().equals("W2IJKKKU")){
            timeReinderHolder.tvTimeRemind.setText("未设置，将使用默认提醒天数");
        }else {
            timeReinderHolder.tvTimeRemind.setText(mDatas.get(position).getReminderDays());
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


}
