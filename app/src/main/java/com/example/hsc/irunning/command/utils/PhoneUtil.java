package com.example.hsc.irunning.command.utils;

import android.os.Environment;
import android.text.format.Time;

/**
 * 手机帮助类
 *
 * @author Diviner
 * @date 2018-4-6 下午6:30:57
 */
public class PhoneUtil {

    /**
     * 获取手机识别名称
     */
    //	public static String getImei(Context context) {
    //		if(){
    //
    //		}
    //
    //		String mImei = "NULL";
    //		try {
    //			mImei = ((TelephonyManager) context
    //					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    //		} catch (Exception e) {
    //			mImei = "NULL";
    //		}
    //		return mImei;
    //	}

    /**
     * 获取SD卡
     *
     * @return
     */
    public static String getSDcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {// 判断当前SD卡是否可用
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 设置id生成策略
     *
     * @param index
     * @return
     */
    public static String setTableId(int index) {
        Time time = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料
        time.setToNow(); // 取得系统时间。

        int year = time.year;
        int month = time.month;
        int day = time.monthDay;

        String id = "";
        if (index >= 10) {
            id = String.valueOf(year) + "" + month + day + index;
        } else if (index >= 100) {
            id = String.valueOf(year) + "" + month + day + index;
        } else {
            id = String.valueOf(year) + "" + month + day + index;
        }
        return id;
    }
}
