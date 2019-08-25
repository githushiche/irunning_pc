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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.command.utils.PictureUtils;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.Dynamic;
import com.example.hsc.irunning.main.bean.HttpMsgSimpleModel;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 发布动态的dialog
 *
 * @author Diviner
 * @date 2018-5-18 下午9:29:23
 */
public class DynamicAddDialog extends Dialog implements
        View.OnClickListener {
    private final String TAG = "DynamicAddDialog";
    private static Activity mActivity;
    private TextView mTvAddBack;// 取消
    private TextView mTvAddSend;// 发布
    private RelativeLayout mRlDynamicImg;// 配图
    private EditText mEtContent;// 内容
    private static ImageView mIvDynamicImg;

    // 照片相关
    private static PictureUtils mPictureUtils;
    private static String sUploadUri = "";

    private Dialog mBottomDialog;

    /**
     * 构造方法
     *
     * @param activity 当前调用的活动名称
     */
    public DynamicAddDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        mActivity = activity;
        mPictureUtils = new PictureUtils(activity);
        mPictureUtils.setTAKE_PHOTH(21);
        mPictureUtils.setCHOOSE_PHOTH(22);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_dynamic_add);
        DialogCollectorUtils.addDialog(this);

        init();
    }

    /**
     * 初始化控件
     */
    public void init() {
        mTvAddBack = (TextView) findViewById(R.id.id_dyn_add_back);
        mTvAddBack.setOnClickListener(this);

        mTvAddSend = (TextView) findViewById(R.id.id_dyn_add_send);
        mTvAddSend.setOnClickListener(this);

        mRlDynamicImg = (RelativeLayout) findViewById(R.id.id_dyn_add_pick);
        mRlDynamicImg.setOnClickListener(this);

        mEtContent = (EditText) findViewById(R.id.id_dyn_add_content);

        mIvDynamicImg = (ImageView) findViewById(R.id.id_iv_dynamicadd_img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_dyn_add_back:// 取消
                cancel();// 返回上一个界面
                break;

            case R.id.id_dyn_add_send:// 发布
                String content = mEtContent.getText().toString().trim();
                if (!content.equals("")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    byte[] imgByte = image2byte(sUploadUri);

                    final Dynamic dynamic = new Dynamic(1, MainActivity.sUser.getuId(), MainActivity.sUser.getuNickName(), content,
                            "0", "0", sUploadUri, imgByte, sdf.format(new Date()), 0, null,
                            null);

                    HttpUtils.sendDynamicRequest(dynamic, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Gson gson = new Gson();
                            if (response.code() == 200) {
                                String json = response.body().string();
                                HttpMsgSimpleModel hmsm = gson.fromJson(json, HttpMsgSimpleModel.class);
                                if (hmsm.getResultCode().equals("200")) {
                                    Intent intent = new Intent("com.example.hsc.irunning.MY_LOCAL_BROADCAST");
                                    intent.putExtra("receiveCode", "2001");
                                    intent.putExtra("dynamic", new Gson().toJson(dynamic));
                                    MainActivity.mLocalBroadcastManager.sendBroadcast(intent);
                                }
                            }
                        }
                    });
                } else {
                    // 提示内容不能为空
                }
                this.cancel();
                break;

            case R.id.id_dyn_add_pick:// 配图
                showBottomChoose();
                break;
        }
    }

    // 将图片转换成byte数组
    public byte[] image2byte(String path) {
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    /**
     * 显示下方弹出框
     */
    private void showBottomChoose() {
        mBottomDialog = new Dialog(getContext(), R.style.DynamicBottomTheme);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_dynamic_add_bottom, null);

        Button btnTakePhoto = (Button) view.findViewById(R.id.id_dynamic_add_takePhoto);
        btnTakePhoto.setOnClickListener(mBottomClickListener);

        Button btnChoosePhoto = (Button) view.findViewById(R.id.id_dynamic_add_choosePhoto);
        btnChoosePhoto.setOnClickListener(mBottomClickListener);

        Button btnCancel = (Button) view.findViewById(R.id.id_dynamic_add_cancel);
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

    private View.OnClickListener mBottomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.id_dynamic_add_takePhoto:// 照相
                    mPictureUtils.openSystemCamera();
                    mBottomDialog.dismiss();// 关闭
                    break;
                case R.id.id_dynamic_add_choosePhoto:// 从手机选图
                    mPictureUtils.choosePhonePhotos();
                    mBottomDialog.dismiss();// 关闭
                    break;
                case R.id.id_dynamic_add_cancel:
                    mBottomDialog.dismiss();// 关闭
                    break;
            }
        }
    };

    // 异步消息处理
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String imgUrl = "";
            switch (msg.what) {
                case 211:// 处理照片显示逻辑
                    LogMsgUtil.Log_D("DynamicAddDialog", "已经收到显示照片消息");
                    Glide.with(mActivity.getApplicationContext())//
                            .load(mPictureUtils.imageUri)//
                            .override(200, 160)//
                            .into(mIvDynamicImg);// 加载图片
                    break;

                case 222:// 处理相册所选照片问题
                    Intent data = (Intent) msg.obj;

                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4以上系统
                        imgUrl = mPictureUtils.handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统
                        imgUrl = mPictureUtils.handleImageBeforeKitKat(data);
                    }
                    // 显示图片
                    Glide.with(mActivity.getApplicationContext())//
                            .load(imgUrl)//
                            .override(200, 160)//
                            .into(mIvDynamicImg);// 加载图片
                    sUploadUri = imgUrl;
                    break;
            }
        }
    };
}
