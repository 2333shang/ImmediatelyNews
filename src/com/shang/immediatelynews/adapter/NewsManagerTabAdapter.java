package com.shang.immediatelynews.adapter;

import java.util.List;

import com.shang.immediatelynews.fragment.NewsManagerFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NewsManagerTabAdapter extends FragmentPagerAdapter {
	
	private List<NewsManagerFragment> fragments;

	public NewsManagerTabAdapter(FragmentManager fm, List<NewsManagerFragment> fragments) {
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
