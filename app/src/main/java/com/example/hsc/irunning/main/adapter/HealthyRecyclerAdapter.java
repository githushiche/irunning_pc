package com.example.hsc.irunning.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.HealthyRecycler;
import com.example.hsc.irunning.main.dialog.DishesDialog;
import com.example.hsc.irunning.main.dialog.FoodBlogDialog;
import com.example.hsc.irunning.main.dialog.HealthyDayFoodDialog;
import com.example.hsc.irunning.main.dialog.RecipeDialog;

import java.util.List;

/**
 * Created by Diviner on 2019/4/4.
 * Vesion:1.0
 */
public class HealthyRecyclerAdapter extends RecyclerView.Adapter<HealthyRecyclerAdapter.ViewHolder> {
    private final String TAG = "HealthyRecyclerAdapter";
    private Activity mActivity;
    private List<HealthyRecycler> mHealthyRecyclers;

    public HealthyRecyclerAdapter(Activity activity, List<HealthyRecycler> healthyRecyclers) {
        mHealthyRecyclers = healthyRecyclers;
        mActivity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mHealthyView;
        private ImageView mIvFeaturesImg;
        private TextView mTvFeaturesName;

        public ViewHolder(View itemView) {
            super(itemView);
            mHealthyView = itemView;

            mIvFeaturesImg = (ImageView) itemView.findViewById(R.id.id_healthy_item_img);
            mTvFeaturesName = (TextView) itemView.findViewById(R.id.id_healthy_item_text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_healthy_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.mHealthyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                HealthyRecycler hr = mHealthyRecyclers.get(position);
                switch (hr.gethType()) {
                    case 1:// 今日推荐
                        LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "界面的" + hr.gethName() + "图标");
                        new HealthyDayFoodDialog(mActivity).show();
                        break;
                    case 2:// 营养菜品搭配
                        LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "界面的" + hr.gethName() + "图标");
                        new RecipeDialog(mActivity).show();
                        break;
                    case 3:// 营养菜品推荐
                        LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "界面的" + hr.gethName() + "图标");
                        new DishesDialog(mActivity).show();
                        break;
                    case 4:// 美食博文推荐
                        LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "界面的" + hr.gethName() + "图标");
                        new FoodBlogDialog(mActivity).show();
                        break;
                }

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HealthyRecycler healthyRecycler = mHealthyRecyclers.get(position);

        holder.mIvFeaturesImg.setImageResource(healthyRecycler.gethImgId());
        holder.mTvFeaturesName.setText(healthyRecycler.gethName());
    }

    @Override
    public int getItemCount() {
        return mHealthyRecyclers.size();
    }
}
