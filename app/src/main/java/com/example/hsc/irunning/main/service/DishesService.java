package com.example.hsc.irunning.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.hsc.irunning.main.notification.DishesPushNotification;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Diviner on 2019/5/23.
 * Vesion:1.0
 */
public class DishesService extends Service {
    private DishesPushNotification mDishesPushNotification;
    private DishesPush mBinder = new DishesPush();

    public DishesService() {

    }

    public class DishesPush extends Binder {

        /**
         * 开始推送
         */
        public void startDishesPush() {
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        mDishesPushNotification = new DishesPushNotification(getApplicationContext());

        PushThread pt = new PushThread();
        pt.start();

        super.onCreate();
    }

    class PushThread extends Thread {
        public boolean isRunning = true;

        @Override
        public void run() {
            super.run();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

            while (isRunning) {
                String time = sdf.format(new Date());
                String[] timeStr = time.split("\\:");

                // 12点定时器
                if (Integer.parseInt(timeStr[0]) < 12 && Integer.parseInt(timeStr[0]) > 12) {
                    try {
                        Thread.sleep(1000 * 6);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (timeStr[0].equals("12")) {// 准时推送
                    if (timeStr[1].equals("00")) {
                        mDishesPushNotification.initNotification();
                        break;
                    }
                }

                // 17点半定时器
                if (Integer.parseInt(timeStr[0]) < 17 && Integer.parseInt(timeStr[0]) > 17) {
                    try {
                        Thread.sleep(1000 * 60);// 休眠一小时
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                } else if (timeStr[0].equals("17")) {
                    if (timeStr[1].equals("30")) {
                        mDishesPushNotification.initNotification();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 启动服务后
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
