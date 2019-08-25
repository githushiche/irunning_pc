package com.example.hsc.irunning.main.adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.Friend;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.dialog.FriendNotifiyDialog;
import com.example.hsc.irunning.main.http.HttpUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 好友验证通知
 *
 * @author Diviner
 * @date 2018-5-3 下午11:00:52
 */
public class FriendNotifiyAdapter extends ArrayAdapter<Friend> {
    private final String TAG = "FriendNotifiyAdapter";

    private int mResourceId;
    private List<Friend> mFriendsList;
    private User mUser;

    /**
     * 适配器的构造方法
     *
     * @param context
     * @param textViewResourceId
     * @param friends
     */
    public FriendNotifiyAdapter(Context context, int textViewResourceId,
                                List<Friend> friends) {
        super(context, textViewResourceId, friends);
        mResourceId = textViewResourceId;

        mFriendsList = friends;

        mUser = new User();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Friend friend = mFriendsList.get(position);// 取到当前项

        View view;// 声明一个视图存储器
        ViewHolder vh;

        if (convertView == null) {
            /*
             * 如果没有视图则加载一个视图
             */
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            vh = new ViewHolder();
            /*
             * 初始化控件
             */
            vh.mImageView = (ImageView) view
                    .findViewById(R.id.id_notifiy_picture);
            vh.mImageView.setImageResource(R.mipmap.step);

            vh.mUserName = (TextView) view.findViewById(R.id.id_notifiy_name);
            vh.mUserName.setText("admin");// 应该查询到用户名

            vh.mContent = (TextView) view.findViewById(R.id.id_notifiy_content);
            vh.mContent.setText(friend.getfAddInfo());

            vh.mTvApproved = (TextView) view.findViewById(R.id.tv_status);// 已同意
            // 同意
            vh.mTVAgreed = (TextView) view
                    .findViewById(R.id.id_add_fri_agree);
            // 拒绝
            vh.mTvRefuse = (TextView) view.findViewById(R.id.tv_do_refuse);

            if (friend.getfState() == 1) {
                // 已同意
                vh.mTvApproved.setVisibility(View.VISIBLE);
                vh.mTVAgreed.setVisibility(View.GONE);
                vh.mTvRefuse.setVisibility(View.GONE);
            } else if (friend.getfState() == 0) {
                vh.mTvApproved.setVisibility(View.GONE);
                vh.mTVAgreed.setVisibility(View.VISIBLE);
                vh.mTvRefuse.setVisibility(View.VISIBLE);
            }

            vh.mTVAgreed.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 同意状态码为1
                    LogMsgUtil.Log_D(TAG, "同意按钮------------------->");
                    //                    mOnclickMyButton.AgreedButtonClick(position);
                    friend.setfState(1);
                    HttpUtils.sendFriendRequest(friend, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogMsgUtil.Log_D(TAG, "FriendNotifiyDialog同意好友异常------->" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 200) {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = friend;
                                FriendNotifiyDialog.mHandler.sendMessage(message);
                            }
                        }
                    });
                }
            });

            vh.mTvRefuse.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 拒绝状态码为0
                    //                    mOnclickMyButton.RefuseButtonClick(position);
                    friend.setfState(0);
                    HttpUtils.sendFriendRequest(friend, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LogMsgUtil.Log_D(TAG, "FriendNotifiyDialog拒绝好友异常------->" + e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 200) {
                                Message message = new Message();
                                message.what = 0;
                                message.obj = friend;
                                FriendNotifiyDialog.mHandler.sendMessage(message);
                            }
                        }
                    });
                }
            });

            view.setTag(vh);
        } else {
            /*
             * 如果用视图的时候
             */
            view = convertView;// 直接把当前视图赋值给View
            vh = (ViewHolder) view.getTag();
        }

        return view;
    }

    private void sendRequest(List<Friend> friends) {
        // 发起网络请求查询用户详细信息
        HttpUtils.sendGetUserInfoRequest(friends, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogMsgUtil.Log_D(TAG, "FriendNotifiyAdapter异常-------->" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();

                    setData(json);
                }
            }
        });
    }

    private void setData(String json) {
        mUser = new Gson().fromJson(json, User.class);
    }

    /**
     * 组装数据
     *
     * @author Diviner
     * @date 2018-1-23 下午1:30:46
     */
    class ViewHolder {
        private ImageView mImageView;
        private TextView mUserName;
        private TextView mContent;

        private TextView mTvApproved;
        private TextView mTVAgreed;
        private TextView mTvRefuse;
    }
}
