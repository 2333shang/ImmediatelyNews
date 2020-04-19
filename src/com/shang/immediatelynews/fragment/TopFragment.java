package com.shang.immediatelynews.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.MainActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.activity.NewsVideoActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TopContentAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Top;
import com.shang.immediatelynews.loader.GlideImageLoader;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
				topContentAdapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				top_recyclerview_refresh.finishRefreshLoadMore();
				topContentAdapter.notifyDataSetChanged();
				break;
			case 3:
				top_recyclerview_refresh.finishRefreshLoadMore();
				top_recyclerview_refresh.finishRefresh();
			case 4:
				//设置Banner轮播
				setBanner(tops.get("1"));
				//设置RecyclerView的适配器
				top_recyclerView.setFocusable(false);
				setRecyclerAdapter(tops.get("0"));
				//设置刷新监听
				setRefreshListener();
			default:
				break;
			}
		};
	};
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		getTopNews();
	}

	private void setRefreshListener() {
//		top_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
//		video_recyclerview_refresh.autoRefreshLoadMore();//设置自动上拉加载
		top_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				getNewData();
			}
			
			@Override
	        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
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
		top_recyclerView.setFocusable(false);
		top_recyclerView.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		top_recyclerView.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		topContentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = null;
				Top top = (Top) data;
				if("1".equals(top.getContent().getNewsType())) {
					intent = new Intent(getActivity(), NewsVideoActivity.class);
					intent.putExtra("video", top.getContent());
				}else {
					intent = new Intent(getActivity(), NewsContentActivity.class);
					intent.putExtra("tops", (Top)data);
				}
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.video_content_detail_right_in, R.anim.video_content_detail_alpha_dismiss);
			}
		});
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setBanner(final List<Top> list) {
		images = new ArrayList<String>();
		titles = new ArrayList<String>();
		for(Top top:list) {
			List<Attachment> pics = top.getContent().getPics();
			if(pics != null && !pics.isEmpty()) {
				images.add(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + pics.get(0).getUrl());
			}else {
				//从网络中下载该图片到本地再加载
				images.add(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + "/news.jpg");
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
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("tops", list.get(position-1));
				getActivity().startActivity(intent);
			}
		});
	}
	
	private void getTopNews() {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(getActivity(), "数据加载中！");
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/seltop";
		HttpRequestUtils.getRequest(url, new Callback() {

			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.dismissLoading2(showLoading2);
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					tops = new HashMap<String, List<Top>>();
					tops.putAll(GsonUtils.getGsonWithLocalDate(new TypeToken<Map<String, List<Top>>>(){}, object));
					refresh_handler.sendEmptyMessage(4);
				}
			}
			
		});
	}
	
	private void getNewData() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/seltop";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					Map<String, List<Top>> data = GsonUtils.getGsonWithLocalDate(new TypeToken<Map<String, List<Top>>>(){}, object);
					List<Top> list = data.get("0");
					Top top_current_new = tops.get("0").get(0);
					//当前最新新闻比查询得到的新闻时间都要早
					if(!top_current_new.getId().equals(list.get(0).getId())) {
						tops.get("0").clear();
						tops.get("0").addAll(list);
						refresh_handler.sendEmptyMessage(1);
					}else {
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(getActivity(), "没有更新的新闻了！", 0).show();
								refresh_handler.sendEmptyMessage(3);
							}
						});
					}
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
				refresh_handler.sendEmptyMessage(3);
			}
		});
	}
	
	private void getMoreData(Top top) {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/addmore?topId=" + top.getId();
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
//				String addmore = response.body().string();
//				if("login_invalid".equals(addmore)) {
//					NetworkUtils.toSessionInvalid(getActivity());
//				}else {
//					List<Top> data = GsonUtils.getGsonWithLocalDate(new TypeToken<List<Top>>(){}, addmore);
//					if(data == null || data.isEmpty()) {
//						getActivity().runOnUiThread(new Runnable() {
//							
//							@Override
//							public void run() {
//								Toast.makeText(getActivity(), "没有更多的新闻了！", 0).show();
//								refresh_handler.sendEmptyMessage(3);
//							}
//						});
//					}else {
//						tops.get("0").addAll(data);
//						refresh_handler.sendEmptyMessage(2);
//					}
//				}
				NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Top>>(){}, tops.get("0"), response.body().string(), refresh_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
				refresh_handler.sendEmptyMessage(3);
			}
		});
	}
}
