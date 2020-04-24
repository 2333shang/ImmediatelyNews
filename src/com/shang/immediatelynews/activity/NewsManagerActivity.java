package com.shang.immediatelynews.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.MainActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.NewsManagerTabAdapter;
import com.shang.immediatelynews.fragment.NewsManagerFragment;
import com.shang.immediatelynews.utils.ActivityUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

@ContentView(R.layout.activity_news_manager)
public class NewsManagerActivity extends BaseActivity {

	@ViewInject(R.id.news_manager_tab)
	private TabLayout news_manager_tab;
	@ViewInject(R.id.news_manager_viewpager)
	private ViewPager news_manager_viewpager;
	
	private List<NewsManagerFragment> fragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		ActivityUtils.addActivities(this);
		 //初始化数据
        fragments = new ArrayList<NewsManagerFragment>();
        fragments.add(new NewsManagerFragment("本日新闻", "0"));
        fragments.add(new NewsManagerFragment("本日视频", "1"));
        fragments.add(new NewsManagerFragment("历史新闻", "2"));
        fragments.add(new NewsManagerFragment("历史视频", "3"));
        //设置ViewPager的适配器
        NewsManagerTabAdapter adapter = new NewsManagerTabAdapter(getSupportFragmentManager(),fragments);
        news_manager_viewpager.setAdapter(adapter);
        //关联ViewPager
        news_manager_tab.setupWithViewPager(news_manager_viewpager);
        //设置固定的
//      tabLayout.setTabMode(TabLayout.MODE_FIXED);
         //设置滚动的
        news_manager_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
	}
	
	public void cancel(View v) {
		finish();
	}
	
//	public void totop(View v) {
//		Intent intent = new Intent(this, TopManagerActivity.class);
//		startActivity(intent);
//	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
