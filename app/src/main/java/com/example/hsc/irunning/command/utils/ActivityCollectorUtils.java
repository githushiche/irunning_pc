package com.example.hsc.irunning.command.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动帮助类
 *
 * @author Diviner
 * @date 2018-5-5 下午7:37:06
 */
public class ActivityCollectorUtils {
    public static List<Activity> activitys = new ArrayList<Activity>();

    /**
     * 添加一个活动
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * 移除一个活动
     *
     * @param activity
     */
    public static void remoActivity(Activity activity) {
        activitys.remove(activity);
        activity.finish();// 关闭这个活动
    }

    /**
     * 移除所有的活动
     */
    public static void finishAll() {
        for (Activity activity : activitys) {
            activity.finish();
        }
        activitys.clear();// 清理
    }
}
