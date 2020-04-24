package com.shang.immediatelynews.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.MainActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsVideoActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.VideoAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.ProgressDialog;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.video_fragment_layout)
public class VideoFragment extends BaseFragment {

	@ViewInject(R.id.video_recyclerview)
	private RecyclerView video_recyclerView;
	@ViewInject(R.id.video_recyclerview_refresh)
	private MaterialRefreshLayout video_recyclerview_refresh;
	
	private List<Content> videos = new ArrayList<Content>();
	private VideoAdapter videoAdapter;
	
	private Handler refresh_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				video_recyclerview_refresh.finishRefresh();
				videoAdapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				video_recyclerview_refresh.finishRefreshLoadMore();
				videoAdapter.notifyDataSetChanged();
				break;
			case 3:
				video_recyclerview_refresh.finishRefresh();
				video_recyclerview_refresh.finishRefreshLoadMore();
				break;
			case 4:
				//设置video界面的适配器
				setVideoAdapter();
				//设置刷新监听
				setRefreshListener();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		getVideoContent();
	}

	private void setRefreshListener() {
//		video_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
		video_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				getNewsData();
			}
			
			@Override
	       public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				getMoreData();
	       }
		});
	}

	protected void getMoreData() {
		String newsId = videos.get(videos.size() - 1).getId();
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/morevideo?newsId=" + newsId;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Content>>(){}, videos, response.body().string(), refresh_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	protected void getNewsData() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/video";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Content>>(){}, videos, response.body().string(), refresh_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	private void setVideoAdapter() {
		videoAdapter = new VideoAdapter(getActivity(), videos);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		video_recyclerView.setLayoutManager(layoutManager);
		video_recyclerView.setAdapter(videoAdapter);
		video_recyclerView.addItemDecoration(new DividerListItemDecoration(getActivity(), DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		video_recyclerView.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		videoAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsVideoActivity.class);
				intent.putExtra("video", (Content)data);
				startActivity(intent);
			}
		});
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}

	public void getVideoContent() {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(getActivity(), "视频加载中！！！");
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/video", new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					videos.addAll(GsonUtils.getGsonWithLocalDate(new TypeToken<List<Content>>(){}, object));
					refresh_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException response) {
				NetworkUtils.dismissLoading2(showLoading2);
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}
}
