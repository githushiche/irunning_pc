package com.example.hsc.irunning.main.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.activity.MapActivity;
import com.example.hsc.irunning.command.broadcasts.LocalReceiver;
import com.example.hsc.irunning.command.broadcasts.NetworkChangeReceiver;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.command.utils.PermissionUtils;
import com.example.hsc.irunning.main.adapter.ScrollViewPagerAdapter;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.dialog.DetailedUploadDialog;
import com.example.hsc.irunning.main.dialog.DynamicAddDialog;
import com.example.hsc.irunning.main.dialog.HealthyDietDialog;
import com.example.hsc.irunning.main.dialog.MessageDialog;
import com.example.hsc.irunning.main.dialog.SelfInformationDialog;
import com.example.hsc.irunning.main.fragment.DynamicFragment;
import com.example.hsc.irunning.main.fragment.FriendFragment;
import com.example.hsc.irunning.main.fragment.MessageFragment;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.example.hsc.irunning.main.service.DishesService;
import com.example.hsc.irunning.main.utils.WSClientUtils;
import com.example.hsc.irunning.main.view.NoScrollViewPagerView;
import com.example.hsc.irunning.stepcount.activity.StepMainActivity;
import com.example.hsc.irunning.stepcount.service.StepService;
import com.example.hsc.irunning.stepcount.utils.StepCountCheckUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Diviner on 2019/4/14.
 * Vesion:1.0
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;// 抽屉式菜单
    private NavigationView mNavigationView;
    private View mNavigationHeadView;

    // 检测网络变化的全局广播
    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;

    // 本地广播根据typeCode来通知对应适配器进行数据更新
    private LocalReceiver mLocalReceiver;
    public static LocalBroadcastManager mLocalBroadcastManager;

    // websocket帮助类
    private WSClientUtils mWsClientUtils;

    // 无滑动实现加载碎片
    public NoScrollViewPagerView mViewpager;// 自定义控件
    private Button mBtnMessage, mBtnFriend, mBtnDynamic, mBtnMap;// 下方控制按钮
    private List<Fragment> mFragmentList;// 滑动显示碎片
    private ScrollViewPagerAdapter mScrollViewPagerAdapter;// 碎片布局适配器
    private Dialog mBottomDialog;

    // 按钮
    public static Bitmap sUserBitmap;
    private FloatingActionButton mFloatingActionButton;
    private ImageView mUserImager;
    private ImageView mMainUserImager;
    private Button mBtnSetting;
    private TextView mTvUserName;
    private TextView mTvIntroduce;
    private RelativeLayout mOffOnToolbar;

    public static User sUser;// 存储用户登录信息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ActivityCollectorUtils.addActivity(this);

        // 开启计步服务
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            setupService();
        } else {
            LogMsgUtil.Log_D(TAG, "没有传感器");
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 禁止弹出软键盘
        sUserBitmap = null;
        sUser = (User) getIntent().getSerializableExtra("user");// 取到传递的信息

        initView();
        sendHttp();
        initBroadcast();

        PermissionUtils.isOpenPermission(this);// 动态申请权限

        mWsClientUtils = new WSClientUtils(this);
        mWsClientUtils.startWebsocket();

    }

    private DishesService.DishesPush mDishesPush;
    private ServiceConnection mDishesConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 服务连接成功后
            mDishesPush = (DishesService.DishesPush) iBinder;
            mDishesPush.startDishesPush();// 执行方法
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 服务关闭之后
        }
    };

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_tb_main_menu);
        setSupportActionBar(toolbar);// 设置标题

        mOffOnToolbar = (RelativeLayout) findViewById(R.id.id_off_on_toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout_main);

        mNavigationView = (NavigationView) findViewById(R.id.id_nv_main_left_view);// 左边滑动布局
        mNavigationHeadView = mNavigationView.getHeaderView(0);


        mNavigationView.setCheckedItem(R.id.id_main_left_selfinfo);// 默认是点击第一个
        mNavigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // 用户头像
        mUserImager = (ImageView) mNavigationHeadView.findViewById(R.id.id_main_user_img);
        mUserImager.setOnClickListener(this);

        mMainUserImager = (ImageView) findViewById(R.id.id_main_click_img);
        mMainUserImager.setOnClickListener(this);

        mTvUserName = (TextView) mNavigationHeadView.findViewById(R.id.id_tv_main_username);
        mTvIntroduce = (TextView) mNavigationHeadView.findViewById(R.id.id_main_user_introduce);

        if (sUser != null) {
            mTvUserName.setText(sUser.getuNickName());
            mTvIntroduce.setText(sUser.getuUserInfo().getuIntroduce());
        }

        // 设置按钮
        mBtnSetting = (Button) findViewById(R.id.id_main_btn_setting);
        mBtnSetting.setOnClickListener(this);

        // 下方的三个按钮
        mBtnMessage = (Button) findViewById(R.id.id_btn_message);
        mBtnMessage.setOnClickListener(this);

        mBtnFriend = (Button) findViewById(R.id.id_btn_friend);
        mBtnFriend.setOnClickListener(this);

        mBtnDynamic = (Button) findViewById(R.id.id_btn_dynamic);
        mBtnDynamic.setOnClickListener(this);

        mBtnMap = (Button) findViewById(R.id.id_btn_map);
        mBtnMap.setOnClickListener(this);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_fab_main_more);
        mFloatingActionButton.setOnClickListener(this);

        mViewpager = (NoScrollViewPagerView) findViewById(R.id.id_scrollview);

        // 添加碎片
        mFragmentList = new ArrayList<Fragment>();
        // 加载下面四个碎片到布局
        mFragmentList.add(new MessageFragment());// 消息列表
        mFragmentList.add(new FriendFragment());// 好友列表
        mFragmentList.add(new DynamicFragment());// 动态

        ScrollViewPagerAdapter scrollViewPagerAdapter = new ScrollViewPagerAdapter(getSupportFragmentManager(), mFragmentList);

        // 加载adapter
        mViewpager.setAdapter(scrollViewPagerAdapter);
        mViewpager.setNoScroll(true);// 不可以滑动

        mViewpager
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                        LogMsgUtil.Log_D(TAG, "当前下标" + position);
                        setdate(position);// 将当前的下标颜色改为红色
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
    }

    private void sendHttp() {
        // 获取网络图片资源
        try {
            HttpUtils.downloadImage(sUser.getuUserPicture().getuPicturePath(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        byte[] Picture_bt = response.body().bytes();
                        sUserBitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                        showImg();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示用户头像
     */
    private void showImg() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mUserImager.setImageBitmap(sUserBitmap);
                mMainUserImager.setImageBitmap(sUserBitmap);
            }
        });
    }

    /**
     * 初始化广播
     */
    private void initBroadcast() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);// 注册广播

        // 注册本地广播
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.example.hsc.irunning.MY_LOCAL_BROADCAST");

        mLocalReceiver = new LocalReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, mIntentFilter);

        Intent dishesService = new Intent(this, DishesService.class);
        bindService(dishesService, mDishesConn, BIND_AUTO_CREATE);
    }

    /*
     * 点击按钮改变字体的颜色
     */
    public void setdate(int index) {
        switch (index) {
            case 0:
                mBtnMessage.setTextColor(Color.RED);
                mBtnMap.setTextColor(Color.BLACK);
                mBtnFriend.setTextColor(Color.BLACK);
                mBtnDynamic.setTextColor(Color.BLACK);
                break;
            case 1:
                mBtnFriend.setTextColor(Color.RED);
                mBtnMap.setTextColor(Color.BLACK);
                mBtnMessage.setTextColor(Color.BLACK);
                mBtnDynamic.setTextColor(Color.BLACK);
                break;
            case 2:
                mBtnDynamic.setTextColor(Color.RED);
                mBtnMap.setTextColor(Color.BLACK);
                mBtnFriend.setTextColor(Color.BLACK);
                mBtnMessage.setTextColor(Color.BLACK);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_message:// 消息
                mViewpager.setCurrentItem(0);
                setdate(0);
                mOffOnToolbar.setVisibility(View.VISIBLE);
                break;

            case R.id.id_btn_friend:// 好友
                mViewpager.setCurrentItem(1);
                setdate(1);
                mOffOnToolbar.setVisibility(View.VISIBLE);
                break;

            case R.id.id_btn_dynamic:// 动态
                mViewpager.setCurrentItem(2);
                setdate(2);
                mOffOnToolbar.setVisibility(View.VISIBLE);
                break;

            case R.id.id_fab_main_more:// 打开地图
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                break;

            case R.id.id_main_click_img:// 界面上的头像,点击滑出菜单
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.id_main_btn_setting:// 设置按钮
                showBottomChoose();
                break;
        }
    }

    /**
     * 显示下方弹出框
     */
    private void showBottomChoose() {
        mBottomDialog = new Dialog(this, R.style.DynamicBottomTheme);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main_setting_bottom, null);

        Button btnUnlogin = (Button) view.findViewById(R.id.id_main_setting_unlogin);
        btnUnlogin.setOnClickListener(mBottomClickListener);

        Button btnCancel = (Button) view.findViewById(R.id.id_main_setting_cancel);
        btnCancel.setOnClickListener(mBottomClickListener);

        mBottomDialog.setContentView(view);

        //获取当前Activity所在的窗体
        Window dialogWindow = mBottomDialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        dialogWindow.setWindowAnimations(R.style.animTranslate);
        // 出现后保持的位置
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mBottomDialog.show();//显示对话框
    }

    // 弹出框点击事件
    private View.OnClickListener mBottomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_main_setting_unlogin:// 注销(有问题)
                    showUnLoginFailedDialog();
                    break;
                case R.id.id_main_setting_cancel:// 取消
                    mBottomDialog.dismiss();
                    break;
            }
        }
    };

    /**
     * 是否退出状态提示框
     */
    private void showUnLoginFailedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog_custom);// 设置样式

        View customView = getLayoutInflater().inflate(R.layout.dialog_main_setting_bottom, null);
        builder.setView(customView);// 加载布局

        final AlertDialog mDialog = builder.create();// 创建窗口的

        TextView titleTv = customView.findViewById(R.id.id_setting_bottom_content);
        TextView confirmBtn = customView.findViewById(R.id.id_setting_bottom_confirmBtn);
        TextView cancelBtn = customView.findViewById(R.id.id_setting_bottom_cancelBtn);

        titleTv.setText("您确定要退出登录吗?");
        confirmBtn.setText("确定");
        cancelBtn.setText("取消");

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mBottomDialog.dismiss();
            }
        });

        //同上
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mBottomDialog.dismiss();
            }
        });
        mDialog.show();
    }

    private void onClose() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        ActivityCollectorUtils.finishAll();
    }

    /**
     * 左边滑动菜单点击事件
     */
    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.id_main_left_selfinfo:// 个人信息
                    mNavigationView.setCheckedItem(R.id.id_main_left_selfinfo);// 设置当前点击项高亮

                    new SelfInformationDialog(MainActivity.this).show();// 打开我的信息界面
                    break;

                case R.id.id_main_my_love:// 我的爱好
                    mNavigationView.setCheckedItem(R.id.id_main_my_love);// 设置当前点击项高亮
                    break;

                case R.id.id_main_left_keephealth:// 运动健康
                    mNavigationView.setCheckedItem(R.id.id_main_left_keephealth);// 设置当前点击项高亮

                    Intent intent = new Intent(MainActivity.this, StepMainActivity.class);
                    startActivity(intent);
                    break;

                case R.id.id_main_left_healthydiet:// 饮食健康
                    mNavigationView.setCheckedItem(R.id.id_main_left_healthydiet);// 设置当前点击项高亮

                    new HealthyDietDialog(MainActivity.this).show();// 跳转到饮食健康界面
                    break;
            }
            return true;
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (sUser != null) {
            mTvUserName.setText(sUser.getuNickName());
            mTvIntroduce.setText(sUser.getuUserInfo().getuIntroduce());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mUserImager.setImageBitmap(sUserBitmap);
        mMainUserImager.setImageBitmap(sUserBitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);// 页面销毁的时候取消注册广播
        unregisterReceiver(mLocalReceiver);

        if (isBind) {
            this.unbindService(conn);
        }
        unbindService(mDishesConn);// 解除服务绑定

        mWsClientUtils.closeConnect();// 程序关闭时关闭websocket
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (100 == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                for (int k = 0; k < permissions.length; k++) {
                    ContextCompat.checkSelfPermission(this, permissions[k]);
                }

                LogMsgUtil.Log_D(TAG, "---------------权限获取失败");
                //                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
                //全部权限通过，可以进行下一步操作。。。
                LogMsgUtil.Log_D(TAG, "---------------权限获取成功");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Message message;
        switch (requestCode) {
            // 用户头像上传
            case 11:
                LogMsgUtil.Log_D(TAG, "MainActivity正在获取照片信息");
                message = new Message();
                message.what = 111;
                message.obj = data;
                DetailedUploadDialog.mHandler.sendMessage(message);// 发送消息给MessageDialog处理照片
                break;
            case 12:
                LogMsgUtil.Log_D(TAG, "MainActivity正在获取手机图库照片信息");
                message = new Message();
                message.what = 121;
                message.obj = data;
                DetailedUploadDialog.mHandler.sendMessage(message);// 发送消息给MessageDialog处理照片
                break;

            case 21:
                LogMsgUtil.Log_D(TAG, "MainActivity正在获取照片信息");
                message = new Message();
                message.what = 211;
                message.obj = data;
                DynamicAddDialog.mHandler.sendMessage(message);// 发送消息给DynamicAddDialog处理照片
                break;
            case 22:
                LogMsgUtil.Log_D(TAG, "MainActivity正在获取手机图库照片信息");
                message = new Message();
                message.what = 222;
                message.obj = data;
                DynamicAddDialog.mHandler.sendMessage(message);// 发送消息给DynamicAddDialog处理照片
                break;

            case 31:
                LogMsgUtil.Log_D(TAG, "MainActivity正在获取照片信息");
                message = new Message();
                message.what = 311;
                message.obj = data;
                MessageDialog.mHandler.sendMessage(message);// 发送消息给MessageDialog处理照片
                break;

            case 32:
                LogMsgUtil.Log_D(TAG, "MainActivity正在获取手机图库照片信息");
                message = new Message();
                message.what = 321;
                message.obj = data;
                MessageDialog.mHandler.sendMessage(message);// 发送消息给MessageDialog处理照片
                break;
        }
    }

    /**
     * 开启计步服务
     */
    public void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
        LogMsgUtil.Log_D(TAG, "开启服务");
    }

    private boolean isBind = false;

    /**
     * 用于查询应用服务（application Service）的状态的一种interface， 更详细的信息可以参考Service 和
     * context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         *
         * @param name
         *            实际所连接到的Service组件名称
         * @param service
         *            服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法， 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         *
         * @param name
         *            丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
