package com.example.hsc.irunning.main.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * // 饮食健康图文实体类
 * Created by Diviner on 2019/4/4.
 * Vesion:1.0
 */
public class HealthyRecycler extends LitePalSupport implements Serializable {
    private int hImgId;
    private String hName;
    private int hType;

    public HealthyRecycler() {
    }

    public HealthyRecycler(int hImgId, String hName, int type) {
        this.hImgId = hImgId;
        this.hName = hName;
        this.hType = type;
    }

    public int gethImgId() {
        return hImgId;
    }

    public void sethImgId(int hImgId) {
        this.hImgId = hImgId;
    }

    public String gethName() {
        return hName;
    }

    public void sethName(String hName) {
        this.hName = hName;
    }

    public int gethType() {
        return hType;
    }

    public void sethType(int hType) {
        this.hType = hType;
    }
}
