package com.example.hsc.irunning.main.http;

import com.example.hsc.irunning.main.bean.Dynamic;
import com.example.hsc.irunning.main.bean.Friend;
import com.example.hsc.irunning.main.bean.User;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Diviner
 * Time:2018/12/20.
 * Version:1.0
 */

public class HttpUtils {
    //    private static final String sUri = "http://10.3.65.211:8080/mes/";
    private static final String sUri = "http://192.168.43.17:8080/mes/";
    private static OkHttpClient mClient = new OkHttpClient();

    /**
     * 用户注册请求
     *
     * @param userName
     * @param passWord
     * @param callback
     */
    public static void sendRegisterRequest(String userName, String passWord, Callback callback) {
        FormBody formBody = new FormBody.Builder()
                .add("userName", userName)
                .add("passWord", passWord)
                .build();

        Request request = new Request.Builder()
                .url(sUri + "user/register")
                .post(formBody)
                .build();

        mClient.newCall(request).enqueue(callback);// 发起新对话并回调方法
    }

    /**
     * 用户登陆请求
     *
     * @param userName
     * @param passWord
     * @param callback
     */
    public static void sendLoginRequest(String userName, String passWord, Callback callback) {
        OkHttpClient client = new OkHttpClient();// 创建客户端

        FormBody formBody = new FormBody.Builder()//
                .add("userName", userName)//
                .add("passWord", passWord)//
                .build();// 构建表单提交数据

        Request request = new Request.Builder()//
                .url(sUri + "user/login")//
                .post(formBody)//
                .build();

        client.newCall(request)//创建新会话
                .enqueue(callback);
    }

    /**
     * 更新用户信息请求
     *
     * @param user
     * @param callback
     */
    public static void sendUpdateInfoRequest(User user, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , new Gson().toJson(user));// 利用表单传递json数据

        Request request = new Request.Builder()
                .url(sUri + "user/update")//
                .post(requestBody)//
                .build();

        client.newCall(request).enqueue(callback);// 创建新对话发送请求并回调
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param friends
     * @param callback
     */
    public static void sendGetUserInfoRequest(List<Friend> friends, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , new Gson().toJson(friends));// 利用表单传递json数据

        Request request = new Request.Builder()
                .url(sUri + "friend/queryById")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 通过用户自己的id来查询到关联的好友
     *
     * @param uid
     * @param callback
     */
    public static void sendFriendListRequest(int uid, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .build();

        Request request = new Request.Builder()
                .url(sUri + "friend/friendList")//
                .post(formBody)//
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 查询好友
     *
     * @param nikeName 通过这个参数来进行模糊查询
     */
    public static void sendQueryFriendRequest(String nikeName, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("nickName", nikeName)
                .build();

        Request request = new Request.Builder()
                .url(sUri + "friend/query")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 发送添加好友的请求
     *
     * @param friend
     * @param callback
     */
    public static void sendAddFriendRequest(Friend friend, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , new Gson().toJson(friend));// 利用表单传递json数据

        Request request = new Request.Builder()
                .url(sUri + "friend/addFriendRequest")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 获取好友请求
     *
     * @param callback
     */
    public static void sendGetFriendRequest(int uid, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .build();

        Request request = new Request.Builder()
                .url(sUri + "friend/getFriendRequest")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 添加好友--接受 or 拒绝
     *
     * @param friend
     * @param callback
     */
    public static void sendFriendRequest(Friend friend, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , new Gson().toJson(friend));// 利用表单传递json数据

        Request request = new Request.Builder()
                .url(sUri + "friend/addFriend")//
                .post(requestBody)//
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 获取全部动态
     *
     * @param callback
     */
    public static void getDynamicRequest(String uid, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .build();

        Request request = new Request.Builder()
                .url(sUri + "dynamic/findAllDynamicById")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 发布动态
     */
    public static void sendDynamicRequest(Dynamic dynamic, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , new Gson().toJson(dynamic));// 利用表单传递json数据

        Request request = new Request.Builder()
                .url(sUri + "dynamic/sendDynamic")//
                .post(requestBody)//
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 更新动态数据
     *
     * @param dynamic
     * @param callback
     */
    public static void updateDynamicRequest(Dynamic dynamic, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , new Gson().toJson(dynamic));// 利用表单传递json数据

        Request request = new Request.Builder()
                .url(sUri + "dynamic/addDynamicLike")//
                .post(requestBody)//
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 图片上传
     *
     * @param uId
     * @param imagePath
     * @param callback
     */
    public static void uploadImager(String uId, String imagePath, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        File file = new File(imagePath);
        MediaType photoType = MediaType.parse("image/png");
        RequestBody imagerBody = RequestBody.create(photoType, file);

        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, imagerBody)
                .addFormDataPart("uId", uId)
                .build();

        Request request = new Request.Builder()
                .url(sUri + "uploadImager/fileUpload")//
                .post(requestBody)//
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void downloadImage(String imgName, Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("imgName", String.valueOf(imgName))
                .build();

        Request request = new Request.Builder()
                .url(sUri + "uploadImager/downloadImage")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 好友图片消息上传图片
     *
     * @param uId
     * @param formId
     * @param imagePath
     * @param callback
     */
    public static void uploadMessageImager(String uId, String formId, String imagePath, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        File file = new File(imagePath);
        MediaType photoType = MediaType.parse("image/png");
        RequestBody imagerBody = RequestBody.create(photoType, file);

        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imagePath, imagerBody)
                .addFormDataPart("uId", uId)
                .addFormDataPart("formId", formId)
                .build();

        Request request = new Request.Builder()
                .url(sUri + "uploadImager/uploadMessageImg")//
                .post(requestBody)//
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void downloadMessageImage(String uId, String formId, String imgName, Callback callback) throws IOException {
        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("uId", uId)
                .add("formId", formId)
                .add("imgName", String.valueOf(imgName))
                .build();

        Request request = new Request.Builder()
                .url(sUri + "uploadImager/downloadMessageImage")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 营养美食博文
     *
     * @param callback
     * @throws IOException
     */
    public static void foodBlogList(Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(sUri + "foodBlog/findFoodList")
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 营养菜品
     */
    public static void dishesList(Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(sUri + "dishes/findDishesList")
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * 菜品搭配
     *
     * @param callback
     */
    public static void RecipeList(Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(sUri + "recipe/findRecipeList")
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void videoList(Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(sUri + "uploadImager/downloadVideo")
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void sendHttpRequest(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()//
                .url(address)//
                .build();
        client.newCall(request).enqueue(callback);
    }


}
