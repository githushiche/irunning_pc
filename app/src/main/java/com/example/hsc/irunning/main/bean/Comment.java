package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 评论实体类
 *
 * @author Diviner
 */
public class Comment extends LitePalSupport implements Serializable {
    private int cId;// 表id
    private int uId;// 发布评论用户id
    private String uName;// 发布评论用户昵称
    private int cBeId;// 被评论人id
    private String cContent;// 评论内容
    private String cCommentTime;// 评论时间

    public Comment() {
        // TODO Auto-generated constructor stub
    }

    public Comment(int cId, int uId, String uName, int cBeId, String cContent, String cCommentTime) {
        super();
        this.cId = cId;
        this.uId = uId;
        this.uName = uName;
        this.cBeId = cBeId;
        this.cContent = cContent;
        this.cCommentTime = cCommentTime;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
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

    public int getcBeId() {
        return cBeId;
    }

    public void setcBeId(int cBeId) {
        this.cBeId = cBeId;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }

    public String getcCommentTime() {
        return cCommentTime;
    }

    public void setcCommentTime(String cCommentTime) {
        this.cCommentTime = cCommentTime;
    }

}
