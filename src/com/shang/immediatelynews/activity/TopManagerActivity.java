package com.shang.immediatelynews.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.TopManagerTabAdapter;
import com.shang.immediatelynews.fragment.TopManagerFragment;
import com.shang.immediatelynews.utils.ActivityUtils;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

@ContentView(R.layout.activity_top_manager)
public class TopManagerActivity extends BaseActivity {

	@ViewInject(R.id.top_manager_tab)
	private TabLayout top_manager_tab;
	@ViewInject(R.id.top_manager_viewpager)
	private ViewPager top_manager_viewpager;
	
	private List<TopManagerFragment> fragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		ActivityUtils.addActivities(this);
		 //初始化数据
        fragments = new ArrayList<TopManagerFragment>();
        fragments.add(new TopManagerFragment("未处理", "0"));
        fragments.add(new TopManagerFragment("已处理", "1"));
        fragments.add(new TopManagerFragment("已退回", "2"));
        //设置ViewPager的适配器
        TopManagerTabAdapter adapter = new TopManagerTabAdapter(getSupportFragmentManager(),fragments);
        top_manager_viewpager.setAdapter(adapter);
        //关联ViewPager
        top_manager_tab.setupWithViewPager(top_manager_viewpager);
        //设置固定的
//      tabLayout.setTabMode(TabLayout.MODE_FIXED);
         //设置滚动的
        top_manager_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
	}
	
	public void cancel(View v) {
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
