package com.shang.immediatelynews.fragment;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.adapter.NewsContentAdapter;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OrderHeadDialogAdapter;
import com.shang.immediatelynews.adapter.TopContentAdapter;
import com.shang.immediatelynews.customview.OrderHeadDialogFragment;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.NewsContent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@ContentView(R.layout.order_head_content_layout)
public class OrderHeadFragment extends BaseFragment {
	
	@ViewInject(R.id.order_head_recyclerview)
	private RecyclerView order_head_recyclerview;
	private int position;
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		//设置适配器
		setAdapter();
	}
	
    public OrderHeadFragment() {
		super();
	}
    
    public OrderHeadFragment(int position) {
		super();
		this.position = position;
	}
    
	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setAdapter() {
		OrderHeadDialogAdapter adapter = new OrderHeadDialogAdapter(position);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		order_head_recyclerview.setLayoutManager(layoutManager);
		order_head_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(), DividerListItemDecoration.VERTICAL_LIST));
		order_head_recyclerview.setAdapter(adapter);
		//设置动画
		order_head_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("news", (NewsContent)data);
				startActivity(intent);
			}
		});
		order_head_recyclerview.setAdapter(adapter);
	}
}
