package com.shang.immediatelynews.adapter;

import java.util.List;

import com.shang.immediatelynews.fragment.OrderContentFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderContentAdapter extends FragmentPagerAdapter {

	private final List<OrderContentFragment> fragments;

	public OrderContentAdapter(FragmentManager fm, List<OrderContentFragment> fragments) {
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
