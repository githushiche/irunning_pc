package com.example.hsc.irunning.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.command.utils.PermissionUtils;
import com.example.hsc.irunning.main.bean.HttpMsgSimpleModel;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.bean.Walk;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 登陆界面
 * Created by Diviner
 * Time:2018/12/11.
 * Version:1.0
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "LoginActivity";

    private ImageView mIvUserImg;// 用户头像
    private EditText mEtUserName;// 用户名
    private EditText mEtPassWord;// 密码
    private TextView mTvLogin;// 登录按钮
    private TextView mTvRegister;// 注册按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
        setContentView(R.layout.activity_login_layout);// 不显示标题栏
        ActivityCollectorUtils.addActivity(this);// 添加到活动管理器

        initView();

        Intent intent = getIntent();// 得到数据
        if (intent != null) {
            // 设置从注册界面返回的数据到输入框中
            mEtUserName.setText(intent.getStringExtra("reg_account"));
            mEtPassWord.setText(intent.getStringExtra("reg_password"));
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mIvUserImg = (ImageView) findViewById(R.id.id_user_img);
        mEtUserName = (EditText) findViewById(R.id.id_user_account);
        mEtPassWord = (EditText) findViewById(R.id.id_pass_word);

        mTvLogin = (TextView) findViewById(R.id.id_btn_login);
        mTvLogin.setOnClickListener(this);

        mTvRegister = (TextView) findViewById(R.id.id_btn_register);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.id_btn_login:// 登陆
                if (PermissionUtils.isNetworkAvalible(getApplicationContext())) {// 判断有无网络连接
                    String uname = mEtUserName.getText().toString();
                    String pword = mEtPassWord.getText().toString();

                    if (!uname.equals("")) {
                        if (!pword.equals("")) {// 判断用户名密码之后
                            HttpUtils.sendLoginRequest(uname, pword, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    // 异常处理
                                    LogMsgUtil.Log_D(TAG, "异常" + e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    // 结果处理
                                    Gson gson = new Gson();
                                    if (response.code() == 200) {
                                        String json = response.body().string();


                                        HttpMsgSimpleModel hmsm = gson.fromJson(json, HttpMsgSimpleModel.class);

                                        if (hmsm.getResultCode().equals("200")) {// 登录成功
                                            User user = gson.fromJson(hmsm.getRemarks(), User.class);

                                            saveAndStart(user);
                                        } else {// 登录失败
                                            showInfo(hmsm);
                                        }
                                    }
                                }
                            });
                        } else {
                            AppToastUtils.toastShort(this, "密码不能为空");
                        }
                    } else {
                        AppToastUtils.toastShort(this, "用户名不能为空");
                    }
                } else {
                    PermissionUtils.checkNetwork(this);// 打开设置网络界面
                }
                break;

            case R.id.id_btn_register:// 注册
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                ActivityCollectorUtils.remoActivity(this);// 活动销毁
                break;
        }
    }

    /**
     * 重置控件的值
     */
    private void cleanController() {
        mEtUserName.setText("");
        mEtPassWord.setText("");
    }

    /**
     * 显示信息
     *
     * @param hmsm
     */
    private void showInfo(final HttpMsgSimpleModel hmsm) {
        if (!hmsm.getResultMsg().equals("")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppToastUtils.toastShort(LoginActivity.this, hmsm.getRemarks());
                    cleanController();
                }
            });
        }
    }

    /**
     * 本地持久化处理
     *
     * @param user
     */
    private void saveAndStart(User user) {
        if (user == null) {
            //            AppToastUtils.toastShort(BaseApplication.getsContext(), "用户名或者密码错误");
        } else {
            LogMsgUtil.Log_D(TAG, "登陆字符串:" + user.toString());

            user.saveOrUpdate();// 没有则保存,有数据则保存并更新原有数据到本地数据库
            user.getuUserInfo().saveOrUpdate();// 保存级联数据

            List<Walk> walk = user.getuWalk();// 保存级联数据
            LitePal.saveAll(walk);


            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);// 传递数据
            startActivity(intent);// 启动活动

            ActivityCollectorUtils.remoActivity(this);
        }
    }
}
