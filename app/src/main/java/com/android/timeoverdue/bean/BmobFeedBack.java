package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobObject;

public class BmobFeedBack extends BmobObject {

    /**
     * 反馈内容
     */
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
