package com.shang.immediatelynews.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OrderContentAdapter;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.fragment.OrderContentFragment;
import com.shang.immediatelynews.utils.ActivityUtils;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

@ContentView(R.layout.activity_order_cotent)
public class OrderCotentActivity extends BaseActivity {

	@ViewInject(R.id.order_content_tab)
	private TabLayout order_content_tab;
	@ViewInject(R.id.order_content_viewpager)
	private ViewPager order_content_viewpager;
	
	private List<OrderContentFragment> fragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		ActivityUtils.addActivities(this);
		Order order = (Order) getIntent().getSerializableExtra("orders");
		 //初始化数据
        fragments = new ArrayList<OrderContentFragment>();
        fragments.add(new OrderContentFragment("新闻", order.getOrderCompany()));
        fragments.add(new OrderContentFragment("视频", order.getOrderCompany()));
        //设置ViewPager的适配器
        OrderContentAdapter adapter = new OrderContentAdapter(getSupportFragmentManager(),fragments);
        order_content_viewpager.setAdapter(adapter);
        //关联ViewPager
        order_content_tab.setupWithViewPager(order_content_viewpager);
        //设置固定的
//      tabLayout.setTabMode(TabLayout.MODE_FIXED);
         //设置滚动的
        order_content_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
