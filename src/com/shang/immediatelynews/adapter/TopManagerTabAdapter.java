package com.shang.immediatelynews.adapter;

import java.util.List;

import com.shang.immediatelynews.fragment.NewsManagerFragment;
import com.shang.immediatelynews.fragment.TopManagerFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TopManagerTabAdapter extends FragmentPagerAdapter {
	
	private List<TopManagerFragment> fragments;

	public TopManagerTabAdapter(FragmentManager fm, List<TopManagerFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	/**
     * 返回标题
     * @param position
     * @return
     */
	@Override
	public CharSequence getPageTitle(int position) {
		return fragments.get(position).getTitle();
	}
}
