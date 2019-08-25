package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 点赞实体类
 *
 * @author Diviner
 */
public class Likes extends LitePalSupport implements Serializable {
    private int lId;// 表id
    private int dId;// 点赞动态id
    private int lBeId;// 被点赞人的id
    private int uId;// 点赞人id
    private String uName;// 点赞人昵称

    public Likes() {
    }

    public Likes(int lId, int dId, int lBeId, int uId, String uName) {
        super();
        this.lId = lId;
        this.dId = dId;
        this.lBeId = lBeId;
        this.uId = uId;
        this.uName = uName;
    }

    public int getlId() {
        return lId;
    }

    public void setlId(int lId) {
        this.lId = lId;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public int getlBeId() {
        return lBeId;
    }

    public void setlBeId(int lBeId) {
        this.lBeId = lBeId;
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

    @Override
    public String toString() {
        return "Likes{" +
                "lId=" + lId +
                ", dId=" + dId +
                ", lBeId=" + lBeId +
                ", uId=" + uId +
                ", uName='" + uName + '\'' +
                '}';
    }
}
