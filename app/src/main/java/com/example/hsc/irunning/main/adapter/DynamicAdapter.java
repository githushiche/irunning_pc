package com.example.hsc.irunning.main.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.Dynamic;
import com.example.hsc.irunning.main.bean.Likes;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 朋友动态适配器
 * Created by Diviner on 2019/4/11.
 * Vesion:1.0
 */
public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {
    private final String TAG = "DynamicAdapter";
    private Context mContext;

    private List<Dynamic> mDynamics;
    private Dynamic mDynamic;

    private final int UPDATE_UI = 10000;

    public DynamicAdapter(List<Dynamic> dynamics) {
        this.mDynamics = dynamics;
    }

    /**
     * 帮助类
     */
    static class ViewHolder extends RecyclerView.ViewHolder implements Serializable {
        private View mDynamicView;
        private CardView mCardView;
        private ImageView mIvDynamicUserImg;// 用户头像
        private ImageView mIvDynamicSendImg;// 配图
        private TextView mTvDynamicUserName;// 用户名称
        private TextView mTvDynamicSendTime;// 发布时间
        private TextView mTvDynamicContent;// 发布内容
        private TextView mTvDynamicKillKll;// 消耗卡路里
        private TextView mTvDynamicSteps;// 所走步数
        private TextView mTvDynamicLikes;// 点赞数量

        private Button mBtnDynamicLike;// 点赞
        private Button mBtnDynamicComment;// 评论按钮
        private EditText mEtDynamicComment;// 评论

        public ViewHolder(View itemView) {
            super(itemView);
            mDynamicView = itemView;

            mCardView = (CardView) itemView;

            mIvDynamicUserImg = (ImageView) itemView.findViewById(R.id.id_iv_dynamic_userimg);
            mIvDynamicSendImg = (ImageView) itemView.findViewById(R.id.id_iv_dynamic_photo);

            mTvDynamicUserName = (TextView) itemView.findViewById(R.id.id_tv_dynamic_username);
            mTvDynamicSendTime = (TextView) itemView.findViewById(R.id.id_tv_dynamic_time);
            mTvDynamicContent = (TextView) itemView.findViewById(R.id.id_tv_dynamic_content);
            mTvDynamicKillKll = (TextView) itemView.findViewById(R.id.id_tv_dynamic_kill_kll);
            mTvDynamicSteps = (TextView) itemView.findViewById(R.id.id_tv_dynamic_steps);
            mTvDynamicLikes = (TextView) itemView.findViewById(R.id.id_tv_dynamic_likes);

            mBtnDynamicLike = (Button) itemView.findViewById(R.id.id_btn_dynamic_like);

            mBtnDynamicComment = (Button) itemView.findViewById(R.id.id_btn_dynamic_comment);
            mEtDynamicComment = (EditText) itemView.findViewById(R.id.id_et_dynamic_comment);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_dynamic_list_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        holder.mDynamicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "的整个页面");
            }
        });

        holder.mBtnDynamicLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "界面的点赞按钮");
                int position = holder.getAdapterPosition();

                mDynamic = mDynamics.get(position);

                // 有人点赞则++
                int number = mDynamic.getdLikeNumber();
                mDynamic.setdLikeNumber((++number));

                List<Likes> likesList = new ArrayList<>();
                Likes likes = new Likes(1, mDynamic.getdId(), mDynamic.getuId(), MainActivity.sUser.getuId(),
                        MainActivity.sUser.getuName());
                likesList.add(likes);

                mDynamic.setdLisks(likesList);// 把点赞信息加入到动态的集合中

                HttpUtils.updateDynamicRequest(mDynamic, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            String json = response.body().string();

                            final Dynamic dynamics = new Gson().fromJson(json, Dynamic.class);
                            String likesPoeple = "";
                            if (dynamics.getdLisks().size() != 0) {
                                for (int i = 0; i < dynamics.getdLisks().size(); i++) {
                                    if (dynamics.getdId() == dynamics.getdLisks().get(i).getdId()) {
                                        if (i != dynamics.getdLisks().size()) {// 不等于最后的时候
                                            likesPoeple += dynamics.getdLisks().get(i).getuName();
                                        }
                                        if (dynamics.getdLisks().size() >= 2) {
                                            if (i != dynamics.getdLisks().size() - 1) {
                                                likesPoeple += ",";
                                            }
                                        }
                                    }
                                }
                                if (dynamics.getuId() != MainActivity.sUser.getuId()) {
                                    likesPoeple += "等" + dynamics.getdLisks().size() + "人赞了他";
                                } else {
                                    likesPoeple += "等" + dynamics.getdLisks().size() + "人赞了你";
                                }

                                if (likesPoeple.equals("")) {
                                    //            holder.mTvDynamicLikes.setVisibility(View.GONE);// 隐藏
                                } else {
                                    Message message = new Message();
                                    message.what = UPDATE_UI;
                                    message.obj = likesPoeple;
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("vh", holder);
                                    message.setData(bundle);
                                    mHandler.sendMessage(message);
                                }
                            }
                        }
                    }
                });
            }
        });

        holder.mBtnDynamicComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 评论按钮点击事件
                LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "界面的评论按钮");

                int positon = holder.getAdapterPosition();
                initBottomEdit();
            }
        });

        holder.mEtDynamicComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBottomEdit();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mDynamic = mDynamics.get(position);

        Glide.with(mContext)
                .load(mDynamic.getdImg())
                .override(400, 300)
                .dontAnimate()
                .into(holder.mIvDynamicSendImg);
        holder.mTvDynamicUserName.setText(mDynamic.getuName());
        holder.mTvDynamicSendTime.setText(mDynamic.getdSendTime());
        holder.mTvDynamicContent.setText(mDynamic.getdContent());
        holder.mTvDynamicKillKll.setText("已经消耗了" + mDynamic.getdCalories() + "卡路里");
        holder.mTvDynamicSteps.setText("行走了" + mDynamic.getdStepCount() + "步");

        String likePeople = "";
        if (mDynamic.getdLisks() != null) {
            if (mDynamic.getdLisks().size() != 0) {
                for (int i = 0; i < mDynamic.getdLisks().size(); i++) {
                    if (mDynamic.getdId() == mDynamic.getdLisks().get(i).getdId()) {
                        if (i != mDynamic.getdLisks().size()) {// 不等于最后的时候
                            likePeople += mDynamic.getdLisks().get(i).getuName();
                        }
                        if (mDynamic.getdLisks().size() >= 2) {
                            if (i != mDynamic.getdLisks().size() - 1) {
                                likePeople += ",";
                            }
                        }
                    }
                }
                if (mDynamic.getuId() != MainActivity.sUser.getuId()) {
                    likePeople += "等" + mDynamic.getdLisks().size() + "人赞了他";
                } else {
                    likePeople += "等" + mDynamic.getdLisks().size() + "人赞了你";
                }
                if (likePeople.equals("")) {
                    //            holder.mTvDynamicLikes.setVisibility(View.GONE);// 隐藏
                } else {
                    holder.mTvDynamicLikes.setText(likePeople);
                }
            } else {
                holder.mTvDynamicLikes.setText("暂时没有人赞你");
            }
        } else {
            holder.mTvDynamicLikes.setText("暂时没有人赞你");
        }
    }

    /**
     * 评论软键盘弹出
     */
    private void initBottomEdit() {
        Dialog mBottomDialog = new Dialog(mContext, R.style.DynamicBottomTheme1);
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_dynamic_list_edittext, null);

        EditText CommentEdit = (EditText) view.findViewById(R.id.id_et_dynamic_pop_comment);

        mBottomDialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mBottomDialog.setContentView(view);

        //获取当前Activity所在的窗体
        Window dialogWindow = mBottomDialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        mBottomDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                // 先显示dialog在显示软键盘
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        //设置弹出动画
        dialogWindow.setWindowAnimations(R.style.animTranslate);
        // 出现后保持的位置
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mBottomDialog.show();//显示对话框


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:// 异步消息码
                    ViewHolder vh = (ViewHolder) msg.getData().getSerializable("vh");
                    vh.mTvDynamicLikes.setText((String) msg.obj);// 更新ui
                    break;
            }
        }
    };

    @Override
    public int getItemCount() {
        return mDynamics.size();
    }

}
