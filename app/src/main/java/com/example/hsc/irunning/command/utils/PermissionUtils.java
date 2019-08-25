package com.example.hsc.irunning.command.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;

import com.example.hsc.irunning.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络判断帮助类
 *
 * @author Diviner
 * @date 2018-5-16 下午5:01:58
 */
public class PermissionUtils {
    private final String TAG = "PermissionUtils";

    // 需要动态申请的权限
    private static String[] mPermissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_MULTICAST_STATE};
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    private static List<String> mPermissionList = new ArrayList<>();

    /**
     * 判断网络情况
     *
     * @param context 上下文
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 动态申请多个权限
     *
     * @param activity
     */
    public static boolean isOpenPermission(Activity activity) {
        mPermissionList.clear();//清空没有通过的权限

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //逐个判断你要的权限是否已经通过
            for (int i = 0; i < mPermissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(mPermissions[i]);//添加还未授予的权限
                    LogMsgUtil.Log_D("PermissionUtils", "执行不通过的权限" + mPermissions[i]);
                }
            }
            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(activity, mPermissions, 100);
            } else {
                return true;
            }
        }
        return false;
    }

    public static void checkPermission(final Activity activity) {
        if (!PermissionUtils.isNetworkAvalible(activity)) {
            TextView msg = new TextView(activity);
            msg.setTextSize(16);
            msg.setGravity(Gravity.CENTER);
            msg.setText("当前没有可以使用的网络，请设置网络！");

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setTitle("网络状态提示");
            builder.setCancelable(false);// 不可点击除弹出框外取消
            builder.setView(msg);
            builder.setPositiveButton("设置", new OnClickListener() {

                public void onClick(DialogInterface dialog,
                                    int whichButton) {
                    // 跳转到设置界面
                    activity.startActivityForResult(new Intent(
                            Settings.ACTION_WIRELESS_SETTINGS), 0);
                }
            }).setNegativeButton("取消", new OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();// 显示提示框

            Button btnok = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btnok.setTextColor(Color.RED);

            Button btncan = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            btncan.setTextColor(Color.RED);
        }
        return;
    }

    /**
     * 如果没有网络，则弹出网络设置对话框
     *
     * @param activity
     */
    public static void checkNetwork(final Activity activity) {
        if (!PermissionUtils.isNetworkAvalible(activity)) {
            TextView msg = new TextView(activity);
            msg.setTextSize(16);
            msg.setGravity(Gravity.CENTER);
            msg.setText("当前没有可以使用的网络，请设置网络！");

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setTitle("网络状态提示");
            builder.setCancelable(false);// 不可点击除弹出框外取消
            builder.setView(msg);
            builder.setPositiveButton("设置", new OnClickListener() {

                public void onClick(DialogInterface dialog,
                                    int whichButton) {
                    // 跳转到设置界面
                    activity.startActivityForResult(new Intent(
                            Settings.ACTION_WIRELESS_SETTINGS), 0);
                }
            }).setNegativeButton("取消", new OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();// 显示提示框

            Button btnok = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btnok.setTextColor(Color.RED);

            Button btncan = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            btncan.setTextColor(Color.RED);
        }
        return;
    }
}
