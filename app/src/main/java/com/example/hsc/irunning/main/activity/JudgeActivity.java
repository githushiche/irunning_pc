package com.example.hsc.irunning.main.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;

/**
 * 判断是否是第一次进入,是的话进行推送
 * Created by Diviner
 * Time:2018/12/11.
 * Version:1.0
 */
public class JudgeActivity extends BaseActivity {
    private SharedPreferences mSharedPreferences;// 用于本地存储一些基本信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题栏

        ActivityCollectorUtils.addActivity(this);// 添加到活动管理器

        mSharedPreferences = getSharedPreferences("JudgeActivity_IsFirst_In", MODE_PRIVATE);// 声明一个文件存取对象

        boolean IS_FIRST = mSharedPreferences.getBoolean("isFirst", true);
        if (IS_FIRST) {
            /*
             * 如果是第一次进入则进入滑动页面
			 */
            Intent intent = new Intent(JudgeActivity.this, VpagerActivity.class);
            startActivity(intent);// 跳转到滑动推送界面
            ActivityCollectorUtils.remoActivity(this);
        } else {
			/*
			 * 否则进入欢迎界面
			 */
            Intent intent = new Intent(JudgeActivity.this,
                    WelcomeActivity.class);
            startActivity(intent);// 跳转到欢迎界面
            ActivityCollectorUtils.remoActivity(this);
        }
    }
}
