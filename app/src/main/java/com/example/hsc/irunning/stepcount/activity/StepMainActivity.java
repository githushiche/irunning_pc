package com.example.hsc.irunning.stepcount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;
import com.example.hsc.irunning.main.activity.BaseActivity;
import com.example.hsc.irunning.main.activity.VideoActivity;
import com.example.hsc.irunning.stepcount.bean.StepEntity;
import com.example.hsc.irunning.stepcount.db.StepDataDao;
import com.example.hsc.irunning.stepcount.utils.BeforeOrAfterCalendarView;
import com.example.hsc.irunning.stepcount.utils.StepCountCheckUtil;
import com.example.hsc.irunning.stepcount.utils.TimeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 计步实现
 *
 * @author Diviner
 * @date 2018-5-13 下午10:47:07
 */
public class StepMainActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "StepMainActivity";

    private LinearLayout movementCalenderLl;
    private TextView kmTimeTv;
    private TextView mTotalKmTv;// 显示公里数
    private TextView stepsTimeTv;
    private TextView mTotalStepsTv;// 显示的步数
    private TextView supportTv;
    private Button mBtnFitnessVideo;
    private ImageView mIvBack;

    /**
     * 屏幕长度和宽度
     */
    public static int screenWidth, screenHeight;

    private BeforeOrAfterCalendarView calenderView;

    private String curSelDate;
    private DecimalFormat df = new DecimalFormat("#.##");// 时间格式化
    private List<StepEntity> stepEntityList = new ArrayList<StepEntity>();
    private StepDataDao stepDataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_step_layout);
        ActivityCollectorUtils.addActivity(this);

        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 顶部LinearLayout效果实现
        movementCalenderLl = (LinearLayout) findViewById(R.id.movement_records_calender_ll);

        kmTimeTv = (TextView) findViewById(R.id.movement_total_km_time_tv);
        mTotalKmTv = (TextView) findViewById(R.id.movement_total_km_tv);
        stepsTimeTv = (TextView) findViewById(R.id.movement_total_steps_time_tv);
        mTotalStepsTv = (TextView) findViewById(R.id.movement_total_steps_tv);
        supportTv = (TextView) findViewById(R.id.is_support_tv);

        curSelDate = TimeUtil.getCurrentDate();

        mBtnFitnessVideo = (Button) findViewById(R.id.id_btn_step_fitness_video);
        mBtnFitnessVideo.setOnClickListener(this);

        mIvBack = (ImageView) findViewById(R.id.id_iv_step_back);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_step_fitness_video:
                Intent intent = new Intent(StepMainActivity.this, VideoActivity.class);
                startActivity(intent);
                break;
            case R.id.id_iv_step_back:
                ActivityCollectorUtils.remoActivity(this);
                break;
        }
    }

    private void initData() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        // 放到获取宽度之后
        calenderView = new BeforeOrAfterCalendarView(this);
        movementCalenderLl.addView(calenderView);
        /**
         * 这里判断当前设备是否支持计步
         */
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            getRecordList();
            supportTv.setVisibility(View.GONE);
            mTimer.schedule(mTimerTask, 1000, 3000);
        } else {
            mTotalStepsTv.setText("0");
            supportTv.setVisibility(View.VISIBLE);
        }
    }

    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setDatas();
                }
            });
        }
    };

    private void initListener() {
        calenderView
                .setOnBoaCalenderClickListener(new BeforeOrAfterCalendarView.BoaCalenderClickListener() {
                    @Override
                    public void onClickToRefresh(int position, String curDate) {
                        // 获取当前选中的时间
                        curSelDate = curDate;
                        // 根据日期去取数据
                        setDatas();
                    }
                });
    }

    /**
     * 设置记录数据
     */
    private void setDatas() {
        StepEntity stepEntity = stepDataDao.getCurDataByDate(curSelDate);

        if (stepEntity != null) {
            int steps = Integer.parseInt(stepEntity.getSteps());

            // 获取全局的步数
            mTotalStepsTv.setText(String.valueOf(steps));
            // 计算总公里数
            mTotalKmTv.setText(countTotalKM(steps));
        } else {
            // 获取全局的步数
            mTotalStepsTv.setText("0");
            // 计算总公里数
            mTotalKmTv.setText("0");
        }

        // 设置时间
        String time = TimeUtil.getWeekStr(curSelDate);
        kmTimeTv.setText(time);
        stepsTimeTv.setText(time);
    }

    /**
     * 简易计算公里数，假设一步大约有0.7米
     *
     * @param steps 用户当前步数
     * @return
     */
    private String countTotalKM(int steps) {
        double totalMeters = steps * 0.7;
        // 保留两位有效数字
        return df.format(totalMeters / 1000);
    }

    /**
     * 获取全部运动历史纪录
     */
    private void getRecordList() {
        // 获取数据库
        stepDataDao = new StepDataDao(this);
        stepEntityList.clear();
        stepEntityList.addAll(stepDataDao.getAllDatas());
        if (stepEntityList.size() >= 7) {
            // TODO: 2017/3/27 在这里获取历史记录条数，当条数达到7条或以上时，就开始删除第七天之前的数据,暂未实现
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        ActivityCollectorUtils.remoActivity(this);
    }
}
