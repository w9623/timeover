package com.android.timeoverdue.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

public class BmobGoods extends BmobObject {

    /**
     * 物品名称
     */
    private String name;

    /**
     * 物品数量
     */
    private String number;

    /**
     * 分类名称
     */
    private String classify;

    /**
     * 生产日期
     */
    private Date dateOfManufacture;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 保质天数
     */
    private String shelfLifeDays;

    /**
     * 过期提醒
     */
    private String timeReminder;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 图片
     */
    private String photo;
}
