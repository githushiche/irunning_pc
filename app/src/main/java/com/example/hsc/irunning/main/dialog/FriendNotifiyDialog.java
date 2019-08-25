package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.BaseApplication;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.adapter.FriendNotifiyAdapter;
import com.example.hsc.irunning.main.bean.Friend;
import com.example.hsc.irunning.main.fragment.FriendFragment;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 好友验证
 *
 * @author Diviner
 * @date 2018-5-2 下午9:41:53
 */
public class FriendNotifiyDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "FriendNotifiyDialog";

    private Activity mActivity;
    private ListView mNotifiyListView;
    private static List<Friend> mFriendList = new ArrayList<Friend>();// 用于存放好友list
    private static FriendNotifiyAdapter mFriendNotifiyAdapter;// 适配器
    private ImageView mIvBack;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public FriendNotifiyDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_friend_add_notifiy);

        init();
        queryFriendNotifiy();
    }

    public void init() {
        mNotifiyListView = (ListView) findViewById(R.id.id_friend_notifiy_listview);

        mFriendNotifiyAdapter = new FriendNotifiyAdapter(mActivity,
                R.layout.fragment_friend_add_notifiy_item, mFriendList);
        mNotifiyListView.setAdapter(mFriendNotifiyAdapter);

        mIvBack = (ImageView) findViewById(R.id.id_iv_friend_add_back);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_friend_add_back:
                dismiss();
                break;
        }
    }

    private void queryFriendNotifiy() {
        HttpUtils.sendGetFriendRequest(MainActivity.sUser.getuId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogMsgUtil.Log_D(TAG, "FriendNotifiyDialog异常-------->" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();
                    setData(json);
                }
            }
        });
    }

    private void setData(String json) {
        Gson gson = new Gson();
        mFriendList = gson.fromJson(json, new TypeToken<List<Friend>>() {
        }.getType());

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFriendNotifiyAdapter = new FriendNotifiyAdapter(mActivity,
                        R.layout.fragment_friend_add_notifiy_item, mFriendList);
                mNotifiyListView.setAdapter(mFriendNotifiyAdapter);
                mFriendNotifiyAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 异步消息处理
     */
    public static Handler mHandler = new Handler() {
        Friend friend = new Friend();

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    friend = (Friend) msg.obj;
                    mFriendList.remove(friend);
                    mFriendNotifiyAdapter.notifyDataSetChanged();
                    showInfo(1);
                    break;

                case 0:
                    showInfo(0);
                    friend = (Friend) msg.obj;
                    mFriendList.remove(friend);
                    mFriendNotifiyAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private static void showInfo(int type) {
        if (type == 1) {
            AppToastUtils.toastShort(BaseApplication.getsContext(), "已经同意好友请求");
            FriendFragment.queryFriendList();
        } else {
            AppToastUtils.toastShort(BaseApplication.getsContext(), "已经拒绝好友请求");
            FriendFragment.queryFriendList();
        }
    }
}
