package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.adapter.DishesAdapter;
import com.example.hsc.irunning.main.bean.Dishes;
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
 * 营养菜品
 * Created by Diviner on 2019/4/22.
 * Vesion:1.0
 */
public class DishesDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "DishesDialog";
    private Activity mActivity;

    // recyclerView适配器相关
    private List<Dishes> mDishes;
    private DishesAdapter mDishesAdapter;
    private RecyclerView mDishesRecycler;
    private ImageView mIvBack;

    public DishesDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_healthy_dishes_layout);

        mDishes = new ArrayList<>();

        initView();
        sendHttp();
    }

    private void initView() {
        mDishesRecycler = (RecyclerView) findViewById(R.id.id_rv_dishes_recycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mDishesRecycler.setLayoutManager(layoutManager);

        mDishesAdapter = new DishesAdapter(mActivity, mDishes);
        mDishesRecycler.setAdapter(mDishesAdapter);

        mIvBack = (ImageView) findViewById(R.id.id_iv_dishes_back);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_dishes_back:
                DialogCollectorUtils.remoActivity(this);
                break;
        }
    }

    private void sendHttp() {
        HttpUtils.dishesList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();

                    LogMsgUtil.Log_D(TAG, "json如下------------>" + json);

                    Gson gson = new Gson();
                    List<Dishes> dishesList = gson.fromJson(json,
                            new TypeToken<List<Dishes>>() {
                            }.getType());
                    sendData(dishesList);
                }
            }
        });
    }

    private void sendData(final List<Dishes> dishes) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDishesAdapter = new DishesAdapter(mActivity, dishes);
                mDishesRecycler.setAdapter(mDishesAdapter);
                mDishesAdapter.notifyDataSetChanged();
            }
        });
    }
}
