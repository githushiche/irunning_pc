package com.example.hsc.irunning.main.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.main.bean.FoodBlog;
import com.example.hsc.irunning.main.bean.User;

import java.io.File;
import java.util.List;

/**
 * 好友消息通知
 * Created by Diviner on 2019/4/16.
 * Vesion:1.0
 */
public class FriendMessageNotification {
    private final String TAG = "FriendMessageNotification";
    private Context mContext;

    private Notification mNotification;
    private NotificationManager mNotificationManager;

    public FriendMessageNotification(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 发送多条好友消息通知
     *
     * @param friendMessages
     */
    public void initNotifications(List<User> friendMessages) {
        // 用于点击通知之后跳转到指定界面
        Intent intent = new Intent(mContext, FriendMessageNotification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        mNotification = new NotificationCompat.Builder(mContext)
                .setContentTitle("您的好友" + friendMessages.get(0).getuNickName() + "等好友发来消息")
                .setContentText("您的好友" + friendMessages.get(0).getuNickName()
                        + "等发来" + friendMessages.get(0).getuFriendMessages().size() + "条消息")
                .setWhen(System.currentTimeMillis())
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Beep_Once.ogg")))// 通知声音
                .setVibrate(new long[]{0, 1000, 0, 0})
                .setSmallIcon(R.mipmap.fimg)
                //                .setContentIntent(pendingIntent)
                .setAutoCancel(true)// 点击自动取消
                .setPriority(NotificationCompat.PRIORITY_MAX)// 表示通知很重要
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.fimg))
                .build();
        showNotification();
    }

    public void initBowenNotifications(List<FoodBlog> fblog) {
        // 用于点击通知之后跳转到指定界面
        Intent intent = new Intent(mContext, FriendMessageNotification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        mNotification = new NotificationCompat.Builder(mContext)
                .setContentTitle(fblog.get(0).getfName())
                .setContentText(fblog.get(0).getfContent())
                .setWhen(System.currentTimeMillis())
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Beep_Once.ogg")))// 通知声音
                .setVibrate(new long[]{0, 1000, 0, 0})
                .setSmallIcon(R.mipmap.step)
                //                .setContentIntent(pendingIntent)
                .setAutoCancel(true)// 点击自动取消
                .setPriority(NotificationCompat.PRIORITY_MAX)// 表示通知很重要
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.fimg))
                .build();
        showNotification();
    }

    /**
     * 显示单独一条消息通知
     *
     * @param user
     */
    public void initNotification(User user) {
        // 用于点击通知之后跳转到指定界面
        Intent intent = new Intent(mContext, FriendMessageNotification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        mNotification = new NotificationCompat.Builder(mContext)
                .setContentTitle("您的好友" + user.getuNickName() + "发来消息")
                .setContentText("" + user.getuFriendMessages().get(0).getmContent())
                .setWhen(System.currentTimeMillis())
                .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Beep_Once.ogg")))// 通知声音
                .setVibrate(new long[]{0, 1000, 0, 0})
                .setSmallIcon(R.mipmap.fimg)
                //                .setContentIntent(pendingIntent)
                .setAutoCancel(true)// 点击自动取消
                .setPriority(NotificationCompat.PRIORITY_MAX)// 表示通知很重要
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.fimg))
                .build();
        showNotification();
    }

    public void showNotification() {
        mNotificationManager.notify(1, mNotification);
    }
}
