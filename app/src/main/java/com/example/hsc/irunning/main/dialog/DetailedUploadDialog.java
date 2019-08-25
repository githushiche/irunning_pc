package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.AppToastUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.command.utils.PictureUtils;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.http.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Diviner on 2019/4/20.
 * Vesion:1.0
 */
public class DetailedUploadDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "DetailedUploadDialog";

    private static Activity mActivity;
    private static PictureUtils mPictureUtils;

    private static ImageView mIvUserPhoto;
    private TextView mTvTake;
    private TextView mTvChoosePhoto;
    private TextView mTvUpload;
    private TextView mTvCal;

    private static String sUploadUri = "";

    public DetailedUploadDialog(Activity activity) {
        super(activity, R.style.DialogCentre);
        mActivity = activity;
        mPictureUtils = new PictureUtils(activity);
        mPictureUtils.setTAKE_PHOTH(11);
        mPictureUtils.setCHOOSE_PHOTH(12);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detailed_upload_layout);
        this.setCancelable(true);// 点击其他区域消息
        Window window = this.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);

        initView();
    }

    private void initView() {
        mIvUserPhoto = (ImageView) findViewById(R.id.id_iv_detailed_upload_photo);
        mIvUserPhoto.setImageBitmap(MainActivity.sUserBitmap);

        mTvTake = (TextView) findViewById(R.id.id_tv_detailed_take);
        mTvTake.setOnClickListener(this);

        mTvChoosePhoto = (TextView) findViewById(R.id.id_tv_detailed_choosePhoto);
        mTvChoosePhoto.setOnClickListener(this);

        mTvUpload = (TextView) findViewById(R.id.id_tv_detailed_upload);
        mTvUpload.setOnClickListener(this);

        mTvCal = (TextView) findViewById(R.id.id_tv_detailed_can);
        mTvCal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_detailed_take:// 拍照
                mPictureUtils.openSystemCamera();
                break;
            case R.id.id_tv_detailed_choosePhoto:// 从手机选择
                mPictureUtils.choosePhonePhotos();
                break;
            case R.id.id_tv_detailed_upload:// 上传图片
                HttpUtils.uploadImager(String.valueOf(MainActivity.sUser.getuId()), sUploadUri, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            LogMsgUtil.Log_D("MessageDialog", "图片上传成功");
                            Message message = new Message();
                            message.what = 9;
                            mHandler.sendMessage(message);
                        }
                    }
                });
                this.dismiss();
                break;
            case R.id.id_tv_detailed_can:// 取消
                onClose();
                break;
        }
    }

    // 异步消息处理
    public static Handler mHandler = new Handler() {
        String imgPath = "";

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:// 处理照片显示逻辑
                    Glide.with(mActivity.getApplicationContext())//
                            .load(mPictureUtils.imageUri)//
                            .override(66, 66)//
                            .into(mIvUserPhoto);// 加载图片
                    sUploadUri = mPictureUtils.imageUri.toString();
                    break;

                case 121:// 处理相册所选照片问题
                    Intent data = (Intent) msg.obj;

                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4以上系统
                        imgPath = mPictureUtils.handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统
                        imgPath = mPictureUtils.handleImageBeforeKitKat(data);
                    }

                    Glide.with(mActivity.getApplicationContext())//
                            .load(imgPath)//
                            .override(66, 66)//
                            .into(mIvUserPhoto);// 加载图片
                    sUploadUri = imgPath;
                    break;
                case 9:
                    AppToastUtils.toastShort(mActivity, "头像上传成功");

                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.sUserBitmap = BitmapFactory.decodeFile(sUploadUri);
                            mIvUserPhoto.setImageBitmap(MainActivity.sUserBitmap);
                        }
                    });
                    break;
            }
        }
    };

    /**
     * 关闭
     */
    public void onClose() {
        if (this != null) {
            this.dismiss();
        }
    }
}
