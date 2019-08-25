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
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.Dishes;
import com.example.hsc.irunning.main.dialog.DishesDetailsDialog;

import java.util.List;

/**
 * Created by Diviner on 2019/4/22.
 * Vesion:1.0
 */
public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.ViewHolder> {
    private final String TAG = "DishesAdapter";
    private List<Dishes> mDishes;
    private Activity mActivity;

    public DishesAdapter(Activity activity, List<Dishes> dishes) {
        mDishes = dishes;
        mActivity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View mDishesView;

        private ImageView mIvDishesImg;
        private TextView mTvDishesName;

        public ViewHolder(View view) {
            super(view);
            mDishesView = view;
            // 加载布局
            mIvDishesImg = (ImageView) view.findViewById(R.id.id_iv_dishes_img);
            mTvDishesName = (TextView) view.findViewById(R.id.id_tv_dishes_name);
        }
    }

    @Override
    public DishesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_healthy_dishes_item, parent, false);

        final DishesAdapter.ViewHolder holder = new DishesAdapter.ViewHolder(view);
        holder.mDishesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "的整个页面");

                int position = holder.getAdapterPosition();
                Dishes dishes = mDishes.get(position);
                new DishesDetailsDialog(mActivity, dishes).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(DishesAdapter.ViewHolder holder, int position) {
        Dishes dishes = mDishes.get(position);

        Glide.with(mActivity)
                .load(dishes.getdImg())
                .dontAnimate()
                .into(holder.mIvDishesImg);
        holder.mTvDishesName.setText(dishes.getdName());
    }

    @Override
    public int getItemCount() {
        return mDishes.size();
    }

}
