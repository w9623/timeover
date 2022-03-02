package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobObject;

public class BmobCompany extends BmobObject {

    /**
     * 是否属于系统单位
     */
    private Boolean isSystem;

    /**
     * 单位名称
     */
    private String name;

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
