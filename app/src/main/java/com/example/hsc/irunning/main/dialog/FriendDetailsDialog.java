package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 朋友详情界面
 *
 * @author Diviner
 * @date 2018-5-3 下午10:40:16
 */
public class FriendDetailsDialog extends Dialog implements OnClickListener {
    private final String TAG = "FriendDetailsDialog";
    private Activity mActivity;

    // 标题栏
    private ImageView mIvBack;
    private Button mBtnMore;

    // 内容
    private ImageView mIvUserPicture;
    private TextView mTvUserName;
    private TextView mTvInfo;
    private TextView mTvUserAccount;
    private TextView mTvSexInfo;
    private TextView mTvDynamicInfo;
    private TextView mTvSelfDynamicInfo;
    private Button mBtnSendMsg;

    // 好友信息,头像
    private User mFriendInfo;
    public static Bitmap sFriendBitmap;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public FriendDetailsDialog(Activity activity, User user) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        mFriendInfo = user;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_friend_details);

        init();
    }

    public void init() {
        mIvBack = (ImageView) findViewById(R.id.id_iv_friend_details_back);
        mIvBack.setOnClickListener(this);

        mBtnMore = (Button) findViewById(R.id.id_btn_friend_details_more);
        mBtnMore.setOnClickListener(this);

        mIvUserPicture = (ImageView) findViewById(R.id.id_iv_friend_details_img);
        mTvUserName = (TextView) findViewById(R.id.id_fri_det_username);
        mTvInfo = (TextView) findViewById(R.id.id_fri_dat_info);
        mTvUserAccount = (TextView) findViewById(R.id.id_fri_det_account);
        mTvSexInfo = (TextView) findViewById(R.id.id_fri_det_sexinfo);
        mTvDynamicInfo = (TextView) findViewById(R.id.id_tv_friend_details_content);

        mTvSelfDynamicInfo = (TextView) findViewById(R.id.id_tv_friend_details_selfDynamicInfo);
        mTvSelfDynamicInfo.setOnClickListener(this);

        mBtnSendMsg = (Button) findViewById(R.id.id_fri_det_sendmsg);
        mBtnSendMsg.setOnClickListener(this);

        if (mFriendInfo != null) {
            sendHttp();

            mTvUserName.setText(mFriendInfo.getuName());
            mTvUserAccount.setText(mFriendInfo.getuNickName());
            if (mFriendInfo.getuUserInfo().getuSex() == 0) {
                mTvSexInfo.setText("男");
            } else {
                mTvSexInfo.setText("女");
            }
            mTvInfo.setText(mFriendInfo.getuUserInfo().getuIntroduce());
            if (mFriendInfo.getuDynamics().size() > 0) {
                mTvDynamicInfo.setText("最新动态信息:\n\n" + mFriendInfo.getuDynamics().get(0).getdContent());
            } else {
                mTvDynamicInfo.setText("您的好友暂时没有最新动态");
            }
        }
    }

    private void sendHttp() {
        // 获取网络图片资源
        try {
            HttpUtils.downloadImage(mFriendInfo.getuUserPicture().getuPicturePath(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogMsgUtil.Log_D(TAG, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        byte[] Picture_bt = response.body().bytes();
                        sFriendBitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                        showImg();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示好友头像
     */
    private void showImg() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIvUserPicture.setImageBitmap(sFriendBitmap);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_fri_det_sendmsg:// 发送消息
                new MessageDialog(mActivity, mFriendInfo).show();// 跳转到发消息界面
                this.cancel();
                break;
            case R.id.id_tv_friend_details_selfDynamicInfo:// 好友的动态
                // 跳转到动态界面
                new FriendDetailsDynamicDialog(mActivity, mFriendInfo).show();
                break;
            case R.id.id_iv_friend_details_back:
                cancel();
                break;
            case R.id.id_btn_friend_details_more:// 更多按钮
                break;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
