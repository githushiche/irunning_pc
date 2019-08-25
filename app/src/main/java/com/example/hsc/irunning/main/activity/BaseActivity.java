package com.example.hsc.irunning.main.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.example.hsc.irunning.R;

/**
 * 所有活动的基类,活动请继承BaseActivity
 * Created by Diviner
 * Time:2018/12/11.
 * Version:1.0
 */
public class BaseActivity extends FragmentActivity {
    private String TAG = "BaseActivity";// 调试名称
    public boolean mIsFragment = false;// 判断是否是碎片

    /**
     * 该方法创建了一个碎片
     *
     * @return
     */
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题框
        if (mIsFragment) {// 判断是否是一个碎片
            setContentView(R.layout.blank_fragment_layout);// 如果是一个碎片则加载它的布局(空白布局)
            FragmentManager fm = getSupportFragmentManager();// 得到碎片管理器
            Fragment fragment = fm.findFragmentById(R.id.id_fragment_layout);// 取到碎片布局的操作权利
            // 碎片管理器查找是否有碎片
            if (fragment == null) {// 判断是否有碎片，没碎片创建一个；
                fragment = createFragment();
                fm.beginTransaction().add(R.id.id_fragment_layout, fragment)
                        .commit();
            }
        }
    }
}
