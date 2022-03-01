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
        viewBinding.ivSet.setOnClickListener(v->{
            jumpToActivity(SettingActivity.class);
        });
    }
}