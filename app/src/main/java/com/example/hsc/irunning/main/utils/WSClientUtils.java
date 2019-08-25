package com.example.hsc.irunning.main.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.FoodBlog;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.notification.FriendMessageNotification;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * websocket帮助类
 * Created by Diviner on 2019/4/16.
 * Vesion:1.0
 */
public class WSClientUtils {
    private final String TAG = "WSClientUtils";

    private Activity mActivity;
    public static WSClient mWsClient;
    private URI mUri;
    //    private String mUrl = "ws://10.3.65.211:8080/mes/websocket?uid=" + MainActivity.sUser.getuId();
    private String mUrl = "ws://192.168.43.17:8080/mes/websocket?uid=" + MainActivity.sUser.getuId();

    public static boolean isOpenMessage = false;

    public WSClientUtils(Activity activity) {
        mActivity = activity;
    }

    /**
     * 开始websocket
     */
    public void startWebsocket() {
        //        OkHttpClient client = new OkHttpClient();
        //
        //        User user = new User(1, "admin", "admin520", "123456", "20190415", 0);
        //
        //        final RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
        //                , new Gson().toJson(user));// 利用表单传递json数据
        //
        //        final Request request = new Request.Builder()
        //                .url("http://172.20.10.6:8080/mes/websocket/login")
        //                .post(requestBody)
        //                .build();
        //        client.newCall(request).enqueue(new Callback() {
        //            @Override
        //            public void onFailure(Call call, IOException e) {
        //                LogMsgUtil.Log_D(TAG, "登录" + e.getMessage());
        //            }
        //
        //            @Override
        //            public void onResponse(Call call, Response response) throws IOException {
        //                // 登陆成功后才开启websocket,写下逻辑
        //                String json = response.body().string();
        //                HttpMsgSimpleModel hmsm = new Gson().fromJson(json, HttpMsgSimpleModel.class);
        //                if (hmsm.getResultCode().equals("200")) {
        //
        //                }
        //            }
        //        });
        initSocketClient();
    }

    private void initSocketClient() {
        mUri = URI.create(mUrl);
        mWsClient = new WSClient(mUri) {

            @Override
            public void onMessage(String message) {
                super.onMessage(message);
                // 获得消息后应该用发出通知
                Gson gson = new Gson();
                String typeCode = "0";
                String[] messageType = message.split("\\|\\|\\|");
                FriendMessageNotification notification = new FriendMessageNotification(mActivity.getApplicationContext());// 初始化通知

                List<User> messages = new ArrayList<>();
                User user = new User();
                switch (messageType[0]) {
                    case "Ordinary":// 普通聊天消息--0
                        typeCode = "0";
                        user = gson.fromJson(messageType[1], User.class);

                        LogMsgUtil.Log_D(TAG, "收到的普通聊天消息---------->" + messageType[1]);
                        notification.initNotification(user);

                        messages.add(user);
                        break;
                    case "OfflineMessages":// 离线消息--1
                        typeCode = "1";
                        messages = gson.fromJson(messageType[1],
                                new TypeToken<List<User>>() {
                                }.getType());
                        LogMsgUtil.Log_D(TAG, "收到的离线聊天消息---------->" + messageType[1] + ",长度" + messages.get(0).getuFriendMessages().size());

                        notification.initNotifications(messages);
                        break;
                    case "foodBlog":
                        LogMsgUtil.Log_D(TAG, "收到服务器推送的美食博文");
                        List<FoodBlog> fl = gson.fromJson(messageType[1],
                                new TypeToken<List<FoodBlog>>() {
                                }.getType());
                        LogMsgUtil.Log_D(TAG, "收到的推送消息---------->" + messageType[1] + ",长度" + fl.get(0).getfName());

                        notification.initBowenNotifications(fl);
                        break;
                }
                Intent intent = new Intent("com.example.hsc.irunning.MY_LOCAL_BROADCAST");

                if (isOpenMessage) {// 先判断是否处于消息界面,是就直接显示消息,不是就发通知
                    LogMsgUtil.Log_D(TAG, "消息界面目前处于打开状态!");

                    intent.putExtra("typeCode", typeCode);// 把类型码传过去
                    intent.putExtra("receiveCode", "2004");
                    intent.putExtra("message", new Gson().toJson(messages));
                    MainActivity.mLocalBroadcastManager.sendBroadcast(intent);//显示到聊天界面
                } else {
                    LogMsgUtil.Log_D(TAG, "消息界面目前处于关闭状态!准备发送通知!");
                    if (typeCode.equals("0")) {
                        intent.putExtra("typeCode", typeCode);// 把类型码传过去
                        intent.putExtra("receiveCode", "2003");
                        intent.putExtra("message", new Gson().toJson(messages));
                    } else {
                        intent.putExtra("typeCode", typeCode);// 把类型码传过去
                        intent.putExtra("receiveCode", "2003");
                        intent.putExtra("message", "" + new Gson().toJson(messages));
                    }
                    MainActivity.mLocalBroadcastManager.sendBroadcast(intent);
                }
            }
        };
        connect();
    }

    //连接
    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    mWsClient.connectBlocking();
                    LogMsgUtil.Log_D("connectBlocking", "连接成功");
                    if (mWsClient.isOpen()) {// connectBlocking连接稳定后告诉服务器我是谁
                        //                        User user = new User(1, "admin", "admin159", "admin1", "20190415", 1);
                        //                        sendMsg("我是客户端");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void sendMsg(String msg) {
        if (null != mWsClient) {
            mWsClient.send(msg);
            LogMsgUtil.Log_D("发送的消息", msg);
        }
    }

    //断开连接
    public void closeConnect() {
        try {
            if (mWsClient != null) {
                mWsClient.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogMsgUtil.Log_D("Socket", "断开连接异常" + e.getMessage());
        } finally {
            mWsClient = null;
        }
    }
}
