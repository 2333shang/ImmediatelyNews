package com.shang.immediatelynews.fragment;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OrderContentAdapter;
import com.shang.immediatelynews.adapter.OrderHeadAdapter;
import com.shang.immediatelynews.customview.OrderHeadDialogFragment;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.NewsContent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

@ContentView(R.layout.order_fragment_layout)
public class OrderFragment extends BaseFragment {
	
	@ViewInject(R.id.order_head_recyclerview)
	private RecyclerView order_head_recyclerView;
	@ViewInject(R.id.order_content_recyclerview)
	private RecyclerView order_content_recyclerview;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		//设置头部适配器
		setHeadAdapter();
		///设置内容适配器
		setContentAdapter();
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setContentAdapter() {
		OrderContentAdapter contentAdapter = new OrderContentAdapter();
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		order_content_recyclerview.setLayoutManager(layoutManager);
		order_content_recyclerview.setAdapter(contentAdapter);
		order_content_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		order_content_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		contentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("news", (NewsContent)data);
				startActivity(intent);
			}
		});
	}
	
	private void setHeadAdapter() {
		OrderHeadAdapter orderAdapter = new OrderHeadAdapter();
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		order_head_recyclerView.setLayoutManager(layoutManager);
		order_head_recyclerView.setAdapter(orderAdapter);
		//设置动画
		order_head_recyclerView.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		orderAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				new OrderHeadDialogFragment(position).show(getChildFragmentManager(), null);
			}
		});
	}
}
