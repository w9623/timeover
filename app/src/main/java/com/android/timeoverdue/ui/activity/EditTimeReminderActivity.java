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
import cn.bmob.v3.listener.UpdateListener;

public class EditTimeReminderActivity extends BaseActivity<ActivityEditTimeReminderBinding> {

    private List<BmobReminderDays> list;
    private EditTimeReminderAdapter editTimeReminderAdapter;
    private String classifyName,mObjectId;
    private String reminderTime;
    private List<String> map = new ArrayList<String>();
    private EditTimeReminderAdapter.OnSetItemClickListener listener = new EditTimeReminderAdapter.OnSetItemClickListener() {
        @Override
        public void onClick(int position) {
            //如果没有选择任何提醒天数，则显示选择了不提醒
            if (editTimeReminderAdapter.getList().get(0).equals("不提醒")){
                viewBinding.ivChoice.setVisibility(View.VISIBLE);
            }else {
                viewBinding.ivChoice.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void initData() {
        classifyName = getIntent().getStringExtra("classifyName");
        reminderTime = getIntent().getStringExtra("reminderTime");
        mObjectId = getIntent().getStringExtra("mObjectId");
        if (reminderTime.equals("不提醒")){
            viewBinding.ivChoice.setVisibility(View.VISIBLE);
        }
        if (!reminderTime.equals("W2IJKKKU")){
            List<String> strList = Arrays.asList(reminderTime.split(","));
            for (String str : strList){
                map.add(str);
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
        if (!classifyName.equals("默认") && !reminderTime.equals("W2IJKKKU")){
            viewBinding.ivDel.setVisibility(View.VISIBLE);
        }else {
            viewBinding.ivDel.setVisibility(View.GONE);
        }
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
        //保存按钮点击事件
        viewBinding.includeTop.tvSave.setOnClickListener(v->{
            if (editTimeReminderAdapter != null){
                update();
            }
        });
        //删除按钮点击事件
        viewBinding.ivDel.setOnClickListener(v->{
            editTimeReminderAdapter.clearData();
            update();
        });
        //不提醒项点击事件
        viewBinding.rlNotReminder.setOnClickListener(v->{
            if (viewBinding.ivChoice.getVisibility() == View.VISIBLE) {
                viewBinding.ivChoice.setVisibility(View.GONE);
            }else {
                viewBinding.ivChoice.setVisibility(View.VISIBLE);
            }
            editTimeReminderAdapter.changeTempMap();
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
                        editTimeReminderAdapter.setOnItemClickListener(listener);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
                        viewBinding.recyclerView.setAdapter(editTimeReminderAdapter);
                    }else {
                        editTimeReminderAdapter.setData(data,map);
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

    private void update(){
        String str = "";
        for (int i =0;i<editTimeReminderAdapter.getList().size();i++){
            if (i == editTimeReminderAdapter.getList().size()-1){
                str += editTimeReminderAdapter.getList().get(i);
            }else {
                str += editTimeReminderAdapter.getList().get(i) + ",";
            }
        }
        Log.e(TAG,str);
        BmobTimeReminder bmobTimeReminder = new BmobTimeReminder();
        bmobTimeReminder.setReminderDays(str);
        bmobTimeReminder.update(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    finish();
                }else {
                    Log.e(TAG,e.toString());
                }
            }
        });
    }
}
