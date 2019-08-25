package com.example.hsc.irunning.main.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.http.HttpUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 添加朋友适配器
 *
 * @author Diviner
 */
public class AddFriendAdapter extends ArrayAdapter<User> {
    private final String TAG = "AddFriendAdapter";
    private int mResourceId;
    private Context mContext;

    private List<User> mUsers;
    private byte[] Picture_bt;
    private ViewHolder mViewHolder;

    /**
     * 适配器的构造方法
     *
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public AddFriendAdapter(Context context, int textViewResourceId,
                            List<User> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mResourceId = textViewResourceId;
        mUsers = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = mUsers.get(position);// 取到当前项

        View view;// 声明一个视图存储器

        if (convertView == null) {
            /*
             * 如果没有视图则加载一个视图
             */
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            mViewHolder = new ViewHolder();
            /*
             * 初始化控件
             */
            mViewHolder.mImageView = (ImageView) view
                    .findViewById(R.id.id_iv_friend_item_picture);
            mViewHolder.mUserName = (TextView) view.findViewById(R.id.id_tv_friend_nick);

            view.setTag(mViewHolder);
        } else {
            /*
             * 如果用视图的时候
             */
            view = convertView;// 直接把当前视图赋值给View
            mViewHolder = (ViewHolder) view.getTag();
        }
        if (user != null) {
            mViewHolder.mUserName.setText(user.getuNickName());
        }
        setPic(user);

        return view;
    }

    private void setPic(User user) {
        try {
            HttpUtils.downloadImage(user.getuUserPicture().getuPicturePath(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogMsgUtil.Log_D(TAG, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        Picture_bt = response.body().bytes();

                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 设置好友头像
                    Glide.with(mContext)
                            .load(Picture_bt)
                            .dontAnimate()
                            .override(50, 50)
                            .into(mViewHolder.mImageView);
                    break;
            }
        }
    };

    /**
     * 组装数据
     *
     * @author Diviner
     * @date 2018-1-23 下午1:30:46
     */
    class ViewHolder {
        private ImageView mImageView;
        private TextView mUserName;
    }

}
