package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.activity.MapActivity;
import com.example.hsc.irunning.baidumap.fragment.MapFragment;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.command.utils.PictureUtils;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.adapter.MessageAdapter;
import com.example.hsc.irunning.main.bean.FriendMessage;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.example.hsc.irunning.main.utils.WSClientUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 好友聊天界面
 *
 * @author Diviner
 * @date 2018-5-5 下午8:29:44
 */
public class MessageDialog extends Dialog implements
        View.OnClickListener {
    private final String TAG = "MessageDialog";

    private static Activity mActivity;
    private LinearLayout mLlInfoTop;
    private LinearLayout mLlInfoBottom;
    public static ListView mMesInfoListView;
    public static MessageAdapter mMessageAdapter;
    //    public static ArrayList<FriendMessage> mFriendMsgs = new ArrayList<>();
    public static List<User> mFriendsMessages;
    public static User mFriendMessage;// 传过来的好友的信息

    private ImageView mIvMesInfo;
    private ImageView mIvMesLocation;
    private TextView mTvTitle;
    private TextView mTvSendMsg;
    private EditText mEtContent;

    public static int sStates = 0;
    private List<FriendMessage> mFriendMessages;

    // 照片相关
    private static PictureUtils mPictureUtils;
    private static String sSendImgUri = "";

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public MessageDialog(Activity activity, User user) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        mPictureUtils = new PictureUtils(activity);
        mPictureUtils.setTAKE_PHOTH(31);
        mPictureUtils.setCHOOSE_PHOTH(32);


        WSClientUtils.isOpenMessage = true;

        mFriendMessage = user;

        mFriendMessages = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_message_layout);

        init();
    }

    /**
     * 初始化控件
     */
    public void init() {
        mFriendsMessages = new ArrayList<>();

        // 聊天界面标题
        mTvTitle = (TextView) findViewById(R.id.id_friend_title);
        mTvTitle.setText("与" + mFriendMessage.getuNickName() + "的聊天");

        mEtContent = (EditText) findViewById(R.id.et_chat_message);

        mTvSendMsg = (TextView) findViewById(R.id.id_mes_info_send);
        mTvSendMsg.setOnClickListener(this);

        mIvMesInfo = (ImageView) findViewById(R.id.id_mes_info_more);
        mIvMesInfo.setOnClickListener(this);

        mIvMesLocation = (ImageView) findViewById(R.id.iv_input_type);
        mIvMesLocation.setOnClickListener(this);

        mLlInfoTop = (LinearLayout) findViewById(R.id.id_mes_info_top);
        mLlInfoBottom = (LinearLayout) findViewById(R.id.id_mes_info_bottom);

        mMesInfoListView = (ListView) findViewById(R.id.id_mes_info_listview);
        mMessageAdapter = new MessageAdapter(mActivity, mFriendsMessages);
        mMesInfoListView.setAdapter(mMessageAdapter);
    }

    /**
     * 历史消息查询
     */
    private void historicalNews() {

    }

    public static void showData() {
        mMessageAdapter = new MessageAdapter(mActivity, mFriendsMessages);
        mMesInfoListView.setAdapter(mMessageAdapter);
        mMessageAdapter.notifyDataSetChanged();
    }

    public static void addData(String messageJson) {
        Gson gson = new Gson();
        List<User> messages = gson.fromJson(messageJson,
                new TypeToken<List<User>>() {
                }.getType());
        if (messages.size() > 0) {
            for (User user : messages) {
                mFriendsMessages.add(user);
            }
            mMessageAdapter = new MessageAdapter(mActivity, mFriendsMessages);
            mMesInfoListView.setAdapter(mMessageAdapter);
            mMessageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_mes_info_send:// 发消息
                User test = new User(MainActivity.sUser.getuId(), MainActivity.sUser.getuName(), MainActivity.sUser.getuNickName(),
                        MainActivity.sUser.getuPassWord(), MainActivity.sUser.getuCreateTime(), MainActivity.sUser.getuState());

                FriendMessage mfm = new FriendMessage(0,
                        test.getuId(), mFriendMessage.getuId(), 0, 0, 0, 0,
                        "19:34", mEtContent.getText().toString().trim());// 最后两参数,第一个判断是接受还是发送,第二个消息类型

                List<FriendMessage> fmessage = new ArrayList<>();
                fmessage.add(mfm);
                test.setuFriendMessages(fmessage);

                WSClientUtils.sendMsg(new Gson().toJson(test));

                mFriendsMessages.add(test);

                showData();
                mEtContent.setText("");
                break;

            case R.id.id_mes_info_more:// 更多按钮
                showBottomChoose();// 显示dialog
                break;

            case R.id.iv_input_type:// 表情

                break;

            case R.id.id_message_takePhoto:// 照相
                mPictureUtils.openSystemCamera();
                mBottomDialog.dismiss();// 关闭
                break;
            case R.id.id_message_choosePhoto:// 从手机选图
                mPictureUtils.choosePhonePhotos();
                mBottomDialog.dismiss();// 关闭
                break;
            case R.id.id_message_location:// 位置
                // 进行位置分享
                if (MapActivity.mLastLocationData != null) {
                    LatLng ll = MapFragment.mLastLocationData;// 获取我的位置

                    User locationMessage = new User(MainActivity.sUser.getuId(), MainActivity.sUser.getuName(), MainActivity.sUser.getuNickName(),
                            MainActivity.sUser.getuPassWord(), MainActivity.sUser.getuCreateTime(), MainActivity.sUser.getuState());

                    FriendMessage msg = new FriendMessage(0,
                            locationMessage.getuId(), mFriendMessage.getuId(), 0, 0, 2,
                            0, "12:43", MainActivity.sUser.getuUserInfo().getuLatitude()
                            + ":" + MainActivity.sUser.getuUserInfo().getuLongitude());// 最后两参数,第一个判断是接受还是发送,第二个消息类型

                    List<FriendMessage> msgs = new ArrayList<>();
                    msgs.add(msg);
                    locationMessage.setuFriendMessages(msgs);


                    WSClientUtils.sendMsg(new Gson().toJson(locationMessage));
                    mBottomDialog.dismiss();
                    mFriendsMessages.add(locationMessage);

                    showData();
                } else {
                    AppToastUtils.toastShort(getContext(), "没有位置信息!请先打开地图!");
                }
                break;
            case R.id.id_message_cancel:
                mBottomDialog.dismiss();// 关闭
                break;
        }
    }

    private Dialog mBottomDialog;

    /**
     * 显示下方弹出框
     */
    private void showBottomChoose() {
        mBottomDialog = new Dialog(getContext(), R.style.DynamicBottomTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_message_bottom_dialog, null);

        Button btnTakePhoto = (Button) view.findViewById(R.id.id_message_takePhoto);
        btnTakePhoto.setOnClickListener(this);

        Button btnChoosePhoto = (Button) view.findViewById(R.id.id_message_choosePhoto);
        btnChoosePhoto.setOnClickListener(this);

        Button btnLocation = (Button) view.findViewById(R.id.id_message_location);
        btnLocation.setOnClickListener(this);

        Button btnCancel = (Button) view.findViewById(R.id.id_message_cancel);
        btnCancel.setOnClickListener(this);

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


    // 异步消息处理
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 311:// 处理照片显示逻辑
                    LogMsgUtil.Log_D("MessageDialog", "地址" + PictureUtils.takeUri);
                    sendPhotoMessage(PictureUtils.takeUri);
                    break;

                case 321:// 处理相册所选照片问题
                    Intent data = (Intent) msg.obj;
                    String imagerPath = "";
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4以上系统
                        imagerPath = mPictureUtils.handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统
                        imagerPath = mPictureUtils.handleImageBeforeKitKat(data);
                    }
                    sSendImgUri = imagerPath;
                    sendPhotoMessage(imagerPath);
                    break;
                case 9:
                    WSClientUtils.sendMsg(new Gson().toJson(mPhotoUser));
                    mFriendsMessages.add(mPhotoUser);
                    showData();
                    break;
            }
        }
    };

    private static User mPhotoUser = new User();

    private static void sendPhotoMessage(String imguri) {
        LogMsgUtil.Log_D("MessageDialog", "已经收到显示照片消息");
        mPhotoUser = new User(MainActivity.sUser.getuId(), MainActivity.sUser.getuName(), MainActivity.sUser.getuNickName(),
                MainActivity.sUser.getuPassWord(), MainActivity.sUser.getuCreateTime(), MainActivity.sUser.getuState());

        FriendMessage photoMessage = new FriendMessage(0,
                mPhotoUser.getuId(), mFriendMessage.getuId(), 0, 0, 1,
                0, "12:43", imguri);// 最后两参数,第一个判断是接受还是发送,第二个消息类型

        List<FriendMessage> messages = new ArrayList<>();
        messages.add(photoMessage);
        mPhotoUser.setuFriendMessages(messages);

        HttpUtils.uploadMessageImager(String.valueOf(MainActivity.sUser.getuId()), String.valueOf(mFriendMessage.getuId()),
                imguri, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            LogMsgUtil.Log_D("MessageDialog", "用户发送的图片已经上传成功!等待回调!");
                            Message message = new Message();
                            message.what = 9;
                            mHandler.sendMessage(message);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        WSClientUtils.isOpenMessage = true;
        sStates = 1;
    }

    @Override
    protected void onStop() {
        super.onStop();
        WSClientUtils.isOpenMessage = false;
    }
}
