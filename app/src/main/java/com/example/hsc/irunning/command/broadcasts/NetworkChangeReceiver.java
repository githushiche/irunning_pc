package com.example.hsc.irunning.command.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.hsc.irunning.command.utils.AppToastUtils;

/**
 * 判断有无网络的广播
 * Created by Diviner on 2019/4/12.
 * Vesion:1.0
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            //            AppToastUtils.toastShort(context, "网络可用");
        } else {
            AppToastUtils.toastShort(context, "网络不可用");
        }
    }
}
