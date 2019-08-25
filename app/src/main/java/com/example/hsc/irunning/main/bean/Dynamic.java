package com.example.hsc.irunning.main.bean;


import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 动态表
 *
 * @author Diviner
 */
public class Dynamic extends LitePalSupport implements Serializable {
    private int dId;
    private int uId;// 发布动态用户id
    private String uName;// 发布动态用户昵称
    private String dContent;// 动态内容
    private String dCalories;// 当天消耗的卡路里
    private String dStepCount;// 当天所走的步数
    private String dPhoto;// 图片
    private byte[] dImg;// 图片数据
    private String dSendTime;// 动态发布时间
    private int dLikeNumber;// 点赞人数
    private List<Likes> dLisks;// 点赞表
    private List<Comment> dComments;// 评量表

    public Dynamic() {

    }

    public Dynamic(int dId, int uId, String uName, String dContent, String dCalories, String dStepCount, String dPhoto,
                   byte[] dImg, String dSendTime, int dLikeNumber, List<Likes> dLisks, List<Comment> dComments) {
        super();
        this.dId = dId;
        this.uId = uId;
        this.uName = uName;
        this.dContent = dContent;
        this.dCalories = dCalories;
        this.dStepCount = dStepCount;
        this.dPhoto = dPhoto;
        this.dImg = dImg;
        this.dSendTime = dSendTime;
        this.dLikeNumber = dLikeNumber;
        this.dLisks = dLisks;
        this.dComments = dComments;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getdContent() {
        return dContent;
    }

    public void setdContent(String dContent) {
        this.dContent = dContent;
    }

    public String getdCalories() {
        return dCalories;
    }

    public void setdCalories(String dCalories) {
        this.dCalories = dCalories;
    }

    public String getdStepCount() {
        return dStepCount;
    }

    public void setdStepCount(String dStepCount) {
        this.dStepCount = dStepCount;
    }

    public String getdPhoto() {
        return dPhoto;
    }

    public void setdPhoto(String dPhoto) {
        this.dPhoto = dPhoto;
    }

    public String getdSendTime() {
        return dSendTime;
    }

    public void setdSendTime(String dSendTime) {
        this.dSendTime = dSendTime;
    }

    public int getdLikeNumber() {
        return dLikeNumber;
    }

    public void setdLikeNumber(int dLikeNumber) {
        this.dLikeNumber = dLikeNumber;
    }

    public List<Likes> getdLisks() {
        return dLisks;
    }

    public void setdLisks(List<Likes> dLisks) {
        this.dLisks = dLisks;
    }

    public List<Comment> getdComments() {
        return dComments;
    }

    public void setdComments(List<Comment> dComments) {
        this.dComments = dComments;
    }

    public byte[] getdImg() {
        return dImg;
    }

    public void setdImg(byte[] dImg) {
        this.dImg = dImg;
    }

    @Override
    public String toString() {
        return "Dynamic{" +
                "dId=" + dId +
                ", uId=" + uId +
                ", uName='" + uName + '\'' +
                ", dContent='" + dContent + '\'' +
                ", dCalories='" + dCalories + '\'' +
                ", dStepCount='" + dStepCount + '\'' +
                ", dPhoto='" + dPhoto + '\'' +
                ", dSendTime='" + dSendTime + '\'' +
                ", dLikeNumber=" + dLikeNumber +
                ", dLisks=" + dLisks +
                ", dComments=" + dComments +
                '}';
    }
}
