package com.android.timeoverdue.app;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        //初始化Bmob
        Bmob.initialize(this, Contents.APPLICATION_ID);

        //初始化讯飞语音
        SpeechUtility.createUtility(MyApplication.this, "appid=a7338ba6");
    }

    public static MyApplication getContext() {
        return instance;
    }
}
