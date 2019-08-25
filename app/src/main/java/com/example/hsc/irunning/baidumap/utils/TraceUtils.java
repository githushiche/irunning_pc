package com.example.hsc.irunning.baidumap.utils;

import android.content.Context;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.example.hsc.irunning.command.utils.LogMsgUtil;

/**
 * 鹰眼帮助类
 * Created by Diviner on 2019/4/5.
 * Vesion:1.0
 */
public class TraceUtils {
    private final String TAG = "TraceUtils";
    private Context mContext;

    private final long mServerId = 211129;    // 设备标识
    private String mEntityName = "myTraceTest";// 设备名称
    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    private boolean isNeedObjectStorage = false;
    public static Trace mTrace;// 轨迹服务
    public static LBSTraceClient mTraceClient = null;// 轨迹客户端
    private int gatherInterval = 5;    // 定位周期(单位:秒)
    private int packInterval = 10;    // 打包回传周期(单位:秒)

    private int mTag = 1;    // 请求标识
    private HistoryTrackRequest mHistoryTrackRequest;    // 创建历史轨迹请求实例
    private long mStartTime = System.currentTimeMillis() / 1000 - 12 * 60 * 60;    // 开始时间(单位：秒)
    private long mEndTime = System.currentTimeMillis() / 1000;    // 结束时间(单位：秒)


    /**
     * 实例化并开启轨迹
     *
     * @param context
     */
    public void initTrace(Context context) {
        mContext = context;
        // 初始化轨迹服务
        mTrace = new Trace(mServerId, mEntityName, isNeedObjectStorage);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(mContext);

        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
    }

    private OnTraceListener mTraceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int i, String s) {

        }

        @Override
        public void onStartTraceCallback(int code, String message) {
            /**
             * 因为startTrace与startGather是异步执行，且startGather依赖startTrace执行开启服务成功，
             * 所以建议startGather在public void onStartTraceCallback(int errorNo, String message)回调返回错误码为0后，
             * 再进行调用执行，否则会出现服务开启失败12002的错误。
             */
            if (code == 0) {
                // 开启采集
                mTraceClient.startGather(mTraceListener);
                LogMsgUtil.Log_D(TAG, "---------------------开始轨迹服务---------------------");
            }
            return;
        }

        @Override
        public void onStopTraceCallback(int i, String s) {
            LogMsgUtil.Log_D(TAG, "---------------------停止轨迹服务---------------------");
        }

        @Override
        public void onStartGatherCallback(int i, String s) {
            LogMsgUtil.Log_D(TAG, "---------------------开始轨迹采集---------------------");
        }

        @Override
        public void onStopGatherCallback(int i, String s) {
            LogMsgUtil.Log_D(TAG, "---------------------停止轨迹采集---------------------");
        }

        @Override
        public void onPushCallback(byte b, PushMessage pushMessage) {

        }

        @Override
        public void onInitBOSCallback(int i, String s) {

        }
    };

    /**
     * 开启轨迹服务
     */
    public void startTrace() {
        mTraceClient.startTrace(mTrace, mTraceListener);// 开启轨迹记录
    }

    /**
     * 此方法将同时停止轨迹服务和轨迹采集，完全结束鹰眼轨迹服务。若需再次启动轨迹追踪，需重新启动服务和轨迹采集
     */
    public void stopTrace() {
        mTraceClient.stopTrace(mTrace, mTraceListener);// 关闭轨迹记录
    }

    /**
     * 只停止轨迹采集,不关闭轨迹服务,再次开启可以调用startTrace()
     */
    public void stopGather() {
        mTraceClient.stopGather(mTraceListener);
    }

    /**
     * 初始化轨迹查询
     */
    private void initQueryTraces() {
        mHistoryTrackRequest = new HistoryTrackRequest(mTag, mServerId, mEntityName);
        mHistoryTrackRequest.setStartTime(mStartTime);// 设置开始时间
        mHistoryTrackRequest.setEndTime(mEndTime);// 设置结束时间
    }

    /**
     * 查询轨迹
     *
     * @param listener
     */
    public void queryTraces(OnTrackListener listener) {
        initQueryTraces();
        mTraceClient.queryHistoryTrack(mHistoryTrackRequest, listener);
    }

}
