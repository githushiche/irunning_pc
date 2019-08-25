package com.example.hsc.irunning.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎界面
 * Created by Diviner
 * Time:2018/12/11.
 * Version:1.0
 */
public class WelcomeActivity extends BaseActivity {
    private final String TAG = "WelcomeActivity";
    private Timer mTimer = new Timer();// 创建一个定时器
    private TimerTask mTTask = new TimerTask() {

        @Override
        public void run() {
            Intent intent = new Intent(WelcomeActivity.this,
                    LoginActivity.class);
            startActivity(intent);// 跳转到登陆界面
            ActivityCollectorUtils.remoActivity(WelcomeActivity.this);// 关闭活动
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题框
        setContentView(R.layout.welcome_layout);

        ActivityCollectorUtils.addActivity(this);

        mTimer.schedule(mTTask, 2000);// 2秒后进行跳转
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTimer.cancel();// 关闭线程
    }
}
