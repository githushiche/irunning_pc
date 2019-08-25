package com.example.hsc.irunning.command.utils;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

/**
 * 全局上下文类
 * Created by Diviner
 * Time:2018/12/20.
 * Version:1.0
 */

public class BaseApplication extends Application {
    private final String TAG = "BaseApplication";
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        LitePal.initialize(sContext);// 初始化litepal数据库
        Connector.getDatabase();// 创建数据库及表

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        LogMsgUtil.Log_D(TAG, "--------------地图初始化--------------");
    }

    /**
     * 获取当前上下文
     *
     * @return
     */
    public static Context getsContext() {
        return sContext;
    }
}
