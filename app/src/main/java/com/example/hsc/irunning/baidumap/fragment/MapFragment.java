package com.example.hsc.irunning.baidumap.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.api.track.DistanceResponse;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.TrackPoint;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.utils.BitmapUtil;
import com.example.hsc.irunning.baidumap.utils.MapEventUtils;
import com.example.hsc.irunning.baidumap.utils.MapSearchUtils;
import com.example.hsc.irunning.baidumap.utils.MapUtil;
import com.example.hsc.irunning.baidumap.utils.MyOrientationListener;
import com.example.hsc.irunning.baidumap.utils.TraceUtils;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 地图碎片-版本1.0
 *
 * @author Diviner
 * @date 2018-5-10 下午10:52:15
 */
public class MapFragment extends Fragment implements OnClickListener {
    private final String TAG = "MapFragment";
    private Activity mActivity;
    private Context mContext;
    private View mView;

    // 地图相关
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private UiSettings mMapUiSettings;

    // 方向传感器相关
    private MyOrientationListener mMyOrientationListener;
    private float mCurrenX;

    // 自定义定位图标相关
    private BitmapDescriptor mIconLocation;

    // 定位相关
    private LocationMode mLocationMode;// 模式
    private LocationClient mLocationClient;// 定位的核心api
    private MyLocationListener mMyLocationListener;// 自定义定位监听器
    private boolean mIsFirstLocation = true;
    public static LatLng mLastLocationData;// 起点
    public static LatLng mDestLocationData;// 终点
    private List<LatLng> trackPoints = new ArrayList<LatLng>();// 轨迹点集合

    // 布局控件相关
    private AutoCompleteTextView mAtQueryText;
    private ArrayAdapter<String> mAtAdapter = null;
    private SuggestionSearch mSuggestionSearch;// sug搜索对象
    private String mAddress;

    private Button mBtnStartTrace;// 开启轨迹
    private Button mBtnClearMap;// 清理地图
    private ImageView mIvSetting;// 设置
    private ImageView mIvMapModel;// 地图模式
    private ImageView mIvQueryInfo;
    private int MODEL_NUMBER = 0;// 0普通 1跟随 2罗盘

    public List<LatLng> mTrackPoints;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater
                .inflate(R.layout.fragment_map_layout, container, false);
        mActivity = getActivity();
        mContext = getContext();

        BitmapUtil.init();
        initView();
        initLocation();

        return mView;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTrackPoints = new ArrayList<>();
        mMapView = (MapView) mView.findViewById(R.id.id_map_view);
        mMapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                /*
                 * 父控件不截取子控件的事件 交给子控件自己处理
                 */
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:// 手势滑动View
                        // 当手势滑动这个控件的时候交给它自己来处理这个事件
                        mMapView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:// 手势抬起View（与DOWN对应）
                    case MotionEvent.ACTION_CANCEL:// 非人为原因结束本次事件
                        mMapView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        mBaiduMap = mMapView.getMap();// 得到当前地图，使当前的操作都是在已经打开的地图上操作

        MapEventUtils meu = new MapEventUtils();
        meu.initMapListener(mActivity, mBaiduMap);// 设置地图监听器

