package com.example.hsc.irunning.main.bean;

/**
 * 返回信息封装实体类模版类
 *
 * @author Diviner
 * @date 2018-5-1 下午5:06:11
 */
public class HttpMsgSimpleModel {

    /**
     * 请求结果 200-成功 其他为失败，自定义
     */
    private String resultCode;

    /**
     * 请求结果信息
     */
    private String resultMsg;

    /**
     * 备注
     */
    private String remarks;

    public HttpMsgSimpleModel() {
    }

    public HttpMsgSimpleModel(String resultCode, String resultMsg, String remarks) {
        super();
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.remarks = remarks;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
