package com.shang.immediatelynews.adapter;

import java.util.List;

import com.shang.immediatelynews.fragment.OwnerTabFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OwnerViewPagerAdapter extends FragmentPagerAdapter {

	private List<OwnerTabFragment> tab_fragments;
	
	public OwnerViewPagerAdapter(FragmentManager fm, List<OwnerTabFragment> tab_fragments) {
		super(fm);
		this.tab_fragments = tab_fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return tab_fragments.get(position);
	}

	@Override
	public int getCount() {
		return tab_fragments.size();
	}
	
	/**
     * 返回标题
     * @param position
     * @return
     */
	@Override
	public CharSequence getPageTitle(int position) {
		return tab_fragments.get(position).getTitle();
	}
}
