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
import com.android.timeoverdue.bean.BmobReminderDays;
import com.android.timeoverdue.bean.BmobTimeReminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTimeReminderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<BmobReminderDays> mDatas;
    private List<String> map = new ArrayList<String>();

    public EditTimeReminderAdapter(Context context, List<BmobReminderDays> datas,List<String> map){
        this.mContext = context;
        this.mDatas = datas;
        this.map = map;
    }

    public EditTimeReminderAdapter(Context context, List<BmobReminderDays> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    public void removePosition(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position,mDatas.size());
    }

    public void setData(List<BmobReminderDays> list,List<String> map){
        mDatas.clear();
        this.map.clear();
        this.mDatas = list;
        this.map = map;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_edit_time_reminder, parent,false);
        return new EditTimeReminderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EditTimeReminderHolder editTimeReminderHolder = (EditTimeReminderHolder) holder;
        editTimeReminderHolder.tvReminderTime.setText(mDatas.get(position).getName());
        if (!map.isEmpty() && map != null && map.size() > 0){
            for (String str : map){
                if (mDatas.get(position).getName().equals(str)){
                    editTimeReminderHolder.ivChoice.setVisibility(View.VISIBLE);
                }else {
                    editTimeReminderHolder.ivChoice.setVisibility(View.GONE);
                }
            }
        }
        editTimeReminderHolder.itemView.setOnClickListener(v->{
            if (editTimeReminderHolder.ivChoice.getVisibility() == View.VISIBLE){
                editTimeReminderHolder.ivChoice.setVisibility(View.GONE);
                map.remove(mDatas.get(position).getName());
            }else if (editTimeReminderHolder.ivChoice.getVisibility() == View.GONE){
                editTimeReminderHolder.ivChoice.setVisibility(View.VISIBLE);
                map.add(mDatas.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size() != 0 ? mDatas.size() : 0;
    }

    public class EditTimeReminderHolder extends RecyclerView.ViewHolder{
        public TextView tvReminderTime;
        public ImageView ivChoice;

        public EditTimeReminderHolder(View itemView) {
            super(itemView);

            tvReminderTime = (TextView) itemView.findViewById(R.id.tv_reminder_time);
            ivChoice = itemView.findViewById(R.id.iv_choice);
        }
    }

}
