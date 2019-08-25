package com.example.hsc.irunning.command.utils;

import android.util.Log;

/**
 * 日志打印
 *
 * @author Diviner
 * @date 2018-5-1 下午5:13:53
 */
public class LogMsgUtil {
    public static void Log_D(String tag, String msg) {
        try {
            Log.d(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void LogMsg_D(String activityName, String msg) {
        try {
            Log.d(activityName, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Log_E(String tag, String msg) {
        try {
            Log.e(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Log_i(String tag, String msg) {
        try {
            Log.i(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Log_wtf(String tag, String msg) {
        try {
            Log.wtf(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Log_w(String tag, String msg) {
        try {
            Log.wtf(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Log_v(String tag, String msg) {
        try {
            Log.wtf(tag, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
