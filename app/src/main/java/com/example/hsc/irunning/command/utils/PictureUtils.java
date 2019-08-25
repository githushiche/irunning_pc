package com.example.hsc.irunning.command.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * 图片处理帮助类
 * Created by Diviner on 2019/4/20.
 * Vesion:1.0
 */
public class PictureUtils {
    private final String TAG = "PictureUtils";
    private Activity mActivity;

    public int TAKE_PHOTH = 0;
    public int CHOOSE_PHOTH = 0;
    public static Uri imageUri;
    public static String takeUri;

    public PictureUtils(Activity activity) {
        mActivity = activity;
    }

    /**
     * 获取系统照相机
     */
    public void openSystemCamera() {
        takeUri = Environment.getExternalStorageDirectory() + "/" + "NewPhoto.jpg";
        File outputImage = new File(Environment.getExternalStorageDirectory(),
                "NewPhoto.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(mActivity, "com.example.hsc.irunning.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mActivity.startActivityForResult(intent, TAKE_PHOTH);
    }

    /**
     * 打开手机相册
     */
    public void choosePhonePhotos() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, CHOOSE_PHOTH);
    }

    /**
     * @param data
     * @return imagePath
     */
    @TargetApi(19)
    public String handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(mActivity, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];// 解析出数字格式id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagerPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagerPath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的uri,则用普通方式处理
            imagePath = getImagerPath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是文件类型的uri,直接获取图片路径就可以
            imagePath = uri.getPath();
        }
        LogMsgUtil.Log_D(TAG, "--------------照片地址是:" + imagePath);
        return imagePath;
    }

    public String handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagerPath(uri, null);

        LogMsgUtil.Log_D(TAG, "--------------照片地址是:" + imagePath);
        return imagePath;
    }

    public String getImagerPath(Uri uri, String selection) {
        String path = null;

        Cursor cursor = mActivity.getContentResolver().query(uri, null,
                selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
        return path;
    }

    public int getTAKE_PHOTH() {
        return TAKE_PHOTH;
    }

    public void setTAKE_PHOTH(int TAKE_PHOTH) {
        this.TAKE_PHOTH = TAKE_PHOTH;
    }

    public int getCHOOSE_PHOTH() {
        return CHOOSE_PHOTH;
    }

    public void setCHOOSE_PHOTH(int CHOOSE_PHOTH) {
        this.CHOOSE_PHOTH = CHOOSE_PHOTH;
    }
}
