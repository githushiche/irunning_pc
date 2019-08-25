package com.example.hsc.irunning.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;
import com.example.hsc.irunning.main.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Diviner on 2019/4/14.
 * Vesion:1.0
 */
public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "VideoActivity";

    private RecyclerView mRecyclerView;
    private List<String> mUris;
    private JZVideoPlayerStandard mJzVideoPlayerStandard;
    private VideoAdapter mVideoAdapter;
    private ImageView mIvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_layout);
        ActivityCollectorUtils.addActivity(this);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_rv_videos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);// 以什么方式来水平还是居中,一行几列
        mRecyclerView.setLayoutManager(layoutManager);

        mUris = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mUris.add("http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4");
        }

        mVideoAdapter = new VideoAdapter(mUris);
        mRecyclerView.setAdapter(mVideoAdapter);

        mIvBack = (ImageView) findViewById(R.id.id_iv_video_back);
        mIvBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_video_back:
                ActivityCollectorUtils.remoActivity(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mJzVideoPlayerStandard.backPress()) {
            ActivityCollectorUtils.remoActivity(this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJzVideoPlayerStandard.releaseAllVideos();
    }

}
