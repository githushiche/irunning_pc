package com.example.hsc.irunning.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 滑动界面适配器
 * Created by Diviner
 * Time:2018/12/11.
 * Version:1.0
 */
public class vPageAdapter extends FragmentPagerAdapter{

	private List<Fragment> fragments;

	public vPageAdapter(FragmentManager fm) {
		super(fm);
	}

	public vPageAdapter(FragmentManager fm, List<Fragment> fragment) {
		super(fm);
		this.fragments = fragment;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
