package com.shang.immediatelynews.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.NewsManagerTabAdapter;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OrderAdapter;
import com.shang.immediatelynews.adapter.OrderContentAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.fragment.NewsManagerFragment;
import com.shang.immediatelynews.utils.HttpRequestUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_news_manager)
public class NewsManagerActivity extends AppCompatActivity {

	@ViewInject(R.id.news_manager_tab)
	private TabLayout news_manager_tab;
	@ViewInject(R.id.news_manager_viewpager)
	private ViewPager news_manager_viewpager;
	
	private List<NewsManagerFragment> fragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
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
	
}
