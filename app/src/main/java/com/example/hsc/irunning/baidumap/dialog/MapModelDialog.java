package com.example.hsc.irunning.baidumap.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.hsc.irunning.R;


/**
 * 自定义dialog
 *
 * @author Diviner
 * @date 2018-1-23 下午3:12:30
 */
public abstract class MapModelDialog extends Dialog implements
        android.view.View.OnClickListener {
    private Activity mActivity;
    private Button mGeneralMap;
    private Button msatelliteMap;
    private Button mRealTimeTraffic;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public MapModelDialog(Activity activity) {
        super(activity, R.style.DialogCentre);
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_map_type);// 加载布局文件

        init();
    }

    public void init() {
        mGeneralMap = (Button) findViewById(R.id.general_map_id);
        mGeneralMap.setOnClickListener(this);

        msatelliteMap = (Button) findViewById(R.id.satellite_map_id);
        msatelliteMap.setOnClickListener(this);

        mRealTimeTraffic = (Button) findViewById(R.id.real_time_traffic_id);
        mRealTimeTraffic.setOnClickListener(this);
        setViewLocation();
        setCanceledOnTouchOutside(true);// 外部点击取消
    }

    /**
     * 设置dialog位于屏幕底部
     */
    private void setViewLocation() {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.general_map_id:
                MapMode(0);
                this.cancel();
                break;

            case R.id.satellite_map_id:
                MapMode(1);
                this.cancel();
                break;
            case R.id.real_time_traffic_id:
                MapMode(2);
                this.cancel();
                break;
        }
    }

    /**
     * 0表示普通地图,1表示卫星地图,2表示实时交通
     *
     * @param state
     */
    public abstract void MapMode(int state);
}
