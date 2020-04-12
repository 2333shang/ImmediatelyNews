package com.shang.immediatelynews.fragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TopContentAdapter;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Top;
import com.shang.immediatelynews.loader.GlideImageLoader;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
	
	private Map<String, List<Top>> tops;
	private TopContentAdapter topContentAdapter;
	
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
				topContentAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		String object = (String) this.getArguments().get("topNews");
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
		Type type = new TypeToken<HashMap<String, List<Top>>>(){}.getType();
		Log.d("news", object);
		tops = gson.fromJson(object, type);
		//设置Banner轮播
		setBanner(tops.get("1"));
		//设置RecyclerView的适配器
		top_recyclerView.setFocusable(false);
		setRecyclerAdapter(tops.get("0"));
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
				getMoreData(tops.get("0").get(tops.get("0").size()-1));
	       }
		});
	}

	private void setRecyclerAdapter(List<Top> list) {
		topContentAdapter = new TopContentAdapter(getActivity(), list);
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
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("tops", (Top)data);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.video_content_detail_right_in, R.anim.video_content_detail_alpha_dismiss);
			}
		});
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setBanner(List<Top> list) {
		images = new ArrayList<String>();
		titles = new ArrayList<String>();
		for(Top top:list) {
			List<Attachment> pics = top.getContent().getPics();
			for(Attachment a:pics) {
				if(a.getAttachmentType().equals("2")) {
					images.add("http://192.168.0.105:8080/news/file" + a.getUrl());
					Log.d("news", "http://192.168.0.105:8080/news/file" + a.getUrl());
					break;
				}
			}
			titles.add(top.getContent().getTitle());
		}
		
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
	
	private void getMoreData(Top top) {
		HttpRequestUtils.getRequest("http://116.62.234.70:8080/news/top/addmore?topId=" + 12, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String addmore = response.body().string();
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<List<Top>>(){}.getType();
				tops.get("0").addAll((List<Top>)gson.fromJson(addmore, type));
				refresh_handler.sendEmptyMessage(2);
			}
			
			@Override
			public void onFailure(Call call, IOException response) {
				
			}
		});
	}
}
