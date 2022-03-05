package com.android.timeoverdue.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobTimeReminder;
import com.android.timeoverdue.databinding.ActivityTimeReminderBinding;
import com.android.timeoverdue.ui.adapter.TimeReinderAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TimeReminderActivity extends BaseActivity<ActivityTimeReminderBinding> {

    private TimeReinderAdapter timeReinderAdapter;
    private List<BmobTimeReminder> list;
    private String defaultTimeReminder;
    private TimeReinderAdapter.OnSetItemClickListener listener = new TimeReinderAdapter.OnSetItemClickListener() {
        @Override
        public void onClick(int position) {
            Intent intent = new Intent(TimeReminderActivity.this,EditTimeReminderActivity.class);
            intent.putExtra("classifyName",list.get(position).getClassifyName());
            intent.putExtra("reminderTime",list.get(position).getReminderDays());
            startActivity(intent);
        }
    };

    @Override
    protected void initData() {
        getTimeReminderList();
        getReminderTime("W2IJKKKU");
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvTitle.setText("自定义过期提醒");
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.GONE);
    }

    @Override
    protected void onClick() {
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
    }

    //查询自定义过期提醒列表
    private void getTimeReminderList(){
        BmobQuery<BmobTimeReminder> query = new BmobQuery<BmobTimeReminder>();
        query.order("createAt");
        query.findObjects(new FindListener<BmobTimeReminder>() {
            @Override
            public void done(List<BmobTimeReminder> data, BmobException e) {
                if (e == null){
                    list = data;
                    if (timeReinderAdapter == null){
                        timeReinderAdapter = new TimeReinderAdapter(mContext,data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        timeReinderAdapter.setOnItemClickListener(listener);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(timeReinderAdapter);
                    }else{
                        timeReinderAdapter.setData(list);
                    }
                }else {
                    Log.e(TAG,e.toString());
                }

            }
        });
    }

    private void getReminderTime(String id){
        BmobQuery<BmobTimeReminder> query = new BmobQuery<BmobTimeReminder>();
        query.addWhereEqualTo("objectId",id);
        query.findObjects(new FindListener<BmobTimeReminder>() {
            @Override
            public void done(List<BmobTimeReminder> list, BmobException e) {
                if (e == null ){
                    defaultTimeReminder = list.get(0).getReminderDays();
                }else {
                    Log.e("TimeReminder",e.toString());
                }
            }
        });
    }

}
