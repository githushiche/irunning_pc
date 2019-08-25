package com.example.hsc.irunning.baidumap.utils;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 地图一系列查询帮助类
 * Created by Diviner on 2019/4/10.
 * Vesion:1.0
 */
public class MapSearchUtils {
    private final String TAG = "MapSearchUtils";
    private Context mContext;

    private BaiduMap mBaiduMap;

    // POI检索相关
    private PoiSearch mPoiSearch;// poi检索对象
    private List<PoiInfo> mPoiInfos;// poi列表
    private List<PoiDetailResult> mPoiDetailResult;// 发起详细查询结果

    public MapSearchUtils(BaiduMap baiduMap) {
        this.mBaiduMap = baiduMap;
    }

    public PoiSearch initPoiSearch() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch
                .setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

                    @Override
                    public void onGetPoiResult(PoiResult result) {
                        mBaiduMap.clear();
                        // 如果搜索到的结果不为空，并且没有错误
                        if (result != null
                                && result.error == PoiResult.ERRORNO.NO_ERROR) {

                            mPoiInfos = result.getAllPoi();// poi列表

                            PoiOverlay overlay = new PoiOverlay(mBaiduMap);
                            overlay.setData(result);
                            overlay.addToMap();
                            overlay.zoomToSpan();

                            // 设置标记物的点击监听事件
                            mBaiduMap.setOnMarkerClickListener(overlay);
                        } else if (result.error == PoiResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                            String strInfo = "在";
                            for (CityInfo cityInfo : result
                                    .getSuggestCityList()) {
                                strInfo += cityInfo.city;
                                strInfo += ",";
                            }
                            strInfo += "找到结果";
                            AppToastUtils.toastShort(mContext, strInfo);
                        } else {
                            AppToastUtils.toastShort(mContext, "搜索不到你需要的信息！");
                        }
                    }

                    @Override
                    public void onGetPoiIndoorResult(PoiIndoorResult result) {
                    }

                    @Override
                    public void onGetPoiDetailResult(PoiDetailResult result) {
                        if (result.error != result.error.NO_ERROR) {
                            AppToastUtils.toastShort(mContext, "抱歉，未找到结果");
                        } else {// 正常返回结果的时候，此处可以获得很多相关信息
                            // 获取Place详情页检索结果
                            LogMsgUtil.Log_D(mContext.toString(),
                                    "正常返回:" + result.getAddress() + "-"
                                            + result.getName()
                                            + result.getUid());

                            mPoiDetailResult = new ArrayList<PoiDetailResult>();
                            mPoiDetailResult.add(result);// 将详细查找结果添加到list
                        }
                    }

                    @Override
                    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

                    }
                });

        return mPoiSearch;
    }
}
