package com.shang.immediatelynews.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.TypeViewPagerAdapter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

@ContentView(R.layout.type_fragment_layout)
public class TypeFragment extends BaseFragment {
	
	@ViewInject(R.id.type_head_tab)
	private TabLayout type_head_tab;
	@ViewInject(R.id.type_content_viewpager)
	private ViewPager type_content_viewpager;
	private List<TypeTabFragment> tab_fragments;
	private TypeViewPagerAdapter typeAdapter;
	
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
		tab_fragments = new ArrayList<TypeTabFragment>();
        for (int i=0;i<12;i++){
        	tab_fragments.add(new TypeTabFragment("标题"+i, "内容"+i));
        }
        //设置ViewPager的适配器
        typeAdapter = new TypeViewPagerAdapter(getChildFragmentManager(),tab_fragments);
        type_content_viewpager.setAdapter(typeAdapter);
        //关联ViewPager
        type_head_tab.setupWithViewPager(type_content_viewpager); 
        //设置固定的
//      tabLayout.setTabMode(TabLayout.MODE_FIXED);
         //设置滚动的
        type_head_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
}
