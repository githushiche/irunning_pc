package com.example.hsc.irunning.main.bean;

public class Videos {
    private int vId;
    private String vName;
    private String vPath;

    private byte[] vVideos;

    public Videos() {

    }

    public Videos(int vId, String vName, String vPath, byte[] vVideos) {
        super();
        this.vId = vId;
        this.vName = vName;
        this.vPath = vPath;
        this.vVideos = vVideos;
    }

    public int getvId() {
        return vId;
    }

    public void setvId(int vId) {
        this.vId = vId;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvPath() {
        return vPath;
    }

    public void setvPath(String vPath) {
        this.vPath = vPath;
    }

    public byte[] getvVideos() {
        return vVideos;
    }

    public void setvVideos(byte[] vVideos) {
        this.vVideos = vVideos;
    }

}
