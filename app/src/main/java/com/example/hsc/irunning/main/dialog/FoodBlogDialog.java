package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.adapter.FoodBlogAdapter;
import com.example.hsc.irunning.main.bean.FoodBlog;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 美食博文
 * Created by Diviner on 2019/4/21.
 * Vesion:1.0
 */
public class FoodBlogDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "FoodBlogDialog";

    private Activity mActivity;

    // recyclerView适配器相关
    private RecyclerView mFoodBlogRecycler;
    private FoodBlogAdapter mFoodBlogAdapter;
    private List<FoodBlog> mFoodBlogs;
    private ImageView mIvBack;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public FoodBlogDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_healthy_food_layout);

        mFoodBlogs = new ArrayList<>();

        initView();
        sendHttp();
    }

    private void initView() {
        mFoodBlogRecycler = (RecyclerView) findViewById(R.id.id_rv_foodblogs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mFoodBlogRecycler.setLayoutManager(layoutManager);

        mFoodBlogAdapter = new FoodBlogAdapter(mActivity, mFoodBlogs);
        mFoodBlogRecycler.setAdapter(mFoodBlogAdapter);

        mIvBack = (ImageView) findViewById(R.id.id_iv_food_back);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_food_back:
                DialogCollectorUtils.remoActivity(this);
                break;
        }
    }

    private void sendHttp() {
        HttpUtils.foodBlogList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();

                    LogMsgUtil.Log_D(TAG, "--->" + json);

                    Gson gson = new Gson();
                    List<FoodBlog> foodBlogs = gson.fromJson(json,
                            new TypeToken<List<FoodBlog>>() {
                            }.getType());
                    //                    mFoodBlogs = foodBlogs;
                    setData(foodBlogs);
                }
            }
        });
    }

    private void setData(final List<FoodBlog> foodBlogs) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFoodBlogAdapter = new FoodBlogAdapter(mActivity, foodBlogs);
                mFoodBlogRecycler.setAdapter(mFoodBlogAdapter);
                mFoodBlogAdapter.notifyDataSetChanged();
            }
        });
    }
}
