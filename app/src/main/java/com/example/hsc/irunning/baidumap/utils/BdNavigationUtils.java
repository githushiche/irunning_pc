package com.example.hsc.irunning.baidumap.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLaunchParam;
import com.baidu.mapapi.bikenavi.params.BikeRouteNodeInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.baidu.mapapi.walknavi.params.WalkRouteNodeInfo;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.activity.BNaviGuideActivity;
import com.example.hsc.irunning.baidumap.activity.WNaviGuideActivity;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.command.utils.PhoneUtil;

/**
 * Created by Diviner on 2019/4/10.
 * Vesion:1.0
 */
public class BdNavigationUtils {
    private final String TAG = "BdNavigationUtils";
    private Activity mActivity;
    private BaiduMap mBaiduMap;

    /*导航起终点Marker，可拖动改变起终点的坐标*/
    private Marker mStartMarker;
    private Marker mEndMarker;

    private LatLng mStartPoint;
    private LatLng mEndPoint;

    // 图标
    private BitmapDescriptor bdStart = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_start);
    private BitmapDescriptor bdEnd = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_end);

    // 起终点
    private BikeNaviLaunchParam bikeParam;
    private WalkNaviLaunchParam walkParam;

    // SD卡相关
    private static String authinfo = null;
    private String APP_FOLDER_NAME = "BD_daohang";
    private String ROUTE_PLAN_NODE = "routePlanNode";
    private String mTTSAppId = "15878679";
    private boolean hasInitSuccess;


    public BdNavigationUtils(Activity activity, BaiduMap baiduMap) {
        this.mActivity = activity;
        this.mBaiduMap = baiduMap;
    }

    /**
     * 初始化导航配置
     *
     * @return
     */
    public boolean initSetting() {
        BaiduNaviManagerFactory.getBaiduNaviManager().init(mActivity,
                PhoneUtil.getSDcardDir(), APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";

                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        LogMsgUtil.Log_D(TAG, "BdNavigation-------------->" + authinfo);
                    }

                    @Override
                    public void initStart() {
                        LogMsgUtil.Log_D(TAG, "BdNavigation-------------->百度导航引擎初始化开始" + authinfo);
                    }

                    @Override
                    public void initSuccess() {
                        LogMsgUtil.Log_D(TAG, "BdNavigation-------------->百度导航引擎初始化成功" + authinfo);
                        hasInitSuccess = true;

                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed() {
                        LogMsgUtil.Log_D(TAG, "BdNavigation-------------->百度导航引擎初始化失败" + authinfo);
                    }

                });
        return hasInitSuccess;
    }

    /**
     * @param stPoint 起点
     * @param enPoint 终点
     * @param type    开启什么导航--0步行--1骑行
     */
    public void initNavigations(LatLng stPoint, LatLng enPoint, int type) {

        /*构造导航起终点参数对象*/
        BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
        bikeStartNode.setLocation(stPoint);
        BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
        bikeEndNode.setLocation(enPoint);
        bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

        WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
        walkStartNode.setLocation(stPoint);
        WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
        walkEndNode.setLocation(enPoint);
        walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

        if (type == 0) {
            walkParam.extraNaviMode(0);// 普通步行
            startWalkNavi();
        } else {
            startBikeNavi();// 骑行
        }
        mStartPoint = stPoint;
        mEndPoint = enPoint;

        /* 初始化起终点Marker */
        initOverlay();
        initTTS();
    }

    private void initTTS() {

        BaiduNaviManagerFactory.getTTSManager().initTTS(mActivity.getApplicationContext(), PhoneUtil.getSDcardDir(),
                APP_FOLDER_NAME, mTTSAppId);

        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(new IBNTTSManager.IOnTTSPlayStateChangedListener() {
            @Override
            public void onPlayStart() {
                LogMsgUtil.Log_D(TAG, "tts开始");
            }

            @Override
            public void onPlayEnd(String s) {
                LogMsgUtil.Log_D(TAG, "tts结束");
            }

            @Override
            public void onPlayError(int i, String s) {
                LogMsgUtil.Log_D(TAG, "tts错误");
            }
        });

        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                LogMsgUtil.Log_D(TAG, "ttsHandler.msg.what=" + msg.what);
            }
        });
    }

    /**
     * 初始化导航起终点Marker
     */
    public void initOverlay() {

        MarkerOptions ooA = new MarkerOptions().position(mStartPoint).icon(bdStart)
                .zIndex(9).draggable(true);
        mStartMarker = (Marker) (mBaiduMap.addOverlay(ooA));
        mStartMarker.setDraggable(true);

        mBaiduMap.clear();

        MarkerOptions ooB = new MarkerOptions().position(mEndPoint).icon(bdEnd)
                .zIndex(5);
        mEndMarker = (Marker) (mBaiduMap.addOverlay(ooB));
        mEndMarker.setDraggable(true);

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                if (marker == mStartMarker) {
                    mStartPoint = marker.getPosition();
                } else if (marker == mEndMarker) {
                    mEndPoint = marker.getPosition();
                }

                BikeRouteNodeInfo bikeStartNode = new BikeRouteNodeInfo();
                bikeStartNode.setLocation(mStartPoint);
                BikeRouteNodeInfo bikeEndNode = new BikeRouteNodeInfo();
                bikeEndNode.setLocation(mEndPoint);
                bikeParam = new BikeNaviLaunchParam().startNodeInfo(bikeStartNode).endNodeInfo(bikeEndNode);

                WalkRouteNodeInfo walkStartNode = new WalkRouteNodeInfo();
                walkStartNode.setLocation(mStartPoint);
                WalkRouteNodeInfo walkEndNode = new WalkRouteNodeInfo();
                walkEndNode.setLocation(mEndPoint);
                walkParam = new WalkNaviLaunchParam().startNodeInfo(walkStartNode).endNodeInfo(walkEndNode);

            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 开始骑行导航
     */
    private void startBikeNavi() {
        LogMsgUtil.Log_D(TAG, "--------------------startBikeNavi");
        try {
            BikeNavigateHelper.getInstance().initNaviEngine(mActivity, new IBEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    LogMsgUtil.Log_D(TAG, "--------------------引擎初始化成功");
                    routePlanWithBikeParam();
                }

                @Override
                public void engineInitFail() {
                    LogMsgUtil.Log_D(TAG, "--------------------引擎初始化失败");
                    BikeNavigateHelper.getInstance().unInitNaviEngine();
                }
            });
        } catch (Exception e) {
            LogMsgUtil.Log_D(TAG, "--------------------startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 开始步行导航
     */
    private void startWalkNavi() {
        LogMsgUtil.Log_D(TAG, "--------------------开始步行导航");
        try {
            WalkNavigateHelper.getInstance().initNaviEngine(mActivity, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    LogMsgUtil.Log_D(TAG, "--------------------引擎初始化成功");
                    routePlanWithWalkParam();
                }

                @Override
                public void engineInitFail() {
                    LogMsgUtil.Log_D(TAG, "--------------------引擎初始化失败");
                    WalkNavigateHelper.getInstance().unInitNaviEngine();
                }
            });
        } catch (Exception e) {
            LogMsgUtil.Log_D(TAG, "--------------------startBikeNavi Exception");
            e.printStackTrace();
        }
    }

    /**
     * 发起骑行导航算路
     */
    private void routePlanWithBikeParam() {
        BikeNavigateHelper.getInstance().routePlanWithRouteNode(bikeParam, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "骑行导航计划开始");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d(TAG, "骑行导航计划成功");
                Intent intent = new Intent();
                intent.setClass(mActivity, BNaviGuideActivity.class);
                mActivity.startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d(TAG, "骑行导航计划成失败");
            }

        });
    }

    /**
     * 发起步行导航算路
     */
    private void routePlanWithWalkParam() {
        WalkNavigateHelper.getInstance().routePlanWithRouteNode(walkParam, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d(TAG, "步行导航计划开始");
            }

            @Override
            public void onRoutePlanSuccess() {

                Log.d(TAG, "步行导航计划成功");

                Intent intent = new Intent();
                intent.setClass(mActivity, WNaviGuideActivity.class);
                mActivity.startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d(TAG, "步行导航计划失败");
            }
        });
    }
}
