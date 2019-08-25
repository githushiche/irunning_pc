package com.example.hsc.irunning.main.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.utils.MapUtil;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.Recipe;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.example.hsc.irunning.stepcount.bean.StepEntity;
import com.example.hsc.irunning.stepcount.db.StepDataDao;
import com.example.hsc.irunning.stepcount.utils.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 菜品推荐广播
 * Created by Diviner on 2019/5/23.
 * Vesion:1.0
 */
public class DishesPushNotification {
    private final String TAG = "FriendMessageNotification";
    private Context mContext;

    private Notification mNotification;
    private NotificationManager mNotificationManager;

    public DishesPushNotification(Context context) {
        mContext = context;

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        curSelDate = TimeUtil.getCurrentDate();

        stepDataDao = new StepDataDao(context);
        mRecipesRecommend = new ArrayList<>();
    }

    /**
     * 显示单独一条菜谱推送
     */
    public void initNotification() {
        // 用于点击通知之后跳转到指定界面
        Intent intent = new Intent(mContext, FriendMessageNotification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

        sendHttp();

        if (mRecipesRecommend.size() > 0) {
            mNotification = new NotificationCompat.Builder(mContext)
                    .setContentTitle("当前推荐菜品为")
                    .setContentText(mRecipesRecommend.get(0).getrName() + "+" + mRecipesRecommend.get(1).getrName() + "+" + mRecipesRecommend.get(2).getrName())
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeByteArray(mRecipesRecommend.get(0).getrPhoto(), 0, mRecipesRecommend.get(0).getrPhoto().length)))
                    .setWhen(System.currentTimeMillis())
                    .setSound(Uri.fromFile(new File("/system/media/audio/notifications/Beep_Once.ogg")))// 通知声音
                    .setVibrate(new long[]{0, 1000, 0, 0})
                    .setSmallIcon(R.mipmap.fimg)
                    //                .setContentIntent(pendingIntent)
                    .setAutoCancel(true)// 点击自动取消
                    .setPriority(NotificationCompat.PRIORITY_MAX)// 表示通知很重要
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.fimg))
                    .build();
            showNotification();
        }
    }

    private void sendHttp() {
        HttpUtils.RecipeList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();

                    LogMsgUtil.Log_D(TAG, "json如下------------>" + json);

                    Gson gson = new Gson();
                    List<Recipe> recipesList = gson.fromJson(json,
                            new TypeToken<List<Recipe>>() {
                            }.getType());

                    setData(recipesList);
                }
            }
        });
    }

    private StepDataDao stepDataDao;
    private String curSelDate = "";
    private List<Recipe> mRecipesRecommend;

    private boolean setData(final List<Recipe> recipes) {
        /**
         * 需要热量
         * 成人每日需要的热量 =人体基础代谢的需要的基本热量 + 体力活动所需要的热量 + 消化食物所需要的热量。
         * 消化食物所需要的热量 =10% x （人体基础代谢的需要的最低热量 +体力活动所需要的热量）
         * 成人每日需要的热量 = 1.1 x (人体基础代谢的需要的最低基本热量 +体力活动所需要的热量 )
         * 男性 ： 9250- 10090千 焦耳
         * 女性： 7980 - 8820千 焦耳
         * 注意：每日由食物提供的热量应不少于己于 5000千焦耳- 7500 千焦耳 这是维持人体正常生命活动的最少的能量
         *
         * 人体基础代谢的需要基本热量 简单算法
         * 女子 ： 基本热量（大卡）= 体重(斤） x 9
         * 男子 ： 基本热量（大卡）= 体重(斤） x 10
         *
         * 年龄 公式
         * 女子
         * 18- 30 岁 14.6 x 体重（公斤） + 450
         * 31- 60 岁 8.6 x 体重（公斤） + 830
         * 60岁以上 10.4 x 体重（公斤） + 600
         *
         * 男子
         * 18- 30 岁 15.2 x 体重（公斤）+ 680
         * 31- 60 岁 11.5 x 体重（公斤） + 830
         * 60岁以上 13.4 x 体重（公斤） + 490
         */

        int age = MainActivity.sUser.getuUserInfo().getuAge();
        int weight = MainActivity.sUser.getuUserInfo().getuWeight();
        int sex = MainActivity.sUser.getuUserInfo().getuSex();

        double dailyNeedCalories = 0;// 成人每日需要的热量
        double basicHeat = 0;// 人体基础代谢的基本热量
        double digestFoodHeat = 0;// 消化食物所需要的热量
        double needHot = 0;// 需要相减能量
        int cal = 0;
        int steps = 0;

        StepEntity stepEntity = stepDataDao.getCurDataByDate(curSelDate);

        if (stepEntity != null) {// 获取步数集合
            steps = Integer.parseInt(stepEntity.getSteps());
            cal = MapUtil.CalculationCalories(weight, Double.parseDouble(stepEntity.getSteps()));
        }

        if (age >= 18 && age <= 30) {// 18-30岁
            switch (sex) {
                case 0:// 男
                    basicHeat = weight * 10;
                    digestFoodHeat = 0.1 * (9250 + cal);
                    dailyNeedCalories = basicHeat + cal + digestFoodHeat;
                    needHot = 15.2 * (weight / 2) + 450;
                    break;
                case 1:// 女
                    basicHeat = weight * 9;
                    digestFoodHeat = 0.1 * (7980 + cal);
                    dailyNeedCalories = basicHeat + cal + digestFoodHeat;
                    needHot = 14.6 * (weight / 2) + 450;
                    break;
            }
        } else if (age >= 30 && age <= 60) {// 30-60岁

        }

        List<Recipe> stapleFoods = new ArrayList<>();// 主食
        List<Recipe> fruits = new ArrayList<>();// 水果
        List<Recipe> MeatEggs = new ArrayList<>();// 肉蛋

        if (cal == 0 && steps == 0) {
            for (Recipe recipe : recipes) {
                switch (recipe.getrType()) {
                    case 1:
                        stapleFoods.add(recipe);
                        break;
                    case 2:
                        MeatEggs.add(recipe);
                        break;
                    case 3:
                        fruits.add(recipe);
                        break;
                }
            }

            mRecipesRecommend.add(stapleFoods.get(0));
            mRecipesRecommend.add(MeatEggs.get(0));
            mRecipesRecommend.add(fruits.get(0));
        } else {
            for (Recipe recipe : recipes) {
                switch (recipe.getrType()) {
                    case 1:
                        stapleFoods.add(recipe);
                        break;
                    case 2:
                        MeatEggs.add(recipe);
                        break;
                    case 3:
                        fruits.add(recipe);
                        break;
                }
            }

            mRecipesRecommend.clear();

            for (int i = 0; i < stapleFoods.size(); i++) {
                for (int k = 0; k < fruits.size(); k++) {
                    for (int n = 0; n < MeatEggs.size(); n++) {
                        int Cal = stapleFoods.get(i).getrKcal() + fruits.get(k).getrKcal() + MeatEggs.get(n).getrKcal();
                        if ((cal + dailyNeedCalories) - needHot > Cal) {
                            mRecipesRecommend.add(stapleFoods.get(i));
                            mRecipesRecommend.add(fruits.get(k));
                            mRecipesRecommend.add(MeatEggs.get(n));
                        }
                    }
                }
            }
        }
        if (mRecipesRecommend.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void showNotification() {
        mNotificationManager.notify(1, mNotification);
    }
}
