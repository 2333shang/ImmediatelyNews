package com.shang.immediatelynews.adapter;

import java.util.List;

import com.shang.immediatelynews.fragment.OrderHeadFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class OrderHeadViewpagerAdapter extends FragmentPagerAdapter {
	
	private List<OrderHeadFragment> orderFragments;

	public OrderHeadViewpagerAdapter(FragmentManager fm, List<OrderHeadFragment> orderFragments) {
		super(fm);
		this.orderFragments = orderFragments;
	}

	@Override
	public Fragment getItem(int position) {
		return orderFragments.get(position);
	}

	@Override
	public int getCount() {
		return orderFragments.size();
	}

}
