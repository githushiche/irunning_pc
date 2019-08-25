package com.example.hsc.irunning.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.main.bean.FriendMessage;
import com.example.hsc.irunning.main.bean.User;

import java.util.List;

/**
 * 好友消息列表适配器
 *
 * @author Diviner
 * @date 2018-5-1 下午9:44:54
 */
public class FriendMsgAdapter extends BaseAdapter {
    private final String TAG = "FriendMsgAdapter";
    private Context mContext;
    private LayoutInflater mLinearLayout;// 总布局
    private List<User> mMessages;// 用户和消息的集合

    public FriendMsgAdapter(Context context, List<User> message) {
        mContext = context;
        mLinearLayout = LayoutInflater.from(mContext);
        mMessages = message;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        User user = mMessages.get(position);
        FriendMessage friendMessage = user.getuFriendMessages().get(0);

        if (convertView == null) {
            if (user == null) {
                // 说明没有数据
                view = mLinearLayout.inflate(R.layout.fragment_message_no_message, null);

                TextView tv = (TextView) view.findViewById(R.id.id_no_content);
                tv.setText("当前无最新消息");
            } else {
                view = mLinearLayout.inflate(R.layout.fragment_message_list_item, null);

                viewHolder = new ViewHolder();// 缓存处理
                viewHolder.mImageView = (ImageView) view
                        .findViewById(R.id.id_img_recent_img);
                viewHolder.mUserName = (TextView) view
                        .findViewById(R.id.id_tv_recent_name);
                viewHolder.mContent = (TextView) view
                        .findViewById(R.id.id_tv_recent_content);
                viewHolder.mSendTime = (TextView) view
                        .findViewById(R.id.id_tv_recent_time);
                viewHolder.mMessageCount = (TextView) view.findViewById(R.id.id_fragment_message_count);

                //                viewHolder.mImageView.setImageResource(fm.get);
                viewHolder.mUserName.setText(user.getuNickName());
                viewHolder.mContent.setText(friendMessage.getmContent());
                viewHolder.mSendTime.setText(friendMessage.getmSendTime());
                viewHolder.mMessageCount.setText(String.valueOf(user.getuFriendMessages().size()));

                view.setTag(viewHolder);
            }
        } else {
            // 有视图
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    class ViewHolder {
        ImageView mImageView;
        TextView mUserName;
        TextView mContent;
        TextView mSendTime;
        TextView mMessageCount;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mMessages.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
