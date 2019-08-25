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

import com.example.hsc.irunning.R;

/**
 * Created by Diviner on 2019/4/10.
 * Vesion:1.0
 */
public class LeftNavigationPopup extends PopupWindow {
    private final String TAG = "LeftNavigationPopup";
    private Context mContext;
    public View mView;

    private Button mBtnWalking;
    private Button mBtnBiking;

    public LeftNavigationPopup(Context context) {
        this.mContext = context;
    }

    /**
     * 传入监听器以实现回调
     *
     * @param itemOnClick
     */
    public void initPopWindow(View.OnClickListener itemOnClick) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.popup_left_navigation_layout, null);

        mBtnWalking = (Button) mView.findViewById(R.id.id_left_na_walking);
        mBtnWalking.setOnClickListener(itemOnClick);

        mBtnBiking = (Button) mView.findViewById(R.id.id_left_na_biking);
        mBtnBiking.setOnClickListener(itemOnClick);

        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);// 根据布局来看 有多高就多高
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);// 必须设置 不然布局内按钮使用不了
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.animTranslateLeft);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.WHITE);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        int[] location = new int[2];
        mView.getLocationOnScreen(location);

        this.showAtLocation(mView, Gravity.NO_GRAVITY, location[0] - this.getWidth(), location[1]);// 显示pop
    }

}
