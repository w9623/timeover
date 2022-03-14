package com.android.timeoverdue.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.timeoverdue.R;
import com.android.timeoverdue.base.BaseActivity;
import com.android.timeoverdue.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void initView() {

    }

    @Override
    protected void onClick() {
        //设置按钮点击事件
        viewBinding.ivSet.setOnClickListener(v->{
            jumpToActivity(SettingActivity.class);
        });
        //添加物品按钮点击事件
        viewBinding.ivAdd.setOnClickListener(v->{
            jumpToActivity(AddGoodsActivity.class);
        });
    }
}