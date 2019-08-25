package com.example.hsc.irunning.baidumap.utils;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.example.hsc.irunning.R;

/**
 * 构建图标帮助类
 *
 * @author Diviner
 * @date 2018-4-14 下午3:02:16
 */
public class BitmapUtil {
    public static BitmapDescriptor mBmArrowPoint = null;
    public static BitmapDescriptor mBmStart = null;
    public static BitmapDescriptor mBmEnd = null;
    public static BitmapDescriptor mBitmapDescriptor = null;

    /**
     * 创建图标
     */
    public static void init() {
        mBmArrowPoint = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_start);
        mBmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
        mBmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);
        mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.up);
    }

    /**
     * 释放
     */
    public static void clear() {
        mBmArrowPoint.recycle();
        mBmStart.recycle();
        mBmEnd.recycle();
        mBitmapDescriptor.recycle();
    }
}
