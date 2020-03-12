package com.shang.immediatelynews.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.activity.NewsContentActivity2;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TopContentAdapter;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.NewsContent;
import com.shang.immediatelynews.loader.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

@ContentView(R.layout.top_fragment_layout)
public class TopFragment extends BaseFragment {
	
	/**banner图片集合*/
	private List<String> images;
	/**banner图片标题集合*/
	private List<String> titles;
	@ViewInject(R.id.banner)
	private Banner banner;
	@ViewInject(R.id.top_recyclerview)
	private RecyclerView top_recyclerView;
	@ViewInject(R.id.top_recyclerview_refresh)
	private MaterialRefreshLayout top_recyclerview_refresh;
	private Activity actvity;
	
	private Handler refresh_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				top_recyclerview_refresh.finishRefresh();
				break;
			case 2:
				//结束上拉刷新
				top_recyclerview_refresh.finishRefreshLoadMore();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		//设置Banner轮播
		setBanner();
		//设置RecyclerView的适配器
		setRecyclerAdapter();
		//设置刷新监听
		setRefreshListener();
	}

	private void setRefreshListener() {
		top_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
//		video_recyclerview_refresh.autoRefreshLoadMore();//设置自动上拉加载
		top_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				Toast.makeText(getActivity(), "下拉刷新", 0).show();
				refresh_handler.sendEmptyMessageDelayed(1, 2000);
			}
			
			@Override
	       public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				Toast.makeText(getActivity(), "上拉刷新", 0).show();
				refresh_handler.sendEmptyMessageDelayed(2, 2000);
	       }
		});
	}

	private void setRecyclerAdapter() {
		TopContentAdapter topContentAdapter = new TopContentAdapter();
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		top_recyclerView.setLayoutManager(layoutManager);
		top_recyclerView.setAdapter(topContentAdapter);
		top_recyclerView.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		top_recyclerView.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		topContentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsContentActivity2.class);
				intent.putExtra("news", (NewsContent)data);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.video_content_detail_right_in, R.anim.video_content_detail_alpha_dismiss);
			}
		});
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setBanner() {
		images = new ArrayList<String>();
		Log.d("TAG", Environment.getExternalStorageDirectory() + "/first.jpg");
		images.add(Environment.getExternalStorageDirectory() + "/first.jpg");
		images.add(Environment.getExternalStorageDirectory() + "/first.jpg");
		images.add(Environment.getExternalStorageDirectory() + "/first.jpg");
		images.add(Environment.getExternalStorageDirectory() + "/first.jpg");
		images.add(Environment.getExternalStorageDirectory() + "/first.jpg");
		images.add(Environment.getExternalStorageDirectory() + "/first.jpg");
		images.add(Environment.getExternalStorageDirectory() + "/first.jpg");

		titles = new ArrayList<String>();
		titles.add("picture01");
		titles.add("picture02");
		titles.add("picture03");
		titles.add("picture04");
		titles.add("picture05");
		titles.add("picture06");
		titles.add("picture07");
		
		//设置banner样式
		banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
		//设置图片加载器
		banner.setImageLoader(new GlideImageLoader());
		//设置图片集合
		banner.setImages(images);
		//设置banner动画效果
		banner.setBannerAnimation(Transformer.Tablet);
		//设置标题集合（当banner样式有显示title时）
		banner.setBannerTitles(titles);
		//设置自动轮播，默认为true
		banner.isAutoPlay(true);
		//设置轮播时间
		banner.setDelayTime(3000);
		//设置指示器位置（当banner模式中有指示器时）
		banner.setIndicatorGravity(BannerConfig.CENTER);
		//banner设置方法全部调用完毕时最后调用
		banner.start();
		//设置点击事件，下标是从1开始
		banner.setOnBannerClickListener(new OnBannerClickListener() {
			
			@Override
			public void OnBannerClick(int position) {
				Toast.makeText(getActivity(), "hello" + position, Toast.LENGTH_LONG).show();
			}
		});
	}
}
