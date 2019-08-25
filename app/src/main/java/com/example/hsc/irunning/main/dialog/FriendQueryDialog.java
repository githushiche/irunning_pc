package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.adapter.AddFriendAdapter;
import com.example.hsc.irunning.main.bean.HttpMsgSimpleModel;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 查询朋友
 *
 * @author Diviner
 * @date 2018-5-3 下午10:40:16
 */
public class FriendQueryDialog extends Dialog implements OnClickListener,
        OnItemClickListener {
    private Activity mActivity;
    private EditText mEtQueryAccount;
    private ImageView mIvQuery;
    private ImageView mIvBack;
    private TextView mTvDisInfo;
    private ListView mLvQueryListView;

    private static AddFriendAdapter mAddFriendAdapter;
    private static List<User> mFriendList = new ArrayList<User>();

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public FriendQueryDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_friend_query);

        init();
    }

    public void init() {
        mIvBack = (ImageView) findViewById(R.id.id_iv_friend_query_back);
        mIvBack.setOnClickListener(this);

        mIvQuery = (ImageView) findViewById(R.id.id_query_search);
        mIvQuery.setOnClickListener(this);

        mTvDisInfo = (TextView) findViewById(R.id.id_query_disinfo);

        mEtQueryAccount = (EditText) findViewById(R.id.id_query_user_account);

        mLvQueryListView = (ListView) findViewById(R.id.id_query_listview);

        if (mFriendList.size() <= 0) {
            mTvDisInfo.setText("请输入要查询的好友昵称！");
            mTvDisInfo.setVisibility(View.VISIBLE);
        } else {
            mAddFriendAdapter = new AddFriendAdapter(mActivity,
                    R.layout.fragment_friend_item, mFriendList);
            mLvQueryListView.setAdapter(mAddFriendAdapter);// 设置适配器
            mTvDisInfo.setVisibility(View.GONE);
            mLvQueryListView.setVisibility(View.VISIBLE);
            mLvQueryListView.setOnItemClickListener(this);
        }
        mAddFriendAdapter = new AddFriendAdapter(mActivity,
                R.layout.fragment_friend_item, mFriendList);
        mLvQueryListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_query_search:// 查询朋友
                LogMsgUtil.Log_D("MainActivity", "查询好友");
                if (mEtQueryAccount.getText().toString().trim().equals("")) {
                    AppToastUtils.toastShort(mActivity, "查询条件不能为空");
                } else {
                    String nikeName = mEtQueryAccount.getText().toString().trim();

                    HttpUtils.sendQueryFriendRequest(nikeName, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 200) {
                                final String json = response.body().string();
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 子线程更新UI
                                        updateUI(json);
                                    }
                                });
                            }
                        }
                    });
                }
                break;

            case R.id.id_iv_friend_query_back:// 返回
                dismiss();
                break;
        }
    }

    private void updateUI(String json) {
        Gson gson = new Gson();

        HttpMsgSimpleModel hmsm = gson.fromJson(json, HttpMsgSimpleModel.class);
        User user = new Gson().fromJson(json, User.class);

        if (user.getuNickName() != null) {
            // 执行添加后不能调用
            // Adapter.notifyDataSetChanged()更新UI，
            // 因为与UI不是同线程
            mFriendList.add(user);
            mAddFriendAdapter = new AddFriendAdapter(mActivity,
                    R.layout.fragment_friend_item, mFriendList);

            mLvQueryListView.setAdapter(mAddFriendAdapter);
            mTvDisInfo.setVisibility(View.GONE);
            mLvQueryListView.setVisibility(View.VISIBLE);
            mAddFriendAdapter.notifyDataSetChanged();
        } else if (hmsm != null) {
            if (hmsm.getResultCode().equals("201")) {
                // 无搜索好友
                mTvDisInfo.setVisibility(View.VISIBLE);
                mTvDisInfo.setText(hmsm.getResultMsg());
                mLvQueryListView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        User user = mFriendList.get(position);
        new FriendInfoDialog(mActivity, user).show();// 显示用户详情框
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.cancel();
    }
}
