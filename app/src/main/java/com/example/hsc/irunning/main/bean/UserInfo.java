package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 用户基本信息
 *
 * @author Diviner
 */
public class UserInfo extends LitePalSupport implements Serializable {
    private int uId;
    private int uSex;// 性别 0男1女
    private int uAge;// 年龄
    private String uCity;// 城市
    private String uEmail;// 邮箱用于认证
    private String uPhone;// 联系电话
    private String uAddress;// 详细地址
    private String uIntroduce;// 个人简介
    private int uHeight;// 身高
    private int uWeight;// 体重
    private float uFat;// 体脂
    private double uLongitude;// 记录用户经度
    private double uLatitude;// 记录用户纬度

    public UserInfo() {
    }

    public UserInfo(int uId, int uSex, int uAge, String uCity, String uEmail, String uPhone, String uAddress,
                    String uIntroduce, int uHeight, int uWeight, float uFat, double uLongitude, double uLatitude) {
        super();
        this.uId = uId;
        this.uSex = uSex;
        this.uAge = uAge;
        this.uCity = uCity;
        this.uEmail = uEmail;
        this.uPhone = uPhone;
        this.uAddress = uAddress;
        this.uIntroduce = uIntroduce;
        this.uHeight = uHeight;
        this.uWeight = uWeight;
        this.uFat = uFat;
        this.uLongitude = uLongitude;
        this.uLatitude = uLatitude;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getuSex() {
        return uSex;
    }

    public void setuSex(int uSex) {
        this.uSex = uSex;
    }

    public int getuAge() {
        return uAge;
    }

    public void setuAge(int uAge) {
        this.uAge = uAge;
    }

    public String getuCity() {
        return uCity;
    }

    public void setuCity(String uCity) {
        this.uCity = uCity;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuAddress() {
        return uAddress;
    }

    public void setuAddress(String uAddress) {
        this.uAddress = uAddress;
    }

    public String getuIntroduce() {
        return uIntroduce;
    }

    public void setuIntroduce(String uIntroduce) {
        this.uIntroduce = uIntroduce;
    }

    public int getuHeight() {
        return uHeight;
    }

    public void setuHeight(int uHeight) {
        this.uHeight = uHeight;
    }

    public int getuWeight() {
        return uWeight;
    }

    public void setuWeight(int uWeight) {
        this.uWeight = uWeight;
    }

    public float getuFat() {
        return uFat;
    }

    public void setuFat(float uFat) {
        this.uFat = uFat;
    }

    public double getuLongitude() {
        return uLongitude;
    }

    public void setuLongitude(double uLongitude) {
        this.uLongitude = uLongitude;
    }

    public double getuLatitude() {
        return uLatitude;
    }

    public void setuLatitude(double uLatitude) {
        this.uLatitude = uLatitude;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uId=" + uId +
                ", uSex=" + uSex +
                ", uAge=" + uAge +
                ", uCity='" + uCity + '\'' +
                ", uEmail='" + uEmail + '\'' +
                ", uPhone='" + uPhone + '\'' +
                ", uAddress='" + uAddress + '\'' +
                ", uIntroduce='" + uIntroduce + '\'' +
                ", uHeight=" + uHeight +
                ", uWeight=" + uWeight +
                ", uFat=" + uFat +
                ", uLongitude=" + uLongitude +
                ", uLatitude=" + uLatitude +
                '}';
    }
}
