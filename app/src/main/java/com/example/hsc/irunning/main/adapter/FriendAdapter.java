package com.example.hsc.irunning.main.adapter;

import android.content.Context;
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
 * 好友列表适配器
 *
 * @author Diviner
 * @date 2018-5-1 下午9:44:46
 */
public class FriendAdapter extends ArrayAdapter<User> {
    private final String TAG = "FriendAdapter";
    private int mResourceId;
    private List<User> mFriends;
    private Context mContext;
    private byte[] Picture_bt;

    /**
     * 适配器的构造方法
     *
     * @param context
     * @param textViewResourceId
     * @param objects
     */
    public FriendAdapter(Context context, int textViewResourceId,
                         List<User> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        mResourceId = textViewResourceId;
        mFriends = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = mFriends.get(position);// 取到当前项

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
            setPic(user);
            vh.mImageView = (ImageView) view
                    .findViewById(R.id.id_iv_friend_item_picture);

            // 设置好友头像
            Glide.with(mContext)
                    .load(Picture_bt)
                    .dontAnimate()
                    .override(50, 50)
                    .into(vh.mImageView);

            vh.mUserName = (TextView) view.findViewById(R.id.id_tv_friend_nick);

            if (user != null) {
                vh.mUserName.setText(user.getuNickName());
            }

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
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

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
    }

}
