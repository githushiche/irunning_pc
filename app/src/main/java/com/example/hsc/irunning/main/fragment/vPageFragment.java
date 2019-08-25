package com.example.hsc.irunning.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hsc.irunning.R;
import com.example.hsc.irunning.main.activity.LoginActivity;
import com.example.hsc.irunning.main.adapter.vPageAdapter;
import com.example.hsc.irunning.command.utils.ActivityCollectorUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 滑动界面碎片
 *
 * @author Diviner
 * @date 2018-5-25 上午12:09:57
 */
public class vPageFragment extends Fragment {
    private View mView;
    private ViewPager mViewPage;
    private vPageAdapter mViewPageAdapter;
    private List<Fragment> mVpagerList = new ArrayList<Fragment>();// 声明一个保存滑动碎片数量的list

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.vpager_layout, container, false);

        initView();
        return mView;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mViewPage = (ViewPager) mView.findViewById(R.id.id_vpager);

        // 组装推送滑动页面
        mVpagerList.add(new vPage1());
        mVpagerList.add(new vPage2());
        mVpagerList.add(new vPage3());

        mViewPageAdapter = new vPageAdapter(getFragmentManager(), mVpagerList);
        mViewPage.setAdapter(mViewPageAdapter);// 设置适配器
    }

    public static class vPage1 extends Fragment {

        @Override
        @Nullable
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.vpager_fragment1, container,
                    false);

            return view;
        }
    }

    public static class vPage2 extends Fragment {

        @Override
        @Nullable
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.vpager_fragment2, container,
                    false);

            return view;
        }
    }

    public static class vPage3 extends Fragment {
        private View mView;
        private ImageView mImageView;

        @Override
        @Nullable
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.vpager_fragment3, container,
                    false);
            initView();

            return mView;
        }

        private void initView() {
            mImageView = (ImageView) mView.findViewById(R.id.id_vpager3);
            mImageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),
                            LoginActivity.class);
                    startActivity(intent);// 跳转到登陆界面
                    ActivityCollectorUtils.remoActivity(getActivity());
                }
            });
        }
    }
}