        mMapUiSettings = mBaiduMap.getUiSettings();
        mMapUiSettings.setCompassEnabled(false);// 是否显示指南针

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);// 设置地图的缩放比例
        mBaiduMap.setMapStatus(msu);// 更新

        // 在下方初始化控件
        mSuggestionSearch = SuggestionSearch.newInstance();// 创建检索实例
        mSuggestionSearch.setOnGetSuggestionResultListener(mSuggestionResultListener);

        mAtQueryText = (AutoCompleteTextView) mView.findViewById(R.id.id_query_line);

        mAtAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line);

        mAtQueryText.setAdapter(mAtAdapter);
        mAtQueryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 0) {
                    return;
                }
                String queryText = mAtQueryText.getText().toString();// 用户输入的信息

                //                List<Map<String, String>> table = MapUtil.addressResolution(mAddress);

                //                String info = table.get(0).get("province") + table.get(0).get("city") + table.get(0).get("county");

                mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                        .keyword(charSequence.toString())
                        .city("蒙自")
                        .citylimit(true));

                LogMsgUtil.Log_D(TAG, "搜索框信息------------>" + queryText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mIvSetting = (ImageView) mView.findViewById(R.id.id_Synthesis);
        mIvSetting.setOnClickListener(this);

        mIvMapModel = (ImageView) mView.findViewById(R.id.id_map_model);
        mIvMapModel.setOnClickListener(this);

        mIvQueryInfo = (ImageView) mView.findViewById(R.id.id_queryLine);
        mIvQueryInfo.setOnClickListener(this);

        mBtnStartTrace = (Button) mView.findViewById(R.id.id_start_trace);
        mBtnStartTrace.setOnClickListener(this);

        mBtnClearMap = (Button) mView.findViewById(R.id.id_clean_map);
        mBtnClearMap.setOnClickListener(this);
    }

    // sug监听器
    private OnGetSuggestionResultListener mSuggestionResultListener = new OnGetSuggestionResultListener() {
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            /*
             * SuggestionInfo中包含的信息有限，只包含：联想词，坐标点，POI的uid等
             * 如果想要POI的详细信息，还得利用uid通过mPoiSearch.searchPoiDetail获取
             */
            if (suggestionResult == null
                    || suggestionResult.getAllSuggestions() == null) {
                return;
            }
            mAtAdapter.clear();// 清理
            for (SuggestionResult.SuggestionInfo info : suggestionResult
                    .getAllSuggestions()) {
                if (info.key != null)
                    mAtAdapter.add(info.key);
            }
            // 详细信息
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAtAdapter.notifyDataSetChanged();// 告诉适配器来更新数据
                }
            });
        }
    };

    /**
     * 初始化定位
     */
    private void initLocation() {
        mMyLocationListener = new MyLocationListener();// 实例化监听器

        mLocationClient = new LocationClient(mContext);// 初始化定位
        mLocationClient.registerLocationListener(mMyLocationListener);// 注册监听器
        mLocationClient.setLocOption(MapUtil.settingLocationOptin());// 设置地图配置信息

        // 初始化我的位置图标
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_point);

        // 配置地图模式--普通、跟随、罗盘
        mLocationMode = LocationMode.NORMAL;

        mMyOrientationListener = new MyOrientationListener(mContext);
        mMyOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void OrientationChanged(float x) {
                // 当得到方向之后更新地图上方向图标的位置
                mCurrenX = x;
            }
        });
    }

    /**
     * 自定义定位监听器
     */
    public class MyLocationListener extends BDAbstractLocationListener {

        /**
         * 获取定位数据
         *
         * @param location
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {// 当定位销毁以后不在接受新的数据
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .direction(mCurrenX)// 当定位成功并获得方向传感器之后进行方向的设置
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())// 经度
                    .longitude(location.getLongitude())// 纬度
                    .build();
            mBaiduMap.setMyLocationData(locData);

            /*
             * 加载一个自定义图标,LocationMode.NORMAL正常模式
             * LocationMode.NORMAL模式，mIconLocation图标对象
             */
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfiguration(config);// 将initLocation中定义好的配置加载进来

            if (mIsFirstLocation) {// 是否为第一次
                /*
                 * 只有第一次进来的时候会定位到用户的中心点,也就是所在位置
                 */
                LatLng ll = null;
                ll = getMostAccuracyLocation(location);
                if (ll == null) {
                    return;
                }
                mIsFirstLocation = false;// 第一次之后设置为false

                // 添加点到点集合里面去
                trackPoints.add(ll);// 添加第一次的轨迹点

                // 每一次成功定位后的最新当前位置
                mLastLocationData = ll;

                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);// 传入当前位置
                mBaiduMap.animateMapStatus(msu);// 通过动态移动到当前定位区域

                mAddress = location.getAddrStr();// 得到当前地址,判断用户在哪里

                // 显示系列定位信息
                LogMsgUtil.LogMsg_D(TAG,
                        MapUtil.getLocationInformation(location));
            }
        }
    }


    /**
     * 来判断点的精度,精度太高直接过滤
     *
     * @param location
     * @return
     */
    private LatLng getMostAccuracyLocation(final BDLocation location) {

        if (location.getRadius() > 40) {// gps位置精度大于50米的点直接弃用，
            return null;
        }
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
        if (DistanceUtil.getDistance(mLastLocationData, ll) > 5) {
            mLastLocationData = ll;// 起始点的位置
            trackPoints.clear();// 有两点位置大于5，重新来过
            return null;
        }
        trackPoints.add(ll);
        mLastLocationData = ll;
        // 有5个连续的点之间的距离小于5，认为gps已稳定，以最新的点为起始点
        if (trackPoints.size() >= 5) {
            trackPoints.clear();
            return ll;
        }
        return null;
    }

    /**
     * 动态移动到当前位置
     *
     * @param point 当前经纬度
     * @param zoom  放大倍率
     */
    public void animateMapStatus(LatLng point, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        MapStatus mapStatus = builder.target(point).zoom(zoom).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                .newMapStatus(mapStatus));
    }

    final TraceUtils tu = new TraceUtils();
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            tu.queryTraces(new OnTrackListener() {
                @Override
                public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
                    super.onHistoryTrackCallback(historyTrackResponse);
                    int total = historyTrackResponse.getTotal();

                    LogMsgUtil.Log_D(TAG, "---------------轨迹点数量:" + historyTrackResponse.getSize() + "--------------");

                    if (historyTrackResponse.getSize() > 0) {// 说明有数据点

                        List<TrackPoint> points = historyTrackResponse.getTrackPoints();
                        for (TrackPoint trackPoint : points) {
                            if (!MapUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                mTrackPoints.add(MapUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    } else {
                        return;
                    }

                    //查找下一页数据
                    if (total > 1000 * 1) {

                    } else {
                        drawHistoryTrack(mTrackPoints);
                    }
                }

                @Override
                public void onDistanceCallback(DistanceResponse distanceResponse) {
                    AppToastUtils.toastShort(mContext, "---------------当前里程:" + (int) distanceResponse.getDistance() + "米");
                    super.onDistanceCallback(distanceResponse);
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_start_trace:// 开启轨迹服务
                if (mBtnStartTrace.getText().equals("开启轨迹记录")) {
                    mBtnStartTrace.setText(getString(R.string.menu_trajectory_on_string));// 设置为关闭轨迹

                    tu.initTrace(mContext);
                    tu.startTrace();

                    mTimer.schedule(mTimerTask, 2000);
                } else {
                    mBtnStartTrace.setText(getString(R.string.menu_trajectory_off_string));// 设置为开启轨迹

                    mTimer.cancel();// 取消定时器
                    tu.stopTrace();// 停止轨迹点采集
                }
                break;
            case R.id.id_clean_map:
                mBaiduMap.clear();
                break;
            case R.id.id_Synthesis:
                break;

            case R.id.id_map_model:// 地图模式
                if (MODEL_NUMBER == 0) {
                    mIvMapModel.setImageResource(R.mipmap.left_jpg0);
                    mLocationMode = LocationMode.NORMAL;// 普通
                    MODEL_NUMBER++;
                } else if (MODEL_NUMBER == 1) {
                    mIvMapModel.setImageResource(R.mipmap.left_jpg1);
                    mLocationMode = LocationMode.FOLLOWING;// 跟随
                    MODEL_NUMBER++;
                } else if (MODEL_NUMBER == 2) {
                    mIvMapModel.setImageResource(R.mipmap.left_jpg2);
                    mLocationMode = LocationMode.COMPASS;// 罗盘
                    MODEL_NUMBER = 0;
                }
                break;
            case R.id.id_queryLine:
                mBaiduMap.clear();// 清除图层上的障碍物

                MapSearchUtils msu = new MapSearchUtils(mBaiduMap);
                PoiSearch poiSearch = msu.initPoiSearch();

                // 发起城市POI建筑搜索
                poiSearch.searchInCity(new PoiCitySearchOption()//
                        .city("蒙自")// 城市名称
                        .isReturnAddr(true)//
                        .keyword(mAtQueryText.getText().toString().trim())// 关键字
                        .pageCapacity(10)//
                        .pageNum(10));//
                break;
        }
    }

    /**
     * 绘制轨迹路线
     *
     * @param points
     */
    public void drawHistoryTrack(List<LatLng> points) {
        // 清除上一次轨迹，避免重叠绘画
        mBaiduMap.clear();
        // 起始点图层也会被清除，重新绘画
        MarkerOptions oStart = new MarkerOptions();
        oStart.position(points.get(0));
        oStart.icon(BitmapUtil.mBmStart);
        mBaiduMap.addOverlay(oStart);

        // 将points集合中的点绘制轨迹线条图层，显示在地图上
        OverlayOptions ooPolyline = new PolylineOptions()//
                .width(20)// 线宽
                .color(0xAAFF0000)// 线的颜色
                .points(points);// 构成线的点集合

        // 路线
        Polyline mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);

        Marker marker;// 定义Marker标志对象
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()//
                .position(points.get(points.size() - 1))//
                .icon(BitmapUtil.mBmEnd);

        // 在地图上添加Marker，并显示
        marker = (Marker) (mBaiduMap.addOverlay(option));
        marker.setTitle("终点");
        marker.setPerspective(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        // onResume执行之后执行这个方法,在此方法中开启定位
        mBaiduMap.setMyLocationEnabled(true);// 地图开启定位的允许

        if (!mLocationClient.isStarted()) {// 如果没有启动则启动定位
            mLocationClient.start();

            // 开启方向传感器
            mMyOrientationListener.start();// 跟活动的生命周期一致

            animateMapStatus(mLastLocationData, 15.0f);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogMsgUtil.Log_D(TAG, "---------------地图已经onStop");
        mBaiduMap.setMyLocationEnabled(false);// 关闭应用之后地图不允许开启定位
        mLocationClient.stop();// 停止定位
        mMyOrientationListener.stop();// 关闭方向传感器
    }

    @Override
    public void onResume() {
        super.onResume();
        LogMsgUtil.Log_D(TAG, "---------------地图已经onResume");
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogMsgUtil.Log_D(TAG, "---------------地图已经onPause");
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogMsgUtil.Log_D(TAG, "---------------地图已经onDestroy");
        mMapView.onDestroy();
    }

}