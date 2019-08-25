package com.example.hsc.irunning.main.bean;

/**
 * 菜品表
 *
 * @author Diviner
 */
public class Dishes {
    private int dId;
    private String dName;
    private String dPhoto;
    private byte[] dImg;
    private String dMaterial;// 材料
    private String dPracticeIntroduction;

    public Dishes() {

    }

    public Dishes(int dId, String dName, String dPhoto, byte[] dImg, String dMaterial, String dPracticeIntroduction) {
        super();
        this.dId = dId;
        this.dName = dName;
        this.dPhoto = dPhoto;
        this.dImg = dImg;
        this.dMaterial = dMaterial;
        this.dPracticeIntroduction = dPracticeIntroduction;
    }

    public int getdId() {
        return dId;
    }

    public void setdId(int dId) {
        this.dId = dId;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdPhoto() {
        return dPhoto;
    }

    public void setdPhoto(String dPhoto) {
        this.dPhoto = dPhoto;
    }

    public byte[] getdImg() {
        return dImg;
    }

    public void setdImg(byte[] dImg) {
        this.dImg = dImg;
    }

    public String getdPracticeIntroduction() {
        return dPracticeIntroduction;
    }

    public void setdPracticeIntroduction(String dPracticeIntroduction) {
        this.dPracticeIntroduction = dPracticeIntroduction;
    }

    public String getdMaterial() {
        return dMaterial;
    }

    public void setdMaterial(String dMaterial) {
        this.dMaterial = dMaterial;
    }

}