package com.android.timeoverdue.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

public class BmobGoods extends BmobObject {

    /**
     * 用户id
     */
    private String userId;

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
    private String dateOfManufacture;

    /**
     * 过期时间
     */
    private String expirationTime;

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

    public BmobGoods() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getDateOfManufacture() {
        return dateOfManufacture;
    }

    public void setDateOfManufacture(String dateOfManufacture) {
        this.dateOfManufacture = dateOfManufacture;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getShelfLifeDays() {
        return shelfLifeDays;
    }

    public void setShelfLifeDays(String shelfLifeDays) {
        this.shelfLifeDays = shelfLifeDays;
    }

    public String getTimeReminder() {
        return timeReminder;
    }

    public void setTimeReminder(String timeReminder) {
        this.timeReminder = timeReminder;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
