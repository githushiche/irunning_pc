package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 用户账号信息
 *
 * @author Diviner
 */
public class User extends LitePalSupport implements Serializable {
    private int uId;
    private String uName;// 用户名
    private String uNickName;// 用户昵称
    private String uPassWord;// 密码
    private String uCreateTime;// 账号创建时间
    private int uState;// 状态,用于判断是否登录
    private UserInfo uUserInfo;
    private UserPicture uUserPicture;
    private List<Walk> uWalk;// 持有记录集合
    private List<Dynamic> uDynamics;
    private List<FriendMessage> uFriendMessages;

    public User() {
    }

    /**
     * @param uId
     * @param uName
     * @param uNickName
     * @param uPassWord
     * @param uCreateTime
     * @param uState
     */
    public User(int uId, String uName, String uNickName, String uPassWord, String uCreateTime, int uState) {
        this.uId = uId;
        this.uName = uName;
        this.uNickName = uNickName;
        this.uPassWord = uPassWord;
        this.uCreateTime = uCreateTime;
        this.uState = uState;
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

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public String getuPassWord() {
        return uPassWord;
    }

    public void setuPassWord(String uPassWord) {
        this.uPassWord = uPassWord;
    }

    public String getuCreateTime() {
        return uCreateTime;
    }

    public void setuCreateTime(String uCreateTime) {
        this.uCreateTime = uCreateTime;
    }

    public int getuState() {
        return uState;
    }

    public void setuState(int uState) {
        this.uState = uState;
    }

    public UserInfo getuUserInfo() {
        return uUserInfo;
    }

    public void setuUserInfo(UserInfo uUserInfo) {
        this.uUserInfo = uUserInfo;
    }

    public List<Walk> getuWalk() {
        return uWalk;
    }

    public void setuWalk(List<Walk> uWalk) {
        this.uWalk = uWalk;
    }

    public List<Dynamic> getuDynamics() {
        return uDynamics;
    }

    public void setuDynamics(List<Dynamic> uDynamics) {
        this.uDynamics = uDynamics;
    }

    public List<FriendMessage> getuFriendMessages() {
        return uFriendMessages;
    }

    public void setuFriendMessages(List<FriendMessage> uFriendMessages) {
        this.uFriendMessages = uFriendMessages;
    }

    public UserPicture getuUserPicture() {
        return uUserPicture;
    }

    public void setuUserPicture(UserPicture uUserPicture) {
        this.uUserPicture = uUserPicture;
    }

    @Override
    public String toString() {
        return "User{" +
                "uId=" + uId +
                ", uName='" + uName + '\'' +
                ", uNickName='" + uNickName + '\'' +
                ", uPassWord='" + uPassWord + '\'' +
                ", uCreateTime='" + uCreateTime + '\'' +
                ", uState=" + uState +
                ", uUserInfo=" + uUserInfo +
                ", uUserPicture=" + uUserPicture +
                ", uWalk=" + uWalk +
                ", uDynamics=" + uDynamics +
                ", uFriendMessages=" + uFriendMessages +
                '}';
    }
}
