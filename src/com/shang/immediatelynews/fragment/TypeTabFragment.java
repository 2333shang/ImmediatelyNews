package com.shang.immediatelynews.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsCommentReplyActivity;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TypeContentAdapter;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.NewsComments;
import com.shang.immediatelynews.entities.NewsContent;

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

@ContentView(R.layout.type_tab_content_layout)
public class TypeTabFragment extends BaseFragment{

	@ViewInject(R.id.type_content_recyclerview)
	private RecyclerView type_content_recyclerview;
	@ViewInject(R.id.type_content_recyclerview_refresh)
	private MaterialRefreshLayout type_content_recyclerview_refresh;
	
	private String title;
	private String content;
	private List<String> contents = new ArrayList<String>();
	
	private Handler refresh_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				type_content_recyclerview_refresh.finishRefresh();
				break;
			case 2:
				//结束上拉刷新
				type_content_recyclerview_refresh.finishRefreshLoadMore();
				break;
			default:
				break;
			}
		};
	};
	
	public TypeTabFragment() {
		super();
	}

	public TypeTabFragment(String title, String content) {
		this.title = title;
		this.content = content;
		for(int i =0; i<12 ; i++) {
			contents.add(content);
		}
	}
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		//设置适配器
		setRecyclerAdapter();
		//设置刷新监听
		setRefreshListener();
	}

	private void setRefreshListener() {
		type_content_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
//		video_recyclerview_refresh.autoRefreshLoadMore();//设置自动上拉加载
		type_content_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
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
		TypeContentAdapter typeContentAdapter = new TypeContentAdapter();
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		type_content_recyclerview.setLayoutManager(layoutManager);
		type_content_recyclerview.setAdapter(typeContentAdapter);
		type_content_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		type_content_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		typeContentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("news", (NewsContent)data);
				startActivity(intent);
			}
		});
		type_content_recyclerview.setAdapter(typeContentAdapter);
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
