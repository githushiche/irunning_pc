package com.example.hsc.irunning.command.utils;

import android.app.Dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * dialog帮助类
 *
 * @author Diviner
 * @date 2018-5-5 下午7:40:03
 */
public class DialogCollectorUtils {
    public static List<Dialog> dialogs = new ArrayList<Dialog>();

    public static void addDialog(Dialog dialog) {
        dialogs.add(dialog);
    }

    public static void remoActivity(Dialog dialog) {
        dialogs.remove(dialog);
        dialog.cancel();// 关闭dialog
    }

    public static void finishAll() {
        for (Dialog dialog : dialogs) {
            dialog.cancel();// 取消全部
        }
        dialogs.clear();// 清理
    }
}
