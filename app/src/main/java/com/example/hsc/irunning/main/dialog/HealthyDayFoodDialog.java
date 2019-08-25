package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.utils.MapUtil;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.Recipe;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.example.hsc.irunning.stepcount.bean.StepEntity;
import com.example.hsc.irunning.stepcount.db.StepDataDao;
import com.example.hsc.irunning.stepcount.utils.TimeUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Diviner on 2019/4/25.
 * Vesion:1.0
 */
public class HealthyDayFoodDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "HealthyDayFoodDialog";
    private Activity mActivity;

    private List<Recipe> mRecipes;
    private List<Recipe> mRecipesRecommend;
    private ImageView mDayFoodImg1, mDayFoodImg2, mDayFoodImg3;
    private TextView mDayFoodName1, mDayFoodName2, mDayFoodName3;
    private TextView mDayFoodCal1, mDayFoodCal2, mDayFoodCal3;

    private ImageView mIvBack;
    private LinearLayout mProgressBar;
    private LinearLayout mShowView;
    private TextView mTvCount;

    private StepDataDao stepDataDao;
    private String curSelDate = "";

    public HealthyDayFoodDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);// 进入效果
        mActivity = activity;
        mRecipes = new ArrayList<>();
        mRecipesRecommend = new ArrayList<>();

        DialogCollectorUtils.addDialog(this);
        stepDataDao = new StepDataDao(mActivity);
        curSelDate = TimeUtil.getCurrentDate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_healthy_day_food);

        initView();
        sendHttp();
    }

    private void initView() {
        mDayFoodImg1 = (ImageView) findViewById(R.id.id_day_food_img1);
        mDayFoodImg2 = (ImageView) findViewById(R.id.id_day_food_img2);
        mDayFoodImg3 = (ImageView) findViewById(R.id.id_day_food_img3);

        mDayFoodName1 = (TextView) findViewById(R.id.id_day_food_name1);
        mDayFoodName2 = (TextView) findViewById(R.id.id_day_food_name2);
        mDayFoodName3 = (TextView) findViewById(R.id.id_day_food_name3);

        mDayFoodCal1 = (TextView) findViewById(R.id.id_day_food_cal_gram1);
        mDayFoodCal2 = (TextView) findViewById(R.id.id_day_food_cal_gram2);
        mDayFoodCal3 = (TextView) findViewById(R.id.id_day_food_cal_gram3);

        mProgressBar = (LinearLayout) findViewById(R.id.id_pb_day_food_progress);
        mShowView = (LinearLayout) findViewById(R.id.id_ly_day_food_views);

        mIvBack = (ImageView) findViewById(R.id.id_iv_day_food_back);
        mIvBack.setOnClickListener(this);

        mTvCount = (TextView) findViewById(R.id.id_healthy_day_food_count);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_day_food_back:
                this.dismiss();
                break;
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

                    if (setData(recipesList)) {
                        showData();
                    }
                    mRecipes = recipesList;
                }
            }
        });
    }

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

    private void showData() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDayFoodName1.setText(mRecipesRecommend.get(0).getrName());
                mDayFoodName2.setText(mRecipesRecommend.get(1).getrName());
                mDayFoodName3.setText(mRecipesRecommend.get(2).getrName());

                mDayFoodCal1.setText(mRecipesRecommend.get(0).getrKcal() + " Cal/" + mRecipesRecommend.get(0).getrGram() + "克");
                mDayFoodCal2.setText(mRecipesRecommend.get(1).getrKcal() + " Cal/" + mRecipesRecommend.get(1).getrGram() + "克");
                mDayFoodCal2.setText(mRecipesRecommend.get(2).getrKcal() + " Cal/" + mRecipesRecommend.get(2).getrGram() + "克");

                Glide.with(mActivity)
                        .load(mRecipesRecommend.get(0).getrPhoto())
                        .override(200, 200)
                        .into(mDayFoodImg1);
                Glide.with(mActivity)
                        .load(mRecipesRecommend.get(1).getrPhoto())
                        .override(200, 200)
                        .into(mDayFoodImg2);
                Glide.with(mActivity)
                        .load(mRecipesRecommend.get(2).getrPhoto())
                        .override(200, 200)
                        .into(mDayFoodImg3);

                int count = mRecipesRecommend.get(0).getrGram() + mRecipesRecommend.get(1).getrGram() + mRecipesRecommend.get(2).getrGram();

                mTvCount.setText("当前推荐饮食 " + count + " Cal");

                mProgressBar.setVisibility(View.GONE);
                mShowView.setVisibility(View.VISIBLE);
            }
        });
    }
}
