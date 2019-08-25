package com.example.hsc.irunning.baidumap.utils;

import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 路线规划
 * Created by Diviner on 2019/4/6.
 * Vesion:1.0
 */
public class RouteLineUtils {
    private final String TAG = "RouteLineUtils";
    private final int SHOW_INFORMATION = 100;
    private BaiduMap mBaiduMap;

    public RoutePlanSearch mRoutePlanSearch = null;

    public static List<WalkingRouteLine> mWalkingRouteLines = new ArrayList<>();// 步行规划路线
    public static List<TransitRouteLine> mTransitRouteLines = new ArrayList<>();// 公交规划路线
    public static List<DrivingRouteLine> mDrivingRouteLines = new ArrayList<>();// 驾驶规划路线
    public static List<BikingRouteLine> mBikingRouteLines = new ArrayList<>();// 自行车规划路线

    public void initRouteLine(BaiduMap baiduMap) {
        mBaiduMap = baiduMap;
        mRoutePlanSearch = RoutePlanSearch.newInstance();// 创建路线规划检索实例
        mRoutePlanSearch.setOnGetRoutePlanResultListener(mRoutePlanResultListener);
    }

    private OnGetRoutePlanResultListener mRoutePlanResultListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            // 步行路线
            if (walkingRouteResult == null
                    || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Message message = new Message();
                message.what = SHOW_INFORMATION;
                mHandler.sendMessage(message);
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mWalkingRouteLines = walkingRouteResult
                        .getRouteLines();// 获取规划路线列表
                WalkingRouteOverlay wro = new WalkingRouteOverlay(mBaiduMap);
                wro.setData(mWalkingRouteLines.get(0));
                wro.addToMap();
                wro.zoomToSpan();
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            // 公交路线
            if (transitRouteResult == null
                    || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Message message = new Message();
                message.what = SHOW_INFORMATION;
                mHandler.sendMessage(message);
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mTransitRouteLines = transitRouteResult
                        .getRouteLines();// 获取规划路线列表
            }
        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
            // 地铁路线
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
            // 驾驶路线
            if (drivingRouteResult == null
                    || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Message message = new Message();
                message.what = SHOW_INFORMATION;
                mHandler.sendMessage(message);
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mDrivingRouteLines = drivingRouteResult
                        .getRouteLines();// 获取规划路线列表

                DrivingRouteOverlay dro = new DrivingRouteOverlay(mBaiduMap);
                dro.setData(mDrivingRouteLines.get(0));
                dro.addToMap();
                dro.zoomToSpan();
            }
        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
            // 室内路线
        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
            // 自行车路线
            if (bikingRouteResult == null
                    || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Message message = new Message();
                message.what = SHOW_INFORMATION;
                mHandler.sendMessage(message);
            }
            if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                mBikingRouteLines = bikingRouteResult
                        .getRouteLines();// 获取规划路线列表

                BikingRouteOverlay bro = new BikingRouteOverlay(mBaiduMap);
                bro.setData(mBikingRouteLines.get(0));
                bro.addToMap();
                bro.zoomToSpan();
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_INFORMATION:
                    AppToastUtils.toastShort(BaseApplication.getsContext(), "抱歉!未查询到您要的结果!");
                    break;
            }
        }
    };

    public static List<WalkingRouteLine> getmWalkingRouteLines() {
        return mWalkingRouteLines;
    }

    public static List<TransitRouteLine> getmTransitRouteLines() {
        return mTransitRouteLines;
    }

    public static List<DrivingRouteLine> getmDrivingRouteLines() {
        return mDrivingRouteLines;
    }

    public static List<BikingRouteLine> getmBikingRouteLines() {
        return mBikingRouteLines;
    }


}
