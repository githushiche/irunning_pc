package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * 步行信息表
 *
 * @author Diviner
 */
public class Walk extends LitePalSupport {
    private int wId;
    private int uId;// 用户id
    private int wStep;// 步数
    private int wDistance;// 距离
    private int wHeight;// 相当于几层楼高
    private Date wStartTime;// 开始时间
    private Date wEndTime;// 结束时间
    private Date wDate;// 日期 指名是那一天

    public Walk() {

    }

    public Walk(int wId, int uId, int wStep, int wDistance, int wHeight, Date wStartTime, Date wEndTime, Date wDate) {
        super();
        this.wId = wId;
        this.uId = uId;
        this.wStep = wStep;
        this.wDistance = wDistance;
        this.wHeight = wHeight;
        this.wStartTime = wStartTime;
        this.wEndTime = wEndTime;
        this.wDate = wDate;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    public int getwStep() {
        return wStep;
    }

    public void setwStep(int wStep) {
        this.wStep = wStep;
    }

    public int getwDistance() {
        return wDistance;
    }

    public void setwDistance(int wDistance) {
        this.wDistance = wDistance;
    }

    public int getwHeight() {
        return wHeight;
    }

    public void setwHeight(int wHeight) {
        this.wHeight = wHeight;
    }

    public Date getwStartTime() {
        return wStartTime;
    }

    public void setwStartTime(Date wStartTime) {
        this.wStartTime = wStartTime;
    }

    public Date getwEndTime() {
        return wEndTime;
    }

    public void setwEndTime(Date wEndTime) {
        this.wEndTime = wEndTime;
    }

    public Date getwDate() {
        return wDate;
    }

    public void setwDate(Date wDate) {
        this.wDate = wDate;
    }

}
