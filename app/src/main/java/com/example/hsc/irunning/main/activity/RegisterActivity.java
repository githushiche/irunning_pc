package com.example.hsc.irunning.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.PermissionUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.HttpMsgSimpleModel;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.example.hsc.irunning.main.utils.VerificationUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 注册界面
 * Created by Diviner
 * Time:2018/12/11.
 * Version:1.0
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "RegisterActivity";

    private ImageView mIvBack;
    private EditText mEtAccount;
    private EditText mEtUserName;
    private EditText mEtPassWord;
    private EditText mEtOkPassWord;
    private TextView mTvToLogin;
    private TextView mTvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        setContentView(R.layout.activity_register_layout);
        ActivityCollectorUtils.addActivity(this);// 添加活动到活动管理类

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.id_back_btn);
        mIvBack.setOnClickListener(this);

        mEtAccount = (EditText) findViewById(R.id.id_reg_account);
        mEtUserName = (EditText) findViewById(R.id.id_reg_user_name);
        mEtPassWord = (EditText) findViewById(R.id.id_reg_pass_word);
        mEtOkPassWord = (EditText) findViewById(R.id.id_reg_confirm_pass);

        mTvToLogin = (TextView) findViewById(R.id.id_reg_btn_register);
        mTvToLogin.setOnClickListener(this);

        mTvRegister = (TextView) findViewById(R.id.id_reg_btn_to_login);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.id_reg_btn_register:// 完成注册
                // 获取用户信息
                final String acc = mEtAccount.getText().toString().trim();
                String nickName = mEtUserName.getText().toString().trim();
                final String passWord = mEtPassWord.getText().toString().trim();
                String okPass = mEtOkPassWord.getText().toString().trim();

                if (!passWord.equals(okPass)) {// 密码不相同则不执行判断下面语句
                    AppToastUtils.toastShort(this, "两次输入密码不一致,请重新输入");
                    mEtPassWord.setText("");
                    mEtOkPassWord.setText("");
                    return;
                }

                // 验证用户信息并发起网络请求
                if (VerificationUtils.userNameRegularExpression(acc)) {
                    if (VerificationUtils.passWordRegularExpression(passWord)) {
                        if (PermissionUtils.isNetworkAvalible(this)) {
                            HttpUtils.sendRegisterRequest(acc, passWord, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogMsgUtil.Log_E(TAG, e.getMessage());// 错误日志
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Gson gson = new Gson();
                                    if (response.code() == 200) {
                                        HttpMsgSimpleModel hmsm = gson.fromJson(response.body().string(), HttpMsgSimpleModel.class);
                                        if (hmsm.getResultCode().equals("200")) {
                                            saveAndStart(acc, passWord);
                                        } else if (hmsm.getResultCode().equals("201")) {
                                            showInfo(hmsm.getResultMsg());
                                        } else if (hmsm.getResultCode().equals("202")) {
                                            showInfo(hmsm.getResultMsg());

                                            // 该账号已经被注册的时候,清空输入框给用户重新输入
                                            mEtAccount.setText("");
                                            mEtUserName.setText("");
                                            mEtPassWord.setText("");
                                            mEtOkPassWord.setText("");
                                        }
                                    }
                                }
                            });
                        } else {
                            PermissionUtils.checkNetwork(this);// 没有网络的话
                        }
                    } else {
                        // 密码不合法
                        AppToastUtils.toastShort(this, "密码不合法,密码由5位字母和数字组成");
                    }
                } else {
                    // 用户名不合法
                    AppToastUtils.toastShort(this, "用户名不合法,用户名由4位字母和数字组成");
                }
                break;
            case R.id.id_reg_btn_to_login:// 直接登陆
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                ActivityCollectorUtils.remoActivity(this);
                break;

            case R.id.id_back_btn:// 返回按钮
                // 应该加入换场特效
                intent = new Intent(this, LoginActivity.class);

                startActivity(intent);
                ActivityCollectorUtils.remoActivity(this);
                break;
        }
    }

    /**
     * 保存用户信息并传递到下一页面
     */
    private void saveAndStart(String userName, String passWord) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("reg_account", userName);
        intent.putExtra("reg_password", passWord);

        startActivity(intent);
        ActivityCollectorUtils.remoActivity(this);
    }

    /**
     * 提示信息
     *
     * @param msg
     */
    private void showInfo(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppToastUtils.toastShort(RegisterActivity.this, msg);
            }
        });
    }

}
