package com.example.hsc.irunning.main.bean;

/**
 * 美食博文
 *
 * @author Diviner
 */
public class FoodBlog {
    private int fId;
    private String fName;
    private String fPhoto;
    private String fContent;
    private String fSendTime;
    private String fAdminName;

    public FoodBlog() {
    }

    public FoodBlog(int fId, String fName, String fPhoto, String fContent, String fSendTime, String fAdminName) {
        super();
        this.fId = fId;
        this.fName = fName;
        this.fPhoto = fPhoto;
        this.fContent = fContent;
        this.fSendTime = fSendTime;
        this.fAdminName = fAdminName;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfPhoto() {
        return fPhoto;
    }

    public void setfPhoto(String fPhoto) {
        this.fPhoto = fPhoto;
    }

    public String getfContent() {
        return fContent;
    }

    public void setfContent(String fContent) {
        this.fContent = fContent;
    }

    public String getfSendTime() {
        return fSendTime;
    }

    public void setfSendTime(String fSendTime) {
        this.fSendTime = fSendTime;
    }

    public String getfAdminName() {
        return fAdminName;
    }

    public void setfAdminName(String fAdminName) {
        this.fAdminName = fAdminName;
    }

}
