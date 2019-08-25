package com.example.hsc.irunning.main.utils;

import com.example.hsc.irunning.command.utils.LogMsgUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by Diviner on 2019/3/27.
 */
public class WSClient extends WebSocketClient {
    private final String TAG = "WSClient";

    public WSClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LogMsgUtil.Log_D(TAG, "-----------------websocket连接打开");
    }

    @Override
    public void onMessage(String message) {
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        LogMsgUtil.Log_D(TAG, "-----------------关闭断开连接onClose---" + reason + ":code" + code);
    }

    @Override
    public void onError(Exception ex) {
        LogMsgUtil.Log_D(TAG, "-----------------出现错误---" + ex.getMessage());
    }
}
