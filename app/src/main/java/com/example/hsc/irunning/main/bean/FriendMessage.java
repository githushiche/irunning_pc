package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 用户消息
 *
 * @author Diviner
 * @date 2018-5-14 下午9:33:42
 */
public class FriendMessage extends LitePalSupport implements Serializable {
    private int mId;// 消息id
    private int mSendUserId;// 发送id
    private int mReceiverUserId;// 接受者id
    private int mState;// 状态
    private int mDirect;
    private int mMessageType;// 消息类型 0普通消息,1图片消息,2位置消息
    private int mResultType;// 返回类型 0是发送,1是接受
    private String mSendTime;// 发送时间
    private String mContent;// 消息内容
    public static final int RECEIVE = 0;// 接收

    public static final int SEND = 1;// 发送

    public FriendMessage() {

    }

    public FriendMessage(int mId, int mSendUserId, int mReceiverUserId,
                         int mState, int mDirect, int mMessageType, int mResultType,
                         String mSendTime, String mContent) {
        super();
        this.mId = mId;
        this.mSendUserId = mSendUserId;
        this.mReceiverUserId = mReceiverUserId;
        this.mState = mState;
        this.mDirect = mDirect;
        this.mMessageType = mMessageType;
        this.mResultType = mResultType;
        this.mSendTime = mSendTime;
        this.mContent = mContent;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmSendUserId() {
        return mSendUserId;
    }

    public void setmSendUserId(int mSendUserId) {
        this.mSendUserId = mSendUserId;
    }

    public int getmReceiverUserId() {
        return mReceiverUserId;
    }

    public void setmReceiverUserId(int mReceiverUserId) {
        this.mReceiverUserId = mReceiverUserId;
    }

    public int getmState() {
        return mState;
    }

    public void setmState(int mState) {
        this.mState = mState;
    }

    public int getmDirect() {
        return mDirect;
    }

    public void setmDirect(int mDirect) {
        this.mDirect = mDirect;
    }

    public int getmMessageType() {
        return mMessageType;
    }

    public void setmMessageType(int mMessageType) {
        this.mMessageType = mMessageType;
    }

    public int getmResultType() {
        return mResultType;
    }

    public void setmResultType(int mResultType) {
        this.mResultType = mResultType;
    }

    public String getmSendTime() {
        return mSendTime;
    }

    public void setmSendTime(String mSendTime) {
        this.mSendTime = mSendTime;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    @Override
    public String toString() {
        return "FriendMessage{" +
                "mId=" + mId +
                ", mSendUserId=" + mSendUserId +
                ", mReceiverUserId=" + mReceiverUserId +
                ", mState=" + mState +
                ", mDirect=" + mDirect +
                ", mMessageType=" + mMessageType +
                ", mResultType=" + mResultType +
                ", mSendTime='" + mSendTime + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }
}
