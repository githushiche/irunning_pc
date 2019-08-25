package com.example.hsc.irunning.command.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 提示框
 */
public class AppToastUtils {
    /**
     * 短时间弹出框
     *
     * @param context 上下文
     * @param str     提示消息
     */
    public static void toastShort(final Context context, final String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间弹出框
     *
     * @param context 上下文
     * @param str     提示消息
     */
    public static void toastLong(final Context context, final String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
