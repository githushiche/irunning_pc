package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.http.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 修改密码
 * Created by Diviner on 2019/5/20.
 * Vesion:1.0
 */
public class ChangePasswordDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "ChangePassword";
    private Activity mActivity;

    private TextView mTvOldPass, mTvNewPass, mTvOkPass;
    private TextView mBtnChange;
    private ImageView mIvSelfBack;

    public ChangePasswordDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不现实标题
        setContentView(R.layout.dialog_self_changepass_layout);
        DialogCollectorUtils.addDialog(this);// 添加到管理器

        initView();
    }

    private void initView() {
        mIvSelfBack = (ImageView) findViewById(R.id.id_iv_self_back);
        mIvSelfBack.setOnClickListener(this);

        mTvOldPass = (TextView) findViewById(R.id.id_changePassword_old_password);
        mTvNewPass = (TextView) findViewById(R.id.id_changePassword_new_password);
        mTvOkPass = (TextView) findViewById(R.id.id_changePassword_ok_password);

        mBtnChange = (TextView) findViewById(R.id.id_btn_changePassword);
        mBtnChange.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_changePassword:// 修改密码
                final User user = MainActivity.sUser;// 拿到原有的账号信息

                String oldPass = mTvOldPass.getText().toString().trim();
                String newPass = mTvNewPass.getText().toString().trim();
                String okPass = mTvOkPass.getText().toString().trim();

                if (user.getuPassWord().equals(oldPass)) {// 旧密码是否正确
                    if (newPass.equals(okPass)) {// 要修改的密码和确认密码是否相同
                        if (!newPass.equals(oldPass)) {// 新密码不能和旧密码相同

                            user.setuPassWord(newPass);

                            HttpUtils.sendUpdateInfoRequest(user, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogMsgUtil.Log_D(TAG, e.getMessage());// 输出异常信息
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.code() == 200) {
                                        MainActivity.sUser = user;
                                        user.saveOrUpdate();// 保存并更新本地用户数据

                                        showMsg();
                                        dismiss();
                                    }
                                }
                            });
                        } else {// 新密码和旧密码相同
                            AppToastUtils.toastShort(mActivity, "新旧密码相同!请重新输入!");
                            mTvNewPass.setText("");
                            mTvOkPass.setText("");
                            mTvOldPass.setText("");
                        }
                    } else {// 两次密码不一致
                        AppToastUtils.toastShort(mActivity, "两次输入的密码不一致!请重新输入!");
                        mTvNewPass.setText("");
                        mTvOkPass.setText("");
                    }
                } else {
                    // 旧密码不正确
                    AppToastUtils.toastShort(mActivity, "旧密码不正确!请重新输入!");
                    mTvOldPass.setText("");
                }
                break;

            case R.id.id_iv_self_back:
                dismiss();
                break;
        }
    }

    private void showMsg() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppToastUtils.toastShort(mActivity, "密码修改成功");
            }
        });
    }
}
