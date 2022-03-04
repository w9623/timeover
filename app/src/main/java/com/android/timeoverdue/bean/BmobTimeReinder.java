package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobObject;

public class BmobTimeReinder extends BmobObject {

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * 提醒天数
     */
    private String reinderDays;

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getReinderDays() {
        return reinderDays;
    }

    public void setReinderDays(String reinderDays) {
        this.reinderDays = reinderDays;
    }
}
