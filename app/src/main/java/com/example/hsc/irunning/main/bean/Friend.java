package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 朋友
 *
 * @author Diviner
 */
public class Friend extends LitePalSupport implements Serializable {
    private int fId;// 好友id
    private String fAddTime;// 添加时间
    private String fAddInfo;// 添加好友信息
    private int fUserId;// 用户自己的id
    private int fFriendId;// 所添加朋友的id
    private int fState;// 是否已经是好友 0是 1不是

    public Friend() {
    }

    /**
     *
     * @param fId 赋值0
     * @param fAddTime 添加时间
     * @param fAddInfo 添加信息
     * @param fUserId 本人id
     * @param fFriendId 要添加好友id
     * @param fState 默认为0
     */
    public Friend(int fId, String fAddTime, String fAddInfo, int fUserId, int fFriendId, int fState) {
        super();
        this.fId = fId;
        this.fAddTime = fAddTime;
        this.fAddInfo = fAddInfo;
        this.fUserId = fUserId;
        this.fFriendId = fFriendId;
        this.fState = fState;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    public String getfAddTime() {
        return fAddTime;
    }

    public void setfAddTime(String fAddTime) {
        this.fAddTime = fAddTime;
    }

    public String getfAddInfo() {
        return fAddInfo;
    }

    public void setfAddInfo(String fAddInfo) {
        this.fAddInfo = fAddInfo;
    }

    public int getfUserId() {
        return fUserId;
    }

    public void setfUserId(int fUserId) {
        this.fUserId = fUserId;
    }

    public int getfFriendId() {
        return fFriendId;
    }

    public void setfFriendId(int fFriendId) {
        this.fFriendId = fFriendId;
    }

    public int getfState() {
        return fState;
    }

    public void setfState(int fState) {
        this.fState = fState;
    }

}
