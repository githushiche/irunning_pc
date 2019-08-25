package com.example.hsc.irunning.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.hsc.irunning.R;

import java.io.Serializable;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Diviner on 2019/4/14.
 * Vesion:1.0
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private final String TAG = "VideoAdapter";
    private Context mContext;
    private List<String> mUris;

    public VideoAdapter(List<String> uris) {
        mUris = uris;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements Serializable {
        private View mVideoView;
        private JZVideoPlayerStandard mJzVideoPlayerStandard;

        public ViewHolder(View itemView) {
            super(itemView);
            mVideoView = itemView;

            mJzVideoPlayerStandard = (JZVideoPlayerStandard) itemView.findViewById(R.id.id_jzvps_video_item_play);
        }
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_video_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        //        holder.mVideoView.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                LogMsgUtil.Log_D(TAG, "点击了" + holder.getAdapterPosition() + "视频的整个页面");
        //            }
        //        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String uri = mUris.get(position);

        holder.mJzVideoPlayerStandard.setUp(uri,
                JZVideoPlayer.SCREEN_LAYOUT_LIST, "");
        Glide.with(mContext).load("http://a4.att.hudong.com/05/71/01300000057455120185716259013.jpg")
                .into(holder.mJzVideoPlayerStandard.thumbImageView);
    }

    @Override
    public int getItemCount() {
        return mUris.size();
    }
}
