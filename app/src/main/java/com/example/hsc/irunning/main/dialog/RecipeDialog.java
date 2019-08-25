package com.example.hsc.irunning.main.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.DialogCollectorUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.adapter.RecipeAdapter;
import com.example.hsc.irunning.main.bean.Recipe;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Diviner on 2019/4/22.
 * Vesion:1.0
 */
public class RecipeDialog extends Dialog implements View.OnClickListener {
    private final String TAG = "DishesDialog";
    private Activity mActivity;

    // recyclerView适配器相关
    private List<Recipe> mRecipes;
    private RecyclerView mRecipeRecycler;
    private RecipeAdapter mRecipeAdapter;

    private Button mBtnStapleFood, mBtnFruit, mBtnMeatEggs;
    private ImageView mIvBack;
    private LinearLayout mRecipeProgressBar;

    public RecipeDialog(Activity activity) {
        super(activity, R.style.Animation_Dialog_RightInRightOut1);
        this.mActivity = activity;
        DialogCollectorUtils.addDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_healthy_recipe_layout);

        mRecipes = new ArrayList<>();

        initView();
        sendHttp();
    }

    private void initView() {
        mRecipeRecycler = (RecyclerView) findViewById(R.id.id_rv_recipe_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        mRecipeRecycler.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter(mActivity, mRecipes);
        mRecipeRecycler.setAdapter(mRecipeAdapter);

        mBtnStapleFood = (Button) findViewById(R.id.id_btn_recipe_staple_food);
        mBtnStapleFood.setOnClickListener(this);

        mBtnFruit = (Button) findViewById(R.id.id_btn_recipe_fruit);
        mBtnFruit.setOnClickListener(this);

        mBtnMeatEggs = (Button) findViewById(R.id.id_btn_recipe_meat_eggs);
        mBtnMeatEggs.setOnClickListener(this);

        mIvBack = (ImageView) findViewById(R.id.id_iv_recipe_back);
        mIvBack.setOnClickListener(this);

        mRecipeProgressBar = findViewById(R.id.id_pb_recipe_progress);
    }

    @Override
    public void onClick(View view) {
        int type = 0;
        switch (view.getId()) {
            case R.id.id_btn_recipe_staple_food:
                mBtnStapleFood.setTextColor(Color.RED);
                mBtnFruit.setTextColor(Color.WHITE);
                mBtnMeatEggs.setTextColor(Color.WHITE);

                type = 1;
                byTypeSetData(type);
                break;
            case R.id.id_btn_recipe_fruit:// 水果
                mBtnStapleFood.setTextColor(Color.WHITE);
                mBtnFruit.setTextColor(Color.RED);
                mBtnMeatEggs.setTextColor(Color.WHITE);

                type = 3;
                byTypeSetData(type);
                break;
            case R.id.id_btn_recipe_meat_eggs:
                mBtnStapleFood.setTextColor(Color.WHITE);
                mBtnFruit.setTextColor(Color.WHITE);
                mBtnMeatEggs.setTextColor(Color.RED);

                type = 2;
                byTypeSetData(type);
                break;

            case R.id.id_iv_recipe_back:
                this.dismiss();
                break;
        }
    }

    private void byTypeSetData(final int type) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Recipe> recipes = new ArrayList<>();
                for (Recipe recipe : mRecipes) {
                    if (recipe.getrType() == type) {
                        recipes.add(recipe);
                    }
                }

                mRecipeAdapter = new RecipeAdapter(mActivity, recipes);
                mRecipeRecycler.setAdapter(mRecipeAdapter);
                mRecipeAdapter.notifyDataSetChanged();
            }
        });
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
                    sendData(recipesList);
                }
            }
        });
    }

    private void sendData(final List<Recipe> recipes) {
        mRecipes = recipes;
        final List<Recipe> recipeList = new ArrayList<>();
        for (Recipe recipe : mRecipes) {
            if (recipe.getrType() == 1) {
                recipeList.add(recipe);
            }
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (recipeList.size() > 0) {
                    mRecipeProgressBar.setVisibility(View.GONE);
                    mRecipeRecycler.setVisibility(View.VISIBLE);
                    mRecipeAdapter = new RecipeAdapter(mActivity, recipeList);
                    mRecipeRecycler.setAdapter(mRecipeAdapter);
                    mRecipeAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
