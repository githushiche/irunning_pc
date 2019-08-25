package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.adapter.DynamicAdapter;
import com.example.hsc.irunning.main.bean.Dynamic;
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
 * 动态弹出框
 *
 * @author Diviner
 * @date 2018-5-8 下午10:48:55
 */
public class DynamicDialog extends Dialog implements OnClickListener {
    private final String TAG = "DynamicDialog";
    private Activity mActivity;
    private static LinearLayout mDynamicProgress;
    private static RecyclerView mDynamicRecyclerView;
    private static DynamicAdapter mDynamicAdapter;
    private static List<Dynamic> mDynamics = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView mIvBack;
    private ImageView mIvDynamicAdd;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public DynamicDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_dynamic_list);

        init();
    }

    /**
     * 初始化控件
     */
    public void init() {
        mDynamicProgress = (LinearLayout) findViewById(R.id.id_pb_dynamic_progress);

        mIvBack = (ImageView) findViewById(R.id.id_iv_dynamic_back);
        mIvBack.setOnClickListener(this);

        mIvDynamicAdd = (ImageView) findViewById(R.id.id_iv_dynamic_add);
        mIvDynamicAdd.setOnClickListener(this);

        mDynamicRecyclerView = (RecyclerView) findViewById(R.id.id_dynamic_recycler);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mDynamicRecyclerView.setLayoutManager(layoutManager);

        setData();

        mDynamicAdapter = new DynamicAdapter(mDynamics);
        mDynamicRecyclerView.setAdapter(mDynamicAdapter);// 设置适配器

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_srl_fragment_dynamic);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
            }
        });
    }

    /**
     * 初始化动态数据
     */
    private void setData() {
        HttpUtils.getDynamicRequest(String.valueOf(MainActivity.sUser.getuId()), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();

                    LogMsgUtil.Log_D(TAG, "json-=----" + json);

                    Gson gson = new Gson();
                    mDynamics = gson.fromJson(json,
                            new TypeToken<List<Dynamic>>() {
                            }.getType());
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDynamicAdapter = new DynamicAdapter(mDynamics);
                            mDynamicRecyclerView.setAdapter(mDynamicAdapter);// 设置适配器
                            mDynamicAdapter.notifyDataSetChanged();

                            mSwipeRefreshLayout.setRefreshing(false);

                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            mDynamicProgress.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_dynamic_add:
                new DynamicAddDialog(mActivity).show();
                break;

            case R.id.id_iv_dynamic_back:
                dismiss();
                break;
        }
    }

    public static void addAdapter(Dynamic dynamic) {
        mDynamics.add(dynamic);
        mDynamicAdapter = new DynamicAdapter(mDynamics);
        mDynamicRecyclerView.setAdapter(mDynamicAdapter);// 设置适配器
        mDynamicAdapter.notifyDataSetChanged();
    }

    public static void updateAdapter(List<Dynamic> dynamics) {
        mDynamics = dynamics;
        mDynamicAdapter = new DynamicAdapter(mDynamics);
        mDynamicRecyclerView.setAdapter(mDynamicAdapter);// 设置适配器
        mDynamicAdapter.notifyDataSetChanged();
    }
}
