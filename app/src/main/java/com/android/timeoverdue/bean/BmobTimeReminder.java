package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobObject;

public class BmobTimeReminder extends BmobObject {

    /**
     * 分类名称
     */
    private String classifyName;

    /**
     * 提醒天数
     */
    private String reminderDays;

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(String reminderDays) {
        this.reminderDays = reminderDays;
    }
}
