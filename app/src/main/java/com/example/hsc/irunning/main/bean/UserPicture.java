package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class UserPicture extends LitePalSupport implements Serializable {
    private int uId;
    private String uPicturePath;

    public UserPicture() {

    }

    public UserPicture(int uId, String uPicturePath) {
        super();
        this.uId = uId;
        this.uPicturePath = uPicturePath;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getuPicturePath() {
        return uPicturePath;
    }

    public void setuPicturePath(String uPicturePath) {
        this.uPicturePath = uPicturePath;
    }

    @Override
    public String toString() {
        return "UserPicture{" +
                "uId=" + uId +
                ", uPicturePath='" + uPicturePath + '\'' +
                '}';
    }
}
