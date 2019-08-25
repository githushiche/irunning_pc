package com.example.hsc.irunning.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.main.bean.Recipe;

import java.util.List;

/**
 * Created by Diviner on 2019/4/22.
 * Vesion:1.0
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    private final String TAG = "RecipeAdapter";
    private List<Recipe> mRecipes;
    private Activity mActivity;

    public RecipeAdapter(Activity activity, List<Recipe> recipes) {
        mRecipes = recipes;
        mActivity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mRecipeView;

        private ImageView mIvRecipeImg1;
        private TextView mTvRecipeName1;
        private TextView mTvRecipeType;
        private TextView mTvCalOrGram;

        public ViewHolder(View view) {
            super(view);
            mRecipeView = view;
            // 加载布局
            mIvRecipeImg1 = (ImageView) view.findViewById(R.id.id_iv_recipe_img1);
            mTvRecipeName1 = (TextView) view.findViewById(R.id.id_tv_recipe_rname1);
            mTvRecipeType = (TextView) view.findViewById(R.id.id_tv_recipe_type);
            mTvCalOrGram = (TextView) view.findViewById(R.id.id_tv_recipe_cal_gram);
        }
    }

    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_healthy_recipe_item, parent, false);

        final RecipeAdapter.ViewHolder holder = new RecipeAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        Glide.with(mActivity)
                .load(recipe.getrPhoto())
                .dontAnimate()
                .override(200, 200)
                .into(holder.mIvRecipeImg1);

        holder.mTvRecipeName1.setText(recipe.getrName());
        holder.mTvCalOrGram.setText(recipe.getrKcal() + " Cal/" + recipe.getrGram() + "克");

        switch (recipe.getrType()) {
            case 1:
                holder.mTvRecipeType.setText("主食类");
                break;
            case 2:
                holder.mTvRecipeType.setText("肉蛋类");
                break;
            case 3:
                holder.mTvRecipeType.setText("水果类");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }
}
