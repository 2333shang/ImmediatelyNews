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
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.activity.NewsVideoActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OrderHeadDialogAdapter;
import com.shang.immediatelynews.adapter.OwnerContentAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

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

@ContentView(R.layout.activity_collect)
public class OrderHeadFragment extends BaseFragment {
	
	@ViewInject(R.id.collect_recyclerview)
	private RecyclerView order_head_recyclerview;
	@ViewInject(R.id.collect_refresh)
	private MaterialRefreshLayout order_head_refresh;
	private int position;
	private Order order;
	private List<Content> contents = new ArrayList<Content>();
	private OrderHeadDialogAdapter orderHeadDialogAdapter;
	
	private Handler order_head_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				order_head_refresh.finishRefresh();
				orderHeadDialogAdapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				order_head_refresh.finishRefreshLoadMore();
				Log.d("news", contents.size() + "");
				orderHeadDialogAdapter.notifyDataSetChanged();
				break;
			case 3:
				order_head_refresh.finishRefresh();
				order_head_refresh.finishRefreshLoadMore();
				break;
			case 4:
				//设置适配器
				setAdapter();
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
		getOrderContent();
	}
	
    private void getOrderContent() {
    	String companyId = order.getOrderCompany();
    	String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?history=1" + "&companyId=" + companyId;
//		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?history=1";
    	Log.d("news", "url=" + url);
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					if(!contents.isEmpty()) {
						contents.clear();
					}
					contents.addAll(GsonUtils.getGsonWithLocalDate(new TypeToken<List<Content>>(){}, object));
					order_head_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	public OrderHeadFragment() {
		super();
	}
    
    public OrderHeadFragment(Order order, int position) {
		super();
		this.position = position;
		this.order = order;
	}
    
	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setAdapter() {
		orderHeadDialogAdapter = new OrderHeadDialogAdapter(getActivity(), contents);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		order_head_recyclerview.setLayoutManager(layoutManager);
		order_head_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(), DividerListItemDecoration.VERTICAL_LIST));
		order_head_recyclerview.setAdapter(orderHeadDialogAdapter);
		//设置动画
		order_head_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		orderHeadDialogAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = null;
				if("1".equals(((Content)data).getNewsType())) {
					intent = new Intent(getActivity(), NewsVideoActivity.class);
					intent.putExtra("video", (Content)data);
				}else {
					intent = new Intent(getActivity(), NewsContentActivity.class);
					intent.putExtra("news", (Content)data);
				}
				startActivity(intent);
			}
		});
	}
	
	private void setRefreshListener() {
//		owner_content_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
		order_head_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				getNewsOwnerData();
			}
			
			@Override
	       public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				getMoreOwnerData();
	       }
		});
	}

	protected void getNewsOwnerData() {
		String companyId = order.getOrderCompany();
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?history=1" + "&companyId=" + companyId;
//		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?history=1";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Content>>(){}, contents, response.body().string(), order_head_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	protected void getMoreOwnerData() {
		String companyId = order.getOrderCompany();
		if(contents.size() == 0) {
			order_head_handler.sendEmptyMessage(3);
			return;
		}
		final String newsId = contents.get(contents.size()-1).getId();
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addmore?newsId=" + newsId + "&history=1" + "&companyId=" + companyId;
//		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addmore?history=1&newsId=" + newsId;	
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Content>>(){}, contents, response.body().string(), order_head_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}
}
