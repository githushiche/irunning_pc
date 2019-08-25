package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.Friend;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.http.HttpUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 发送好友请求
 *
 * @author Diviner
 * @date 2018-5-5 下午7:12:49
 */
public class FriendRequestDialog extends Dialog implements
        View.OnClickListener {
    private final String TAG = "FriendRequestDialog";
    private Activity mActivity;

    private ImageView mIvBack;
    private EditText mEtRequestInfo;// 添加信息
    private TextView mTvRequestSend;// 添加

    private User mFriendInfo;// 查询到的用户信息

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public FriendRequestDialog(Activity activity, User user) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        mFriendInfo = user;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_friend_add_request);

        init();
    }

    public void init() {
        mIvBack = (ImageView) findViewById(R.id.id_iv_friend_request_back);
        mIvBack.setOnClickListener(this);

        mEtRequestInfo = (EditText) findViewById(R.id.id_request_msg);

        mTvRequestSend = (TextView) findViewById(R.id.id_request_send);
        mTvRequestSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_request_send:// 发送请求到服务器
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                Friend friend = new Friend(0, sdf.format(new Date()), mEtRequestInfo.getText()
                        .toString(), MainActivity.sUser.getuId(), mFriendInfo
                        .getuId(), 0);

                HttpUtils.sendAddFriendRequest(friend, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogMsgUtil.Log_D(TAG, "FriendRequestDialog异常-------->" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        DialogCollectorUtils.remoActivity(FriendRequestDialog.this);
                    }
                });
                break;

            case R.id.id_iv_friend_request_back:
                dismiss();
                break;
        }
    }
}
