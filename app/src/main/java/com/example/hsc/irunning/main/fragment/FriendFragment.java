package com.example.hsc.irunning.main.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.adapter.FriendAdapter;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.dialog.FriendDetailsDialog;
import com.example.hsc.irunning.main.dialog.FriendNotifiyDialog;
import com.example.hsc.irunning.main.dialog.FriendQueryDialog;
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
 * 朋友碎片
 *
 * @author Diviner
 * @date 2018-5-1 下午9:34:46
 */
public class FriendFragment extends Fragment implements OnClickListener,
        OnItemClickListener {
    private final String TAG = "FriendFragment";

    private static Activity mActivity;
    private View mView;
    private static LinearLayout mProgressLayout;
    private RelativeLayout mRlAddFriend;// 添加朋友
    private RelativeLayout mRlAddFriendVerification;// 好友验证
    private RelativeLayout mRlGroupFriend;// 群聊

    public static ListView mFriendListView;// 好友列表
    public static List<User> mFriends;
    public static FriendAdapter mFriendAdapter;// 好友适配器

    private static SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_friend_layout, container, false);

        mActivity = getActivity();

        initView();
        queryFriendList();

        return mView;
    }

    /**
     * 初始化
     */
    private void initView() {
        mFriends = new ArrayList<>();

        mProgressLayout = (LinearLayout) mView.findViewById(R.id.id_pb_friend_progress);

        mRlAddFriend = (RelativeLayout) mView
                .findViewById(R.id.layout_add_friend);
        mRlAddFriend.setOnClickListener(this);

        mRlAddFriendVerification = (RelativeLayout) mView
                .findViewById(R.id.layout_msg_notify);
        mRlAddFriendVerification.setOnClickListener(this);

        mFriendListView = (ListView) mView.findViewById(R.id.id_friend_list);
        mFriendListView.setOnItemClickListener(this);

        mFriendAdapter = new FriendAdapter(getActivity(),
                R.layout.fragment_friend_item, mFriends);
        mFriendListView.setAdapter(mFriendAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_srl_fragment_fragment);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryFriendList();
            }
        });
    }

    /**
     * 查询好友列表
     */
    public static void queryFriendList() {
        HttpUtils.sendFriendListRequest(MainActivity.sUser.getuId(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogMsgUtil.Log_D("FriendFragment", "----->" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        Gson gson = new Gson();
                        String json = response.body().string();
                        final List<User> friends = gson.fromJson(json,
                                new TypeToken<List<User>>() {
                                }.getType());
                        LogMsgUtil.Log_D("FriendFragment", "好友的数据------->" + friends.toString());
                        setFriendData(friends);
                    } catch (Exception e) {
                        LogMsgUtil.Log_D("FriendFragment", "FriendFragment------->" + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 更新好友数据
     *
     * @param user
     */
    public static void setFriendData(final List<User> user) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFriends = user;
                mFriendAdapter = new FriendAdapter(mActivity,
                        R.layout.fragment_friend_item, user);
                mFriendListView.setAdapter(mFriendAdapter);
                mFriendAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);

                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mProgressLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_add_friend:// 添加朋友
                new FriendQueryDialog(getActivity()).show();
                break;

            case R.id.layout_msg_notify:// 好友验证查看
                new FriendNotifiyDialog(getActivity()).show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // 子项点击事件
        User user = mFriends.get(position);
        new FriendDetailsDialog(getActivity(), user).show();
    }
}
