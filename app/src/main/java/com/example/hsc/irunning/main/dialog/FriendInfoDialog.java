package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.http.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 弹出添加好友的详情
 *
 * @author Diviner
 * @date 2018-5-4 上午9:21:50
 */
public class FriendInfoDialog extends Dialog implements
        View.OnClickListener {
    private final String TAG = "FriendInfoDialog";
    private Activity mActivity;

    // 好友信息
    private ImageView mIvBack;// 返回
    private ImageView mIvInfoPicture;// 用户头像
    private TextView mTvInfoAccount;// 用户名称
    private TextView mTvInfoNike;// 用户昵称
    private TextView mTvInfoSetRemark;// 备注
    private TextView mTvInfoAddFriend;// 添加到通讯录

    private User mFriendInfo;
    private byte[] Picture_bt;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public FriendInfoDialog(Activity activity, User user) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        mFriendInfo = user;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_friend_add);

        Picture_bt = null;
        init();
    }

    /**
     * 初始化控件
     */
    public void init() {
        mIvBack = (ImageView) findViewById(R.id.id_iv_friend_add_back);
        mIvBack.setOnClickListener(this);

        mIvInfoPicture = (ImageView) findViewById(R.id.id_info_picture);
        setPic(mFriendInfo);

        mTvInfoAccount = (TextView) findViewById(R.id.id_info_account);
        mTvInfoAccount.setText(mFriendInfo.getuName());

        mTvInfoNike = (TextView) findViewById(R.id.id_info_nike);
        mTvInfoNike.setText(mFriendInfo.getuNickName());

        mTvInfoSetRemark = (TextView) findViewById(R.id.id_info_set_remark);
        mTvInfoSetRemark.setOnClickListener(this);

        mTvInfoAddFriend = (TextView) findViewById(R.id.id_info_add_contract);
        mTvInfoAddFriend.setOnClickListener(this);
    }

    private void setPic(User user) {
        try {
            HttpUtils.downloadImage(user.getuUserPicture().getuPicturePath(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogMsgUtil.Log_D(TAG, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        Picture_bt = response.body().bytes();

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(mActivity)
                                        .load(Picture_bt)
                                        .dontAnimate()
                                        .override(80, 80)
                                        .into(mIvInfoPicture);
                            }
                        });
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_info_set_remark:// 设置备注

                break;

            case R.id.id_info_add_contract:// 添加到通讯录
                new FriendRequestDialog(mActivity, mFriendInfo).show();// 弹出发好友请求界面
                break;

            case R.id.id_iv_friend_add_back:
                dismiss();
                break;
        }
    }
}
