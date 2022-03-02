package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobObject;

public class BmobClassify extends BmobObject {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 是否属于系统分类
     */
    private Boolean isSystem;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public Boolean getSystem() {
        return isSystem;
    }
}
