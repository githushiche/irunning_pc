package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

/**
 * 抽屉菜单实体类
 *
 * @author Diviner
 * @date 2018-5-1 下午6:11:37
 */
public class LeftMenu extends LitePalSupport {
    private int mImgId;
    private int mType;// 0个人信息,1我的爱好,2我的足迹,3我的健康其他类型自定义
    private String mName;

    public LeftMenu() {

    }

    public LeftMenu(int mImgId, int type, String mName) {
        super();
        this.mImgId = mImgId;
        this.mType = type;
        this.mName = mName;
    }

    /**
     * @return the mImgId
     */
    public int getmImgId() {
        return mImgId;
    }

    /**
     * @param mImgId the mImgId to set
     */
    public void setmImgId(int mImgId) {
        this.mImgId = mImgId;
    }

    /**
     * @return the mType
     */
    public int getmType() {
        return mType;
    }

    /**
     * @param mType the mType to set
     */
    public void setmType(int mType) {
        this.mType = mType;
    }

    /**
     * @return the mName
     */
    public String getmName() {
        return mName;
    }

    /**
     * @param mName the mName to set
     */
    public void setmName(String mName) {
        this.mName = mName;
    }

}
