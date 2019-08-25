package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.main.bean.Dishes;

/**
 * Created by Diviner on 2019/4/22.
 * Vesion:1.0
 */
public class DishesDetailsDialog extends Dialog {
    private final String TAG = "DishesDetailsDialog";
    private Activity mActivity;

    // recyclerView适配器相关
    private Dishes mDishes;

    private ImageView mIvDetailsImg;
    private TextView mTvDetailsTitle;
    private TextView mTvDetailsContent;
    private TextView mTvDetailsIntroduction;

    public DishesDetailsDialog(Activity activity, Dishes dishes) {
        super(activity);
        this.mActivity = activity;
        DialogCollectorUtils.addDialog(this);

        mDishes = dishes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_healthy_dishes_details);

        initView();
    }

    private void initView() {
        mIvDetailsImg = (ImageView) findViewById(R.id.id_tv_dishes_details_img);
        Glide.with(mActivity)
                .load(mDishes.getdImg())
                .dontAnimate()
                .into(mIvDetailsImg);

        mTvDetailsTitle = (TextView) findViewById(R.id.id_tv_dishes_details_title);
        mTvDetailsTitle.setText(mDishes.getdName());

        mTvDetailsContent = (TextView) findViewById(R.id.id_tv_dishes_details_content);
        mTvDetailsContent.setText(mDishes.getdMaterial());

        mTvDetailsIntroduction = (TextView) findViewById(R.id.id_tv_dishes_details_introduction);
        mTvDetailsIntroduction.setText(mDishes.getdPracticeIntroduction());
    }
}
