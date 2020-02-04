package com.shang.immediatelynews.fragment;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.VideoAdapter;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;

import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

@ContentView(R.layout.video_fragment_layout)
public class VideoFragment extends BaseFragment {

	@ViewInject(R.id.video_recyclerview)
	private RecyclerView video_recyclerView;
	@ViewInject(R.id.video_recyclerview_refresh)
	private MaterialRefreshLayout video_recyclerview_refresh;
	
	private Handler refresh_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				video_recyclerview_refresh.finishRefresh();
				break;
			case 2:
				//结束上拉刷新
				video_recyclerview_refresh.finishRefreshLoadMore();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		//设置video界面的适配器
		setVideoAdapter();
		//设置刷新监听
		setRefreshListener();
	}

	private void setRefreshListener() {
		video_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
//		video_recyclerview_refresh.autoRefreshLoadMore();//设置自动上拉加载
		video_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
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

	private void setVideoAdapter() {
		VideoAdapter videoAdapter = new VideoAdapter(getActivity());
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		video_recyclerView.setLayoutManager(layoutManager);
		video_recyclerView.setAdapter(videoAdapter);
		video_recyclerView.addItemDecoration(new DividerListItemDecoration(getActivity(), DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		video_recyclerView.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		/*videoAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Toast.makeText(v.getContext(), data.toString(), 0).show();
			}
		});*/
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
}
