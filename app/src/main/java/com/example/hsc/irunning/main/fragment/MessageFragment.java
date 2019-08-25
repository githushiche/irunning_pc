package com.example.hsc.irunning.main.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.adapter.FriendMsgAdapter;
import com.example.hsc.irunning.main.bean.FriendMessage;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.dialog.MessageDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * 消息列表碎片
 *
 * @author Diviner
 * @date 2018-5-1 下午8:39:00
 */
public class MessageFragment extends Fragment implements OnItemClickListener {
    private final String TAG = "MessageFragment";
    public static Activity mActivity;
    private View mView;

    public static ListView mMessageListView;
    public static List<User> sMessages;// 用户信息列表
    public static List<FriendMessage> sFriendMsg;// 消息列表
    public static FriendMsgAdapter sFriendMsgAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater
                .inflate(R.layout.fragment_message_list_layout, container, false);
        mActivity = getActivity();

        initView();

        return mView;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mMessageListView = (ListView) mView
                .findViewById(R.id.id_message_listview);

        // 实例化存储对象
        sMessages = new ArrayList<>();
        sFriendMsg = new ArrayList<>();

        sFriendMsgAdapter = new FriendMsgAdapter(mActivity, sMessages);
        mMessageListView.setAdapter(sFriendMsgAdapter);
        mMessageListView.setOnItemClickListener(this);// 设置子项可以点击

        // 应该有一个查出历史聊天记录功能实现

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_srl_fragment_message);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }

    /**
     * 显示消息到消息列表
     *
     * @param typeCode
     * @param messageJson
     */
    public static void showMessage(String typeCode, String messageJson) {
        Gson gson = new Gson();

        List<User> messages = gson.fromJson(messageJson,
                new TypeToken<List<User>>() {
                }.getType());
        sMessages = messages;

        LogMsgUtil.Log_D("MessageFragment", "开始组装数据");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sFriendMsgAdapter = new FriendMsgAdapter(mActivity, sMessages);
                mMessageListView.setAdapter(sFriendMsgAdapter);// 设置适配器
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                            long arg3) {
        // 子项点击事件
        User user = sMessages.get(position);
        new MessageDialog(getActivity(), user).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
