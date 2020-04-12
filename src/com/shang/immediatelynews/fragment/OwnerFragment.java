package com.shang.immediatelynews.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OwnerViewPagerAdapter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@ContentView(R.layout.owner_fragment_layout)
public class OwnerFragment extends BaseFragment {
	
	@ViewInject(R.id.owner_head_tab)
	private TabLayout owner_head_tab;
	@ViewInject(R.id.owner_content_viewpager)
	private ViewPager owner_content_viewpager;
	private List<OwnerTabFragment> tab_fragments;
	private OwnerViewPagerAdapter ownerAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		//设置TabLayout
		setTabLayout();
	}

	private void setTabLayout() {
		//初始化数据
		tab_fragments = new ArrayList<OwnerTabFragment>();
        tab_fragments.add(new OwnerTabFragment("新闻", "0"));
        tab_fragments.add(new OwnerTabFragment("视频", "1"));
        //设置ViewPager的适配器
        ownerAdapter = new OwnerViewPagerAdapter(getChildFragmentManager(),tab_fragments);
        owner_content_viewpager.setAdapter(ownerAdapter);
        //关联ViewPager
        owner_head_tab.setupWithViewPager(owner_content_viewpager); 
        //设置固定的
//      tabLayout.setTabMode(TabLayout.MODE_FIXED);
         //设置滚动的
        owner_head_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
}
