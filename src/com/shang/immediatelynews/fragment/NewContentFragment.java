package com.shang.immediatelynews.fragment;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@ContentView(R.layout.type_fragment_layout)
public class NewContentFragment extends BaseFragment {
	
	@ViewInject(R.id.type_head_recyclerview)
	private RecyclerView type_head_recyclerView;
//	@ViewInject(R.id.type_content_recyclerview)
//	private RecyclerView type_content_recyclerview;
	
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
//		TypeContentAdapter contentAdapter = new TypeContentAdapter();
//		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		type_content_recyclerview.setLayoutManager(layoutManager);
//		type_content_recyclerview.setAdapter(contentAdapter);
//		type_content_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
//		//设置动画
//		type_content_recyclerview.setItemAnimator(new DefaultItemAnimator());
//		//设置点击事件
//		contentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
//			
//			@Override
//			public void onItemClick(View v, int position, Object data) {
//				Toast.makeText(v.getContext(), data.toString(), 0).show();
//			}
//		});
	}
	
	private void setHeadAdapter() {
//		TypeOrderHeadAdapter orderAdapter = new TypeOrderHeadAdapter();
//		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//		type_head_recyclerView.setLayoutManager(layoutManager);
//		type_head_recyclerView.setAdapter(orderAdapter);
//		//设置动画
//		type_head_recyclerView.setItemAnimator(new DefaultItemAnimator());
//		//设置点击事件
//		orderAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
//			
//			@Override
//			public void onItemClick(View v, int position, Object data) {
//				Toast.makeText(v.getContext(), data.toString(), 0).show();
//			}
//		});
	}
}
