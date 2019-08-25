package com.example.hsc.irunning.baidumap.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.hsc.irunning.R;

/**
 * 底部弹出框-显示地图信息
 * Created by Diviner on 2019/4/8.
 * Vesion:1.0
 */
public class BottomPopup extends PopupWindow {
    private Context mContext;
    public View mView;

    private TextView mTvSetLoc;
    private TextView mTvDisplayLoc;
    private Button mBtnWalking;
    private Button mBtnDriving;
    private Button mBtnBiking;
    private Button mBtnNavigation;

    public BottomPopup(Context context) {
        this.mContext = context;
    }

    /**
     * 传入监听器以实现回调
     *
     * @param itemOnClick
     */
    public void initPopWindow(View.OnClickListener itemOnClick) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.popup_bottom_layout, null);

        mTvSetLoc = (TextView) mView.findViewById(R.id.id_set_location);
        mTvDisplayLoc = (TextView) mView.findViewById(R.id.id_display_location);

        mBtnWalking = (Button) mView.findViewById(R.id.id_Walking);
        mBtnWalking.setOnClickListener(itemOnClick);

        mBtnDriving = (Button) mView.findViewById(R.id.id_driving);
        mBtnDriving.setOnClickListener(itemOnClick);

        mBtnBiking = (Button) mView.findViewById(R.id.id_Biking);
        mBtnBiking.setOnClickListener(itemOnClick);

        mBtnNavigation = (Button) mView.findViewById(R.id.id_start_line);
        mBtnNavigation.setOnClickListener(itemOnClick);

        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);// 根据布局来看 有多高就多高
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);// 必须设置 不然布局内按钮使用不了
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.animTranslate);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        this.showAtLocation(mView, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);// 显示pop
    }

    public void setmTvSetLoc(String disinfo) {
        this.mTvSetLoc.setText(disinfo);
    }

    public void setmTvDisplayLoc(String disinfo) {
        this.mTvDisplayLoc.setText(disinfo);
    }
}
