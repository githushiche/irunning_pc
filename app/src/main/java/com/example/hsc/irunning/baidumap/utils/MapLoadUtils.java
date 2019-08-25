package com.example.hsc.irunning.baidumap.utils;

import android.app.Activity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;

public class MapLoadUtils {
    private final String TAG = "MapLoadUtils";
    private static Activity mActivity;

    // 地图
    private static MapView mMapView;
    private static BaiduMap mBaiduMap;

    public MapLoadUtils(Activity activity) {
        mActivity = activity;
    }

    public static void DisplayMap(MapView mapView, LatLng ll, String userName) {
        mMapView = mapView;
        mBaiduMap = mMapView.getMap();
        // 开启定位图层，一定不要少了这句，否则对在地图的设置、绘制定位点将无效
        mBaiduMap.setMyLocationEnabled(true);

        LatLng lls = new LatLng(23.352603, 103.429512);
        displaySharePoiont(lls, userName);
    }

    /**
     * 添加障碍物,可以用来显示附近的好友
     *
     * @param ll
     */
    public static void displaySharePoiont(LatLng ll, String userName) {
        LogMsgUtil.Log_D("MapLoadUtils", "正在显示位置分享信息");
        mBaiduMap.clear();// 清除图层

        // 添加文字信息
        OverlayOptions mTextOptions = new TextOptions()
                .text(userName) //文字内容
                .bgColor(0xAAFFFF00) //背景色
                .fontSize(30) //字号
                .fontColor(0xFFFF00FF) //文字颜色
                .position(ll);
        //在地图上显示文字覆盖物
        mBaiduMap.addOverlay(mTextOptions);

        // 添加点信息
        OverlayOptions optins = new MarkerOptions()//
                .position(ll)//
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location))//
                .zIndex(5)// 设置图层层级
                .perspective(true);// 设置远大近小

        mBaiduMap.addOverlay(optins);// 在目标地点添加障碍物

        MapStatusUpdate msu1 = MapStatusUpdateFactory.zoomTo(15.0f);// 设置地图的缩放比例
        mBaiduMap.setMapStatus(msu1);// 更新

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);// 传入当前位置
        mBaiduMap.animateMapStatus(msu);// 通过动态移动到当前定位区域
    }
}
