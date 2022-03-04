package com.android.timeoverdue.ui.activity;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.bean.BmobClassify;
import com.android.timeoverdue.bean.BmobTimeReinder;
import com.android.timeoverdue.databinding.ActivityTimeRemindBinding;
import com.android.timeoverdue.ui.adapter.ClassifyAdapter;
import com.android.timeoverdue.ui.adapter.TimeReinderAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TimeReinderActivity extends BaseActivity<ActivityTimeRemindBinding> {

    private TimeReinderAdapter timeReinderAdapter;
    private List<BmobTimeReinder> list;
    private TimeReinderAdapter.OnSetItemClickListener listener = new TimeReinderAdapter.OnSetItemClickListener() {
        @Override
        public void onClick(int position) {
//            jumpToActivity();
        }
    };

    @Override
    protected void initData() {
        getTimeReinderList();
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
    private void getTimeReinderList(){
        BmobQuery<BmobTimeReinder> query = new BmobQuery<BmobTimeReinder>();
        query.order("createAt");
        query.findObjects(new FindListener<BmobTimeReinder>() {
            @Override
            public void done(List<BmobTimeReinder> data, BmobException e) {
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

}
