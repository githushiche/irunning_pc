package com.example.hsc.irunning.command.utils;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.TextView;

import com.example.hsc.irunning.R;

/**
 * Created by Diviner on 2019/3/24.
 */
public class AlertDialogUtils {

    /**
     * @param title
     * @param context
     */
    public static AlertDialog.Builder alertDialog(final Activity activity, String title, String context) {
        TextView msg = new TextView(activity);
        msg.setTextSize(16);
        msg.setGravity(Gravity.CENTER);
        msg.setText(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle(title);
        builder.setCancelable(false);// 不可点击除弹出框外取消
        builder.setView(msg);

        return builder;

    }
}
