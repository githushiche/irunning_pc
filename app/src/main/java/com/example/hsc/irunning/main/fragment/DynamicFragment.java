package com.example.hsc.irunning.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.main.dialog.DynamicDialog;

/**
 * 动态碎片
 *
 * @author Diviner
 * @date 2018-5-1 下午9:58:57
 */
public class DynamicFragment extends Fragment implements OnClickListener {
    private View mView;
    private RelativeLayout mRlDynamic;
    private RelativeLayout mRlNearFriend;// 附近好友

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dynamic_layout, container, false);

        init();

        return mView;
    }

    private void init() {
        mRlDynamic = (RelativeLayout) mView
                .findViewById(R.id.id_dynamic_layout);
        mRlDynamic.setOnClickListener(this);

        mRlNearFriend = (RelativeLayout) mView
                .findViewById(R.id.id_near_friend);
        mRlNearFriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_dynamic_layout:// 动态按钮点击事件
                new DynamicDialog(getActivity()).show();// 弹出动态页
                break;

            case R.id.id_near_friend:
                // 附近的好友逻辑
                break;
        }
    }
}
