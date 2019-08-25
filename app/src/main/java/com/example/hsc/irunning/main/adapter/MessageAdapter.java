package com.example.hsc.irunning.main.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;
import com.example.hsc.irunning.baidumap.utils.MapLoadUtils;
import com.example.hsc.irunning.command.utils.LogMsgUtil;
import com.example.hsc.irunning.main.activity.MainActivity;
import com.example.hsc.irunning.main.bean.FriendMessage;
import com.example.hsc.irunning.main.bean.User;
import com.example.hsc.irunning.main.http.HttpUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 聊天界面消息适配器
 *
 * @author Diviner
 * @date 2018-5-5 下午9:22:19
 */
public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLinearLayout;// 总布局
    //    private ArrayList<FriendMessage> mFriendMsgs;
    private List<User> mFriendMessages;

    private boolean isShowPhoto = false;

    /**
     * 适配器的构造方法
     *
     * @param context
     * @param friendMessages
     */
    public MessageAdapter(Context context, List<User> friendMessages) {
        mContext = context;
        mLinearLayout = LayoutInflater.from(mContext);
        mFriendMessages = friendMessages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 类型可以分为3类,0消息、1图片、2地图
        User user = mFriendMessages.get(position);
        List<FriendMessage> friendMessages = user.getuFriendMessages();

        for (final FriendMessage bl : friendMessages) {
            int lrtype = bl.getmResultType();// 根据类型来判断是发送者还是接受者
            int type = bl.getmMessageType();// 获取到消息类型
            if (bl != null) {
                if (convertView == null) {// 说明没有视图
                    if (lrtype == 0) {// 等于0是发送者
                        switch (type) {
                            case 0:// 普通消息
                                convertView = mLinearLayout.inflate(
                                        R.layout.fragment_message_list_right, null);

                                ProgressBar pb = (ProgressBar) convertView
                                        .findViewById(R.id.id_pb_right_progress);
                                pb.setVisibility(View.GONE);

                                ImageView mIvImager = (ImageView) convertView
                                        .findViewById(R.id.id_iv_right_message_picture);
                                Glide.with(mContext)//
                                        .load(R.mipmap.fimg)//
                                        .override(40, 40)//
                                        .into(mIvImager);// 加载图片

                                // 发送内容
                                TextView mTextView = (TextView) convertView
                                        .findViewById(R.id.id_message_right_content);
                                mTextView.setText(bl.getmContent());
                                break;

                            case 1:// 图片消息
                                convertView = mLinearLayout.inflate(
                                        R.layout.fragment_message_right_img, null);

                                pb = (ProgressBar) convertView
                                        .findViewById(R.id.id_message_right_img_progress);
                                pb.setVisibility(View.GONE);

                                ImageView userImg = (ImageView) convertView
                                        .findViewById(R.id.id_iv_right_img_picture);
                                Glide.with(mContext)//
                                        .load(R.mipmap.fimg)//
                                        .override(40, 40)//
                                        .into(userImg);// 加载图片

                                ImageView ivMessage = (ImageView) convertView
                                        .findViewById(R.id.id_iv_right_message_content);
                                Glide.with(mContext)//
                                        .load(bl.getmContent())//
                                        .override(150, 150)//
                                        .into(ivMessage);// 加载图片
                                break;

                            case 2:// 位置分享消息
                                convertView = mLinearLayout.inflate(
                                        R.layout.fragment_message_right_map, null);

                                MapView mapView = (MapView) convertView
                                        .findViewById(R.id.id_share_mapview);

                                String resultContent = bl.getmContent();
                                if (!resultContent.equals("")) {
                                    String str[] = resultContent.split(":");


                                    LatLng ll = new LatLng(MainActivity.sUser.getuUserInfo().getuLatitude(),
                                            MainActivity.sUser.getuUserInfo().getuLongitude());
                                    MapLoadUtils.DisplayMap(mapView, ll, user.getuNickName());// 初始化地图

                                    LogMsgUtil.Log_D("MapFragment",
                                            "位置分享串" + ll.toString());
                                }
                                break;
                        }
                    } else {
                        switch (type) {// 根据type来判断消息类型
                            case 0:// 文字
                                convertView = mLinearLayout.inflate(
                                        R.layout.fragment_message_list_left, null);

                                ImageView uimg = (ImageView) convertView
                                        .findViewById(R.id.id_iv_left_message_picture);
                                Glide.with(mContext)//
                                        .load(R.mipmap.fimg)//
                                        .override(40, 40)//
                                        .into(uimg);// 加载聊天头像

                                // 设置消息内容
                                TextView content = (TextView) convertView
                                        .findViewById(R.id.id_mes_left_msg);
                                content.setText(bl.getmContent());// 设置聊天内容
                                break;

                            case 1:// 图片
                                convertView = mLinearLayout.inflate(
                                        R.layout.fragment_message_left_img, null);

                                ImageView pick = (ImageView) convertView
                                        .findViewById(R.id.id_iv_left_message_photo);
                                Glide.with(mContext)//
                                        .load(R.mipmap.fimg)//
                                        .override(40, 40)//
                                        .into(pick); // 设置聊天头像

                                try {
                                    HttpUtils.downloadMessageImage(String.valueOf(user.getuId()), String.valueOf(bl.getmReceiverUserId()),
                                            bl.getmContent(), new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {

                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    if (response.code() == 200) {
                                                        byte[] Picture_bt = response.body().bytes();

                                                        String imgPath[] = bl.getmContent().split("\\/");

                                                        OutputStream outputStream = new FileOutputStream(Environment.getExternalStorageDirectory()
                                                                + "/" + imgPath[imgPath.length - 1]);
                                                        outputStream.write(Picture_bt);
                                                        outputStream.flush();
                                                        LogMsgUtil.Log_D("MessageAdapter", "照片已经存入本地图库");

                                                        isShowPhoto = true;
                                                    }
                                                }
                                            });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ImageView messageImg = (ImageView) convertView.findViewById(R.id.id_iv_left_message_img);
                                String imgPath[] = bl.getmContent().split("\\/");

                                LogMsgUtil.Log_D("MessageAdapter","照片显示地址" + Environment.getExternalStorageDirectory()
                                        + "/" + imgPath[imgPath.length - 1]);

                                Glide.with(mContext)//
                                        .load(Environment.getExternalStorageDirectory()
                                                + "/" + imgPath[imgPath.length - 1])//
                                        .override(150, 150)//
                                        .into(messageImg); // 设置聊天头像
                                break;

                            case 2:// 地图
                                convertView = mLinearLayout.inflate(
                                        R.layout.fragment_message_left_map, null);
                                MapView mv = (MapView) convertView
                                        .findViewById(R.id.id_msg_left_mapview);

                                ImageView mapuimg = (ImageView) convertView
                                        .findViewById(R.id.iv_head_picture);
                                mapuimg.setImageResource(R.mipmap.fimg);// 设置聊天头像

                                String resultContent = bl.getmContent();
                                if (!resultContent.equals("")) {
                                    String str[] = resultContent.split(":");

                                    LogMsgUtil.Log_D("MapFragment", "位置分享串"
                                            + resultContent);

                                    LatLng ll = new LatLng(MainActivity.sUser.getuUserInfo().getuLatitude(),
                                            MainActivity.sUser.getuUserInfo().getuLongitude());
                                    MapLoadUtils.DisplayMap(mv, ll, user.getuNickName());// 初始化地图

                                }
                                break;
                        }
                    }
                }
            }
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mFriendMessages.get(position);
    }

    @Override
    public int getCount() {
        return mFriendMessages.size();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

}
