package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.AlertDialogUtils;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.bean.UserInfo;
import com.example.hsc.irunning.main.http.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 个人信息详细界面
 *
 * @author Diviner
 * @date 2018-1-23 下午3:12:30
 */
public abstract class DetailedInfoDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "DetailedInfoDialog";// log
    private Activity mActivity;

    private ImageView mIvUserImg;// 头像
    private TextView mTvAccount;// 跑跑号
    private EditText mTvNickName;// 昵称
    private EditText mTvUserSex;// 性别
    private EditText mTvBirthday;// 生日
    private EditText mTvAddress;// 地址
    private EditText mTvIntroduce;// 简介
    private EditText mEtHeight;// 身高
    private EditText mEtWeight;// 体重
    private EditText mEtPhone;// 号码
    private EditText mEtEmail;// 邮箱
    private ImageView mIvBack;// 返回

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public DetailedInfoDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
        setContentView(R.layout.activity_user_info_layout);
        DialogCollectorUtils.addDialog(this);// 添加到弹框管理器

        init();
    }

    public void init() {
        mIvUserImg = (ImageView) findViewById(R.id.id_iv_userinfo_picture);
        mIvUserImg.setImageBitmap(MainActivity.sUserBitmap);// 设置头像
        mIvUserImg.setOnClickListener(this);

        mTvAccount = (TextView) findViewById(R.id.tv_account);
        mTvNickName = (EditText) findViewById(R.id.et_account_nick);
        mTvUserSex = (EditText) findViewById(R.id.is_user_sex);
        mTvBirthday = (EditText) findViewById(R.id.tv_account_birth);
        mTvAddress = (EditText) findViewById(R.id.tv_account_location);
        mTvIntroduce = (EditText) findViewById(R.id.id_my_content);

        // 身高体重属性,后面算卡路里要用
        mEtHeight = (EditText) findViewById(R.id.id_user_height);
        mEtWeight = (EditText) findViewById(R.id.id_user_weight);
        mEtPhone = (EditText) findViewById(R.id.id_user_phone);
        mEtEmail = (EditText) findViewById(R.id.id_user_email);

        mIvBack = (ImageView) findViewById(R.id.id_iv_info_back);
        mIvBack.setOnClickListener(this);

        // 设置显示信息
        if (MainActivity.sUser != null) {
            String sex = "";
            if (MainActivity.sUser.getuUserInfo().getuSex() == 0) {// 判断性别
                sex = "男";
            } else {
                sex = "女";
            }
            mTvAccount.setText(MainActivity.sUser.getuName());
            mTvNickName.setText(MainActivity.sUser.getuNickName());
            mTvUserSex.setText(sex);
            mTvBirthday.setText(String.valueOf(MainActivity.sUser
                    .getuUserInfo().getuAge()));
            mTvAddress.setText(MainActivity.sUser.getuUserInfo().getuAddress());
            mTvIntroduce.setText(MainActivity.sUser.getuUserInfo()
                    .getuIntroduce());
            mEtHeight.setText(String.valueOf(MainActivity.sUser.getuUserInfo()
                    .getuHeight()));
            mEtWeight.setText(String.valueOf(MainActivity.sUser.getuUserInfo()
                    .getuWeight()));
            mEtPhone.setText(MainActivity.sUser.getuUserInfo().getuPhone());
            mEtEmail.setText(MainActivity.sUser.getuUserInfo().getuEmail());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIvUserImg.setImageBitmap(MainActivity.sUserBitmap);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_userinfo_picture:// 用户点击头像时候
                // 弹出上传图片选项框
                new DetailedUploadDialog(mActivity).show();
                return;
            case R.id.id_iv_info_back:
                // 保存用户更改的信息,先做缓存存入本地数据库
                AlertDialog.Builder builder = AlertDialogUtils.alertDialog(mActivity, "状态提醒", "您确定要保存修改吗？");

                builder.setPositiveButton("保存修改", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 关闭dialog前保存数据并关闭页面
                        save();
                        dismiss();
                    }
                }).setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();// 关闭当前页面
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();// 显示提示框

                // 设置按钮颜色
                Button btnok = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btnok.setTextColor(Color.RED);

                Button btncan = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btncan.setTextColor(Color.RED);
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 保存用户更改的信息,先做缓存存入本地数据库
            AlertDialog.Builder builder = AlertDialogUtils.alertDialog(mActivity, "状态提醒", "您确定要保存修改吗？");

            builder.setPositiveButton("保存修改", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 关闭dialog前保存数据并关闭页面
                    save();
                    dismiss();
                }
            }).setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dismiss();// 关闭当前页面
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();// 显示提示框

            // 设置按钮颜色
            Button btnok = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btnok.setTextColor(Color.RED);

            Button btncan = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            btncan.setTextColor(Color.RED);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 保存用户信息到本地并发起网络更新请求
    private void save() {
        int sex = 0;
        if (mTvUserSex.getText().toString().trim().equals("男")) {
            sex = 0;
        } else {
            sex = 1;
        }
        final User user = MainActivity.sUser;
        user.setuNickName(mTvNickName.getText().toString().trim());// 设置修改后的昵称

        // 设置用户信息
        final UserInfo ui = new UserInfo(MainActivity.sUser.getuUserInfo()
                .getuId(), sex, Integer.parseInt(mTvBirthday.getText()
                .toString()), "", mEtEmail.getText().toString(), mEtPhone
                .getText().toString(), mTvAddress.getText().toString(),
                mTvIntroduce.getText().toString(), Integer.parseInt(mEtHeight
                .getText().toString()), Integer.parseInt(mEtWeight
                .getText().toString()), MainActivity.sUser
                .getuUserInfo().getuFat(), MainActivity.sUser
                .getuUserInfo().getuLongitude(), MainActivity.sUser
                .getuUserInfo().getuLatitude());
        MainActivity.sUser.setuUserInfo(ui);

        LogMsgUtil.Log_D(TAG, "用户信息" + MainActivity.sUser.toString());

        // 保存并更新本地数据
        MainActivity.sUser.saveOrUpdate();
        ui.saveOrUpdate();

        HttpUtils.sendUpdateInfoRequest(MainActivity.sUser, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogMsgUtil.Log_D(TAG, e.getMessage());// 输出异常信息
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    dismiss();
                }
            }
        });
    }
}
