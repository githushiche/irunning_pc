package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.main.activity.MainActivity;

/**
 * 个人信息界面
 *
 * @author Diviner
 * @date 2018-5-2 下午9:18:30
 */
public class SelfInformationDialog extends Dialog implements
        View.OnClickListener {
    private String TAG = "SelfInformationDialog";

    private Activity mActivity;

    private RelativeLayout mRlMeInfo;
    private NavigationView mNavigationView;// 菜单
    private View mNavigationHeadView;
    private ImageView mIvUserImg;// 用户头像
    private TextView mTvUserName;// 用户名
    private TextView mTvAccount;// 昵称
    private ImageView mIvBack;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public SelfInformationDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不现实标题
        setContentView(R.layout.activity_user_self_layout);
        DialogCollectorUtils.addDialog(this);// 添加到管理器

        init();
    }

    public void init() {
        mRlMeInfo = (RelativeLayout) findViewById(R.id.id_me_rl);
        mRlMeInfo.setOnClickListener(this);

        mNavigationView = (NavigationView) findViewById(R.id.id_nv_self_info_view);// 左边滑动布局
        mNavigationHeadView = mNavigationView.getHeaderView(0);

        mNavigationView.setCheckedItem(R.id.id_self_info_updatePass);// 默认是点击第一个
        mNavigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mIvUserImg = (ImageView) findViewById(R.id.iv_head_picture);// 头像
        mIvUserImg.setImageBitmap(MainActivity.sUserBitmap);

        mTvUserName = (TextView) findViewById(R.id.id_me_username);// 昵称
        mTvAccount = (TextView) findViewById(R.id.id_me_account);// 账号

        // 设置显示信息
        if (MainActivity.sUser != null) {
            // mIvUserImg.setImageBitmap(bm);
            mTvUserName.setText(MainActivity.sUser.getuNickName().toString());// 昵称
            mTvAccount.setText(MainActivity.sUser.getuName().toString());// 账号
        }

        mIvBack = (ImageView) findViewById(R.id.id_btn_user_info_back);
        mIvBack.setOnClickListener(this);
    }

    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.id_self_info_updatePass:// 修改密码
                    mNavigationView.setCheckedItem(R.id.id_self_info_updatePass);// 设置当前点击项高亮

                    new ChangePasswordDialog(mActivity).show();
                    break;

                case R.id.id_self_info_setting:// 设置
                    mNavigationView.setCheckedItem(R.id.id_self_info_setting);// 设置当前点击项高亮
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mIvUserImg.setImageBitmap(MainActivity.sUserBitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_me_rl:
                new DetailedInfoDialog(mActivity) {
                }.show();
                break;
            case R.id.id_btn_user_info_back:
                DialogCollectorUtils.remoActivity(this);
                break;
        }
    }
}
