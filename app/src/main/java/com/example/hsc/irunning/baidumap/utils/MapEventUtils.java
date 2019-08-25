package com.example.hsc.irunning.baidumap.utils;

import android.app.Activity;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.activity.MapActivity;
import com.example.hsc.irunning.baidumap.dialog.BottomPopup;
import com.example.hsc.irunning.baidumap.dialog.LeftNavigationPopup;
import com.example.hsc.irunning.command.utils.LogMsgUtil;

import java.util.Map;

/**
 * 地图常用事件处理类
 * Created by Diviner on 2019/4/8.
 * Vesion:1.0
 */
public class MapEventUtils {
    private final String TAG = "MapEventUtils";

    private Activity mActivity;
    private BaiduMap mBaiduMap;

    private BottomPopup mBottomPopupWindow;
    private LeftNavigationPopup mLeftNavigationPopup;

    private BdNavigationUtils mBdNavigationUtils;

    /**
     * 初始化一系列地图监听器
     */
    public void initMapListener(Activity activity, BaiduMap baiduMap) {
        this.mBaiduMap = baiduMap;
        this.mActivity = activity;

        mBaiduMap.setOnMapClickListener(mMapClickListener);
        mBaiduMap.setOnMapLongClickListener(mMapLongClickListener);
        mBaiduMap.setOnMarkerClickListener(mMarkerClickListener);

        mBottomPopupWindow = new BottomPopup(mActivity);
        mLeftNavigationPopup = new LeftNavigationPopup(mActivity);

        mBdNavigationUtils = new BdNavigationUtils(mActivity, mBaiduMap);
        //        mBdNavigationUtils.initSetting();
    }

