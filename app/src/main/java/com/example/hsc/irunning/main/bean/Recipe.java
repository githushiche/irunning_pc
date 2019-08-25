package com.example.hsc.irunning.main.bean;

public class Recipe {
    private int rId;
    private String rName;// 食物名称
    private int rGram;// 每克含多少c
    private int rKcal;// 卡路里
    private String rPhotoImgPath;
    private int rType;// 类型
    private byte[] rPhoto;// 食物图片

    public Recipe() {

    }

    public Recipe(int rId, String rName, int rGram, int rKcal, String rPhotoImgPath, int rType, byte[] rPhoto) {
        super();
        this.rId = rId;
        this.rName = rName;
        this.rGram = rGram;
        this.rKcal = rKcal;
        this.rPhotoImgPath = rPhotoImgPath;
        this.rType = rType;
        this.rPhoto = rPhoto;
    }

    public int getrId() {
        return rId;
    }

    public void setrId(int rId) {
        this.rId = rId;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public int getrGram() {
        return rGram;
    }

    public void setrGram(int rGram) {
        this.rGram = rGram;
    }

    public int getrKcal() {
        return rKcal;
    }

    public void setrKcal(int rKcal) {
        this.rKcal = rKcal;
    }

    public int getrType() {
        return rType;
    }

    public void setrType(int rType) {
        this.rType = rType;
    }

    public String getrPhotoImgPath() {
        return rPhotoImgPath;
    }

    public void setrPhotoImgPath(String rPhotoImgPath) {
        this.rPhotoImgPath = rPhotoImgPath;
    }

    public byte[] getrPhoto() {
        return rPhoto;
    }

    public void setrPhoto(byte[] rPhoto) {
        this.rPhoto = rPhoto;
    }

}
