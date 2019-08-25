package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.main.adapter.DetailsDynamicAdapter;
import com.example.hsc.irunning.main.bean.User;

/**
 * 好友详情动态界面
 * Created by Diviner on 2019/5/18.
 * Vesion:1.0
 */
public class FriendDetailsDynamicDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "FriendDetailsDynamicDialog";
    private Activity mActivity;

    private static RecyclerView mDynamicRecyclerView;
    private static DetailsDynamicAdapter mDynamicAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;// 刷新控件

    private LinearLayout mNoDynamicLayout;

    // 标题相关
    private ImageView mIvLeftImg;
    private TextView mTvTitle;

    private User mFriendInfo;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public FriendDetailsDynamicDialog(Activity activity, User user) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        mFriendInfo = user;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_friend_details_dynamic_layout);// 加载朋友详情动态布局

        initView();
    }

    /**
     * 初始化控件
     */
    public void initView() {
        // 返回按钮
        mIvLeftImg = (ImageView) findViewById(R.id.id_iv_friend_detailsDynamic_back);
        mIvLeftImg.setOnClickListener(this);

        mTvTitle = (TextView) findViewById(R.id.id_iv_friend_detailsDynamic_title);
        mTvTitle.setText("好友 " + mFriendInfo.getuNickName() + " 的动态");

        mNoDynamicLayout = (LinearLayout) findViewById(R.id.id_ll_fDetails_noDynamic);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_srl_friend_datails);

        mDynamicRecyclerView = (RecyclerView) findViewById(R.id.id_friend_detials_dynamicRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mDynamicRecyclerView.setLayoutManager(layoutManager);

        if (mFriendInfo.getuDynamics().size() == 0) {
            mNoDynamicLayout.setVisibility(View.VISIBLE);// 显示没有动态信息布局
            mSwipeRefreshLayout.setVisibility(View.GONE);// 隐藏动态布局
        } else {
            mDynamicAdapter = new DetailsDynamicAdapter(mFriendInfo.getuDynamics());
            mDynamicRecyclerView.setAdapter(mDynamicAdapter);// 设置适配器

            mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);// 刷新图标颜色
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    setData();
                }
            });
        }
    }

    /**
     * 初始化动态数据
     */
    private void setData() {
        mDynamicAdapter = new DetailsDynamicAdapter(mFriendInfo.getuDynamics());
        mDynamicRecyclerView.setAdapter(mDynamicAdapter);// 设置适配器
        mDynamicAdapter.notifyDataSetChanged();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_menu_btn:
                new DynamicAddDialog(mActivity).show();
                break;
            case R.id.id_iv_friend_detailsDynamic_back:
                cancel();
                break;
        }
    }
}
