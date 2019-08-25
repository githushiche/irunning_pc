package com.example.hsc.irunning.main.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.hsc.irunning.main.fragment.vPageFragment;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;

/**
 * 推送滑动界面
 * Created by Diviner
 * Time:2018/12/11.
 * Version:1.0
 */
public class VpagerActivity extends BaseActivity {
    private SharedPreferences.Editor mEditor;

    protected android.support.v4.app.Fragment createFragment() {
        return new vPageFragment();// 新建一个碎片
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.mIsFragment = true;// 如果是碎片的话就执行
        super.onCreate(savedInstanceState);
        ActivityCollectorUtils.addActivity(this);// 添加到活动管理器
        /*
         * 先到本地数据文件里面取数据,如果有则代表不是第一次进入
		 */
        SharedPreferences pref = getSharedPreferences("JudgeActivity_IsFirst_In",
                MODE_PRIVATE);

        boolean IS_FIRST = pref.getBoolean("IsFirst", true);

        if (IS_FIRST) {// 判断是否是第一次运行
            /*
			 * 第一次进入后需要把参数更改为false
			 */
            mEditor = getSharedPreferences("JudgeActivity_IsFirst_In", MODE_PRIVATE).edit();// 声明一个存储对象
            mEditor.putBoolean("isFirst", false);
            mEditor.commit();// 提交
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
