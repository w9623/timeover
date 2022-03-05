package com.android.timeoverdue.ui.activity;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobReminderDays;
import com.android.timeoverdue.bean.BmobTimeReminder;
import com.android.timeoverdue.databinding.ActivityEditTimeReminderBinding;
import com.android.timeoverdue.ui.adapter.EditTimeReminderAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class EditTimeReminderActivity extends BaseActivity<ActivityEditTimeReminderBinding> {

    private List<BmobReminderDays> list;
    private EditTimeReminderAdapter editTimeReminderAdapter;
    private String classifyName;
    private String reminderTime;
    private List<String> map = new ArrayList<String>();

    @Override
    protected void initData() {
        classifyName = getIntent().getStringExtra("classifyName");
        reminderTime = getIntent().getStringExtra("reminderTime");
        if (!reminderTime.equals("W2IJKKKU")){
            List<String> strList = Arrays.asList(reminderTime.split(","));
            for (String str : strList){
                map.add(str);
                Log.e("getlist",str);
            }
        }
        getEditReminderTimeList();
    }

    @Override
    protected void initView() {
        viewBinding.includeTop.tvTitle.setText("编辑提醒天数");
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvSave.setVisibility(View.VISIBLE);
        viewBinding.includeTop.tvSave.setText("保存");
    }

    @Override
    protected void onClick() {
        //返回按钮点击事件
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //自定义天数按钮点击事件
        viewBinding.llSetTime.setOnClickListener(v->{

        });
    }

    private void getEditReminderTimeList(){
        BmobQuery<BmobReminderDays> query = new BmobQuery<BmobReminderDays>();
        query.order("timeSorting");
        query.findObjects(new FindListener<BmobReminderDays>() {
            @Override
            public void done(List<BmobReminderDays> data, BmobException e) {
                if (e == null){
                    list = data;
                    if (editTimeReminderAdapter == null){
                        if (!reminderTime.equals("W2IJKKKU")){
                            editTimeReminderAdapter = new EditTimeReminderAdapter(mContext,data,map);
                        }else {
                            editTimeReminderAdapter = new EditTimeReminderAdapter(mContext,data);
                        }
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(editTimeReminderAdapter);
                    }
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.clear();
    }
}
