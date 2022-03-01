package com.android.timeoverdue.bean;

import cn.bmob.v3.BmobUser;

public class UserBean extends BmobUser {

    //昵称
    private String userName;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
