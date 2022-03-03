package com.android.timeoverdue.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.databinding.ActivitySettingBinding;
import com.android.timeoverdue.utils.ActivityUtil;

import java.util.List;

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
        //自定义分类按钮
        viewBinding.llClassify.setOnClickListener(v->{
            jumpToActivity(ClassifyActivity.class);
        });
        //自定义单位按钮
        viewBinding.llCompany.setOnClickListener(v->{
            jumpToActivity(CompanyActivity.class);
        });
        //评价app按钮
        viewBinding.llEvaluate.setOnClickListener(v->{
            if (hasAnyMarketInstalled(mContext)) {
                Uri uri = Uri.parse("market://details?id="+ mContext.getPackageName());
                Intent intent =new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                showToast("本设备没有应用市场！");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        judgeUser();
    }

    //判断用户当前是否登录
    private void judgeUser(){
        if (BmobUser.isLogin()){
            viewBinding.tvSynchronization.setText("已开启同步");
        }else {
            viewBinding.tvSynchronization.setText("未开启同步");
        }
    }

    public static boolean hasAnyMarketInstalled(Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("market://details?id=android.browser"));
        List list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return 0!= list.size();

    }
}