package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.main.bean.FoodBlog;

/**
 * Created by Diviner on 2019/4/21.
 * Vesion:1.0
 */
public class FoodBlogDetailsDialog extends Dialog {
    private final String TAG = "FoodBlogDetailsDialog";
    private Activity mActivity;
    private FoodBlog mFoodBlog;

    private TextView mDetailsTitle;
    private TextView mDetailsContent;

    public FoodBlogDetailsDialog(Activity activity, FoodBlog foodBlog) {
        super(activity);
        mFoodBlog = foodBlog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_healthy_food_details);

        initView();
    }

    private void initView() {
        mDetailsTitle = (TextView) findViewById(R.id.id_tv_food_details_title);
        mDetailsTitle.setText(mFoodBlog.getfName());

        mDetailsContent = (TextView) findViewById(R.id.id_tv_food_details_content);
        mDetailsContent.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置内容过多可以滑动
        mDetailsContent.setText(mFoodBlog.getfContent());
    }
}
