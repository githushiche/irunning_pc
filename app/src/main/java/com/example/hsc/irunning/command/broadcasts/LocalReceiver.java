package com.example.hsc.irunning.command.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.Dynamic;
import com.example.hsc.irunning.main.dialog.DynamicDialog;
import com.example.hsc.irunning.main.dialog.MessageDialog;
import com.example.hsc.irunning.main.fragment.MessageFragment;
import com.google.gson.Gson;

/**
 * Created by Diviner on 2019/4/12.
 * Vesion:1.0
 */
public class LocalReceiver extends BroadcastReceiver {
    private final String TAG = "LocalReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogMsgUtil.Log_D(TAG, "已经收到系统发出的本地广播!正在根据类型码做相应的处理!");
        String receiveCode = intent.getStringExtra("receiveCode");

        String typeCode = "";
        String json = "";
        if (!receiveCode.equals("")) {
            switch (receiveCode) {
                case "2001":
                    Dynamic dynamic = new Gson().fromJson(intent.getStringExtra("dynamic"), Dynamic.class);
                    DynamicDialog.addAdapter(dynamic);
                    break;

                case "2002":
                    //                    LogMsgUtil.Log_D(TAG, "--------------->2002广播要处理的数据" + intent.getStringExtra("dynamic"));
                    //                    List<Dynamic> dynamics = new Gson().fromJson(intent.getStringExtra("dynamic"),
                    //                            new TypeToken<List<Dynamic>>() {
                    //                            }.getType());
                    //                    DynamicDialog.updateAdapter(dynamics);
                    break;
                case "2003":
                    typeCode = intent.getStringExtra("typeCode");// 获取到是一条消息还是一堆消息
                    json = intent.getStringExtra("message");
                    LogMsgUtil.Log_D(TAG, "当前状态码是" + typeCode + ",消息是:" + json);
                    MessageFragment.showMessage(typeCode, json);// 显示好友发送的消息数据
                    break;

                case "2004":
                    // 消息界面打开的逻辑
                    typeCode = intent.getStringExtra("typeCode");// 获取到是一条消息还是一堆消息
                    json = intent.getStringExtra("message");
                    LogMsgUtil.Log_D(TAG, "当前状态码是" + typeCode + ",消息是:" + json);
                    MessageFragment.showMessage(typeCode, json);// 显示好友发送的消息数据
                    MessageDialog.addData(json);// 显示好友发送的消息数据
                    break;
            }
        }
    }
}
