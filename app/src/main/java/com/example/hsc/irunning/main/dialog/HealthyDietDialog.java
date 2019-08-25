package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.utils.MapUtil;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.adapter.HealthyRecyclerAdapter;
import com.example.hsc.irunning.main.bean.HealthyRecycler;
import com.example.hsc.irunning.stepcount.bean.StepEntity;
import com.example.hsc.irunning.stepcount.db.StepDataDao;
import com.example.hsc.irunning.stepcount.utils.TimeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 饮食健康
 * Created by Diviner on 2019/4/4.
 */
public class HealthyDietDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "HealthyDietDialog";
    private Activity mActivity;

    private RecyclerView mHealthyRecyclerView;
    private HealthyRecyclerAdapter mHealthyRecyclerAdapter;
    private List<HealthyRecycler> mHealthyRecyclers = new ArrayList<>();
    private ImageView mIvBack;
    private TextView mTvKilometer, mTvSteps, mTvCals;

    private StepDataDao stepDataDao;
    private String curSelDate = "";

    public HealthyDietDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);// 进入效果
        mActivity = activity;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_healthy_layout);

        initData();
        initView();
        setDatas();
    }

    /**
     * 构建菜单子项
     */
    private void initData() {
        HealthyRecycler hr = new HealthyRecycler();
        hr = new HealthyRecycler(R.mipmap.around, "今日推荐菜品", 1);
        mHealthyRecyclers.add(hr);

        hr = new HealthyRecycler(R.mipmap.around, "营养菜品搭配", 2);
        mHealthyRecyclers.add(hr);

        hr = new HealthyRecycler(R.mipmap.around, "营养菜品推荐", 3);
        mHealthyRecyclers.add(hr);

        hr = new HealthyRecycler(R.mipmap.around, "美食博文推荐", 4);
        mHealthyRecyclers.add(hr);
    }

    private void initView() {
        stepDataDao = new StepDataDao(mActivity);

        mHealthyRecyclerView = (RecyclerView) findViewById(R.id.id_healthy_recycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);// 以什么方式来水平还是居中,一行几列
        mHealthyRecyclerView.setLayoutManager(layoutManager);

        mHealthyRecyclerAdapter = new HealthyRecyclerAdapter(mActivity, mHealthyRecyclers);
        mHealthyRecyclerView.setAdapter(mHealthyRecyclerAdapter);

        mIvBack = (ImageView) findViewById(R.id.id_iv_healthy_back);
        mIvBack.setOnClickListener(this);

        mTvKilometer = (TextView) findViewById(R.id.id_tv_healthy_kilometer);
        mTvSteps = (TextView) findViewById(R.id.id_tv_healthy_step);
        mTvCals = (TextView) findViewById(R.id.id_tv_healthy_cal);

        curSelDate = TimeUtil.getCurrentDate();
        LogMsgUtil.Log_D(TAG, "时间为" + curSelDate);
    }

    private void setDatas() {
        StepEntity stepEntity = stepDataDao.getCurDataByDate(curSelDate);

        if (stepEntity != null) {
            int steps = Integer.parseInt(stepEntity.getSteps());

            // 获取全局的步数
            mTvSteps.setText("您当前已经走了" + String.valueOf(steps) + "步");
            // 计算总公里数
            mTvKilometer.setText("您当前已经走了" + countTotalKM(steps) + "KM");

            // 计算出当前的cal消耗
            int cal = MapUtil.CalculationCalories(MainActivity.sUser.getuUserInfo().getuWeight(), steps);

            mTvCals.setText("您当前已经消耗了" + cal + " Cal");

        } else {
            // 获取全局的步数
            mTvSteps.setText("您当前已经走了0 步");
            // 计算总公里数
            mTvKilometer.setText("您当前已经走了0 KM");

            mTvCals.setText("您当前已经消耗了0 Cal");
        }
    }

    /**
     * 简易计算公里数，假设一步大约有0.7米
     *
     * @param steps 用户当前步数
     * @return
     */
    private String countTotalKM(int steps) {
        DecimalFormat df = new DecimalFormat("#.##");// 时间格式化
        double totalMeters = steps * 0.7;
        // 保留两位有效数字
        return df.format(totalMeters / 1000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_healthy_back:
                DialogCollectorUtils.remoActivity(this);
                break;
        }
    }
}
