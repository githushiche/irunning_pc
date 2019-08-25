package com.example.hsc.irunning.main.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.FoodBlog;
import com.example.hsc.irunning.main.dialog.FoodBlogDetailsDialog;

import java.util.List;

/**
 * Created by Diviner on 2019/4/21.
 * Vesion:1.0
 */
public class FoodBlogAdapter extends RecyclerView.Adapter<FoodBlogAdapter.ViewHolder> {
    private final String TAG = "FoodBlogAdapter";
    private List<FoodBlog> mFoodBlogs;
    private Activity mActivity;

    public FoodBlogAdapter(Activity activity, List<FoodBlog> foodBlogs) {
        mFoodBlogs = foodBlogs;
        mActivity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mFoodView;

        private TextView mTvFoodTitle;
        private TextView mTvFoodTime;
        private TextView mTvFoodContent;

        public ViewHolder(View view) {
            super(view);
            mFoodView = view;

            mTvFoodTitle = (TextView) mFoodView.findViewById(R.id.id_tv_food_title);
            mTvFoodTime = (TextView) mFoodView.findViewById(R.id.id_tv_food_time);
            mTvFoodContent = (TextView) mFoodView.findViewById(R.id.id_tv_food_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_healthy_food_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        holder.mFoodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                FoodBlog foodBlog = mFoodBlogs.get(position);

                new FoodBlogDetailsDialog(mActivity, foodBlog).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodBlog foodBlog = mFoodBlogs.get(position);

        LogMsgUtil.Log_D(TAG, "数据--->" + foodBlog.getfName());

        holder.mTvFoodTitle.setText(foodBlog.getfName());
        holder.mTvFoodContent.setText(foodBlog.getfContent());
    }

    @Override
    public int getItemCount() {
        return mFoodBlogs.size();
    }
}