    /**
     * 地图点击事件监听器
     */
    private BaiduMap.OnMapClickListener mMapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            // 如没有特殊要求就不做处理
            LogMsgUtil.Log_D(TAG, "MapEventUtils---------------点击了地图");
        }

        /**
         * 地图内 Poi 单击事件回调函数
         *
         * @param mapPoi 点击的 poi 信息
         */
        @Override
        public boolean onMapPoiClick(final MapPoi mapPoi) {
            Map map = MapUtil.getDistance(MapActivity.mLastLocationData, mapPoi.getPosition());// 计算当前里程

            mBottomPopupWindow.initPopWindow(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RouteLineUtils rlu = new RouteLineUtils();
                    PlanNode stNode = null;
                    PlanNode enNode = null;

                    switch (view.getId()) {
                        case R.id.id_Walking:// 走路
                            mBaiduMap.clear();
                            rlu.initRouteLine(mBaiduMap);

                            // 设置起点终点信息
                            stNode = PlanNode.withLocation(MapActivity.mLastLocationData);// 从什么地方开始
                            enNode = PlanNode.withLocation(mapPoi.getPosition());// 终点

                            // 发起查询
                            rlu.mRoutePlanSearch.walkingSearch((new WalkingRoutePlanOption())//
                                    .from(stNode)// 起点
                                    .to(enNode));// 终点
                            break;

                        case R.id.id_driving:// 驾车
                            mBaiduMap.clear();
                            rlu.initRouteLine(mBaiduMap);

                            // 设置起点终点信息
                            stNode = PlanNode.withLocation(MapActivity.mLastLocationData);// 从什么地方开始
                            enNode = PlanNode.withLocation(mapPoi.getPosition());// 终点

                            // 发起查询
                            rlu.mRoutePlanSearch.drivingSearch((new DrivingRoutePlanOption())//
                                    .from(stNode)// 起点
                                    .to(enNode));// 终点
                            break;

                        case R.id.id_Biking:// 骑行
                            mBaiduMap.clear();
                            rlu.initRouteLine(mBaiduMap);

                            // 设置起点终点信息
                            stNode = PlanNode.withLocation(MapActivity.mLastLocationData);// 从什么地方开始
                            enNode = PlanNode.withLocation(mapPoi.getPosition());// 终点

                            // 发起查询
                            rlu.mRoutePlanSearch.bikingSearch((new BikingRoutePlanOption())//
                                    .from(stNode)// 起点
                                    .to(enNode)// 终点
                                    .ridingType(0));// ridingType默认为普通骑行--0普通/1电动车
                            break;

                        case R.id.id_start_line:// 导航
                            LogMsgUtil.Log_D(TAG, "点击了导航");

                            mLeftNavigationPopup.initPopWindow(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int type = 0;
                                    switch (view.getId()) {
                                        case R.id.id_left_na_walking:// 步行导航
                                            type = 0;
                                            mBdNavigationUtils.initNavigations(MapActivity.mLastLocationData, mapPoi.getPosition(), type);
                                            mLeftNavigationPopup.dismiss();// 开启导航后关闭
                                            break;
                                        case R.id.id_left_na_biking:// 骑行导航
                                            type = 1;
                                            mBdNavigationUtils.initNavigations(MapActivity.mLastLocationData, mapPoi.getPosition(), type);
                                            mLeftNavigationPopup.dismiss();// 开启导航后关闭
                                            break;
                                    }
                                }
                            });
                            break;
                    }
                }
            });
            mBottomPopupWindow.setmTvSetLoc(mapPoi.getName());
            mBottomPopupWindow.setmTvDisplayLoc("距离" + map.get("mileage") + ",步行大约需要" + map.get("needTime") + "分钟");

            LogMsgUtil.Log_D(TAG, "MapEventUtils---------------点击了地图POI");
            return false;
        }
    };

    /**
     * 地图长按事件监听器
     */
    private BaiduMap.OnMapLongClickListener mMapLongClickListener = new BaiduMap.OnMapLongClickListener() {
        /**
         * 地图长按事件监听回调函数
         *
         * @param point 长按的地理坐标
         */
        @Override
        public void onMapLongClick(LatLng point) {
            // 在长按的地点打出标记
            addDestInfoOverlay(point);

            LogMsgUtil.Log_D(TAG, "MapEventUtils---------------长按了地图");
        }
    };

    /**
     * 地图覆盖物点击监听器
     */
    private BaiduMap.OnMarkerClickListener mMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        /**
         * 地图 Marker 覆盖物点击事件监听函数,只有在长按后生成覆盖物点击才有效
         * @param marker 被点击的 marker
         */
        public boolean onMarkerClick(final Marker marker) {
            Map map = MapUtil.getDistance(MapActivity.mLastLocationData, marker.getPosition());// 计算当前里程

            mBottomPopupWindow.initPopWindow(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RouteLineUtils rlu = new RouteLineUtils();
                    PlanNode stNode = null;
                    PlanNode enNode = null;

                    switch (view.getId()) {
                        case R.id.id_Walking:// 走路
                            mBaiduMap.clear();
                            rlu.initRouteLine(mBaiduMap);

                            // 设置起点终点信息
                            stNode = PlanNode.withLocation(MapActivity.mLastLocationData);// 从什么地方开始
                            enNode = PlanNode.withLocation(marker.getPosition());// 终点

                            // 发起查询
                            rlu.mRoutePlanSearch.walkingSearch((new WalkingRoutePlanOption())//
                                    .from(stNode)// 起点
                                    .to(enNode));// 终点
                            break;

                        case R.id.id_driving:// 驾车
                            mBaiduMap.clear();
                            rlu.initRouteLine(mBaiduMap);

                            // 设置起点终点信息
                            stNode = PlanNode.withLocation(MapActivity.mLastLocationData);// 从什么地方开始
                            enNode = PlanNode.withLocation(marker.getPosition());// 终点

                            // 发起查询
                            rlu.mRoutePlanSearch.drivingSearch((new DrivingRoutePlanOption())//
                                    .from(stNode)// 起点
                                    .to(enNode));// 终点
                            break;

                        case R.id.id_Biking:// 骑行
                            mBaiduMap.clear();
                            rlu.initRouteLine(mBaiduMap);

                            // 设置起点终点信息
                            stNode = PlanNode.withLocation(MapActivity.mLastLocationData);// 从什么地方开始
                            enNode = PlanNode.withLocation(marker.getPosition());// 终点

                            // 发起查询
                            rlu.mRoutePlanSearch.bikingSearch((new BikingRoutePlanOption())//
                                    .from(stNode)// 起点
                                    .to(enNode)// 终点
                                    .ridingType(0));// ridingType默认为普通骑行--0普通/1电动车
                            break;

                        case R.id.id_start_line:// 导航
                            LogMsgUtil.Log_D(TAG, "点击了导航");
                            mLeftNavigationPopup.initPopWindow(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int type = 0;
                                    switch (view.getId()) {
                                        case R.id.id_left_na_walking:// 步行导航
                                            type = 0;
                                            mBdNavigationUtils.initNavigations(MapActivity.mLastLocationData, marker.getPosition(), type);
                                            mLeftNavigationPopup.dismiss();// 开启导航后关闭
                                            break;
                                        case R.id.id_left_na_biking:// 骑行导航
                                            type = 1;
                                            mBdNavigationUtils.initNavigations(MapActivity.mLastLocationData, marker.getPosition(), type);
                                            mLeftNavigationPopup.dismiss();// 开启导航后关闭
                                            break;
                                    }
                                }
                            });
                            break;
                    }
                }
            });
            mBottomPopupWindow.setmTvSetLoc("已标记位置:" + marker.getTitle());
            mBottomPopupWindow.setmTvDisplayLoc("距离" + map.get("mileage") + ",步行大约需要" + map.get("needTime") + "分钟");
            return false;
        }
    };

    /**
     * 添加覆盖物
     *
     * @param ll
     */
    public void addDestInfoOverlay(LatLng ll) {
        mBaiduMap.clear();// 清除图层
        OverlayOptions optins = new MarkerOptions()//
                .position(ll)//
                .title("已标记位置")//
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.location))//
                .zIndex(5)// 设置图层层级
                .perspective(true);// 设置远大近小
        mBaiduMap.addOverlay(optins);// 在目标地点添加障碍物
    }
}
