package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobObject;

public class BmobReminderDays extends BmobObject {

    /**
     * 提醒时间
     */
    private String name;

    /**
     * 时间排序
     */
    private Number timeSorting;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getTimeSorting() {
        return timeSorting;
    }

    public void setTimeSorting(Number timeSorting) {
        this.timeSorting = timeSorting;
    }
}
