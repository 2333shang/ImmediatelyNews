package com.shang.immediatelynews.fragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.MainActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.CompanyActivity;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TypeContentAdapter;
import com.shang.immediatelynews.adapter.TypeOrderHeadAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.customview.OrderHeadDialogFragment;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

@ContentView(R.layout.type_fragment_layout)
public class TypeCompanyFragment extends BaseFragment {
	
	@ViewInject(R.id.type_head_recyclerview)
	private RecyclerView type_head_recyclerView;
	@ViewInject(R.id.type_recyclerview_refresh)
	private MaterialRefreshLayout type_recyclerview_refresh;
	@ViewInject(R.id.type_content_recyclerview)
	private RecyclerView type_content_recyclerview;
	
	private List<Order> orders = new ArrayList<Order>();
	private List<Content> contents = new ArrayList<Content>();
	private TypeContentAdapter contentAdapter;
	private TypeOrderHeadAdapter orderAdapter;
	
	private Handler type_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//结束下拉刷新
				type_recyclerview_refresh.finishRefresh();
				contentAdapter.notifyDataSetChanged();
				orderAdapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				type_recyclerview_refresh.finishRefreshLoadMore();
				contentAdapter.notifyDataSetChanged();
				orderAdapter.notifyDataSetChanged();
				break;
			case 3:
				type_recyclerview_refresh.finishRefresh();
				type_recyclerview_refresh.finishRefreshLoadMore();
				break;
			case 4:
				//设置头部适配器
				setHeadAdapter(orders);
				break;
			case 5:
				///设置内容适配器
				setContent(contents);
				setRefreshListener();
				break;
			case 6:
				orderAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		getOrderContent();
		getCompanyContent();
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setRefreshListener() {
		type_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				getNewData();
			}
			
			@Override
	        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				getMoreData();
	       }
		});
	}
	
	protected void getNewData() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/content";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Content>>(){}, contents, response.body().string(), type_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
				type_handler.sendEmptyMessage(3);
			}
		});
		url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/orders";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				orders.clear();
				orders.addAll(GsonUtils.getGsonWithLocalDate(new TypeToken<List<Order>>(){}, response.body().string()));
				type_handler.sendEmptyMessage(6);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
				type_handler.sendEmptyMessage(3);
			}
		});
	}

	protected void getMoreData() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/morecontent";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Content>>(){}, contents, response.body().string(), type_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
				type_handler.sendEmptyMessage(3);
			}
		});
	}

	private void setContent(List<Content> rand) {
		contentAdapter = new TypeContentAdapter(getActivity(), contents);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		type_content_recyclerview.setLayoutManager(layoutManager);
		type_content_recyclerview.setAdapter(contentAdapter);
		type_content_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		type_content_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		contentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("news", (Content)data);
				getActivity().startActivity(intent);
			}
		});
	}
	
	private void setHeadAdapter(List<Order> order) {
		orderAdapter = new TypeOrderHeadAdapter(getActivity(), order);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		type_head_recyclerView.setLayoutManager(layoutManager);
		type_head_recyclerView.setAdapter(orderAdapter);
		//设置动画
		type_head_recyclerView.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		orderAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				if(position != orders.size()) {
					new OrderHeadDialogFragment(orders, position).show(getChildFragmentManager(), null);
				}else {
					Intent intent = new Intent(getActivity(), CompanyActivity.class);
					getActivity().startActivity(intent);
				}
			}
		});
	}
	
	public void getOrderContent() {
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/orders", new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					orders.addAll(GsonUtils.getGsonWithLocalDate(new TypeToken<List<Order>>(){}, object));
					type_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}
	
	public void getCompanyContent() {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(getActivity(), "数据加载中！");
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/content", new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					contents.addAll(GsonUtils.getGsonWithLocalDate(new TypeToken<List<Content>>(){}, object));
					type_handler.sendEmptyMessage(5);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.dismissLoading2(showLoading2);
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}
}
