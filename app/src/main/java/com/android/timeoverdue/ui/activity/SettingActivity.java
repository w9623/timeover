package com.android.timeoverdue.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.databinding.ActivitySettingBinding;
import com.android.timeoverdue.utils.ActivityUtil;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends BaseActivity<ActivitySettingBinding> {

    @Override
    protected void initView() {
        viewBinding.includeTop.tvSave.setVisibility(View.GONE);
        viewBinding.includeTop.ivMore.setVisibility(View.GONE);
        viewBinding.includeTop.tvTitle.setText("设置");
        judgeUser();
    }

    @Override
    protected void onClick() {
        //返回按钮
        viewBinding.includeTop.ivBack.setOnClickListener(v->{
            finish();
        });
        //发送反馈按钮
        viewBinding.llFeedBack.setOnClickListener(v->{
            jumpToActivity(FeedBackActivity.class);
        });
        //数据备份按钮
        viewBinding.llBackups.setOnClickListener(v->{
            if (BmobUser.isLogin()){
                jumpToActivity(BackupsActivity.class);
            }else {
                jumpToActivity(LoginOrRegisterActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeUser();
    }

    private void judgeUser(){
        if (BmobUser.isLogin()){
            viewBinding.tvSynchronization.setText("已开启同步");
        }else {
            viewBinding.tvSynchronization.setText("未开启同步");
        }
    }
}