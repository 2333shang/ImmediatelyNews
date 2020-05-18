package com.shang.immediatelynews.activity;

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
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OrderAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_order)
public class OrderActivity extends BaseActivity {

	@ViewInject(R.id.order_cyclerview)
	private RecyclerView order_recyclerview;
	@ViewInject(R.id.order_refresh)
	private MaterialRefreshLayout order_refresh;
	@ViewInject(R.id.order_empty)
	private TextView order_empty;
	
	private List<Order> orders = new ArrayList<Order>();
	private OrderAdapter orderAdapter;
	private Handler order_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//结束下拉刷新
				order_refresh.finishRefresh();
				orderAdapter.notifyDataSetChanged();
				setOrderAdapter();
				break;
			case 2:
				//结束上拉刷新
				order_refresh.finishRefreshLoadMore();
				orderAdapter.notifyDataSetChanged();
				break;
			case 3:
				order_refresh.finishRefreshLoadMore();
				order_refresh.finishRefresh();
				break;
			case 4:
				setOrderAdapter();
				setRefreshListener();
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		ActivityUtils.addActivities(this);
		getOrderDatas();
	}

	protected void setOrderAdapter() {
		if(orders.size() == 0) {
			order_empty.setVisibility(View.VISIBLE);
			order_recyclerview.setVisibility(View.GONE);
			return;
		}
		orderAdapter = new OrderAdapter(this, orders);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		order_recyclerview.setLayoutManager(layoutManager);
		order_recyclerview.setAdapter(orderAdapter);
		order_recyclerview.addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		order_recyclerview.setItemAnimator(new DefaultItemAnimator());
		orderAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(OrderActivity.this, OrderCotentActivity.class);
				intent.putExtra("orders", (Order) data);
				startActivity(intent);
			}
		});
	}

	private void getOrderDatas() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/orders";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(OrderActivity.this);
				}else {
					orders.addAll((List<Order>)GsonUtils.getGsonWithLocalDate(new TypeToken<List<Order>>(){}, object));
					order_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.showErrorMessage(OrderActivity.this, getMessage());
			}
		});
	}
	
	private void setRefreshListener() {
		order_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				getNewData();
			}
			
			@Override
	        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				if(orders.size() == 0) {
					order_handler.sendEmptyMessage(3);
					return;
				}
				getMoreData(orders.get(orders.size()-1));
	       }
		});
	}
	
	protected void getNewData() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/orders";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(OrderActivity.this, new TypeToken<List<Order>>(){}, orders, response.body().string(), order_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				NetworkUtils.showErrorMessage(OrderActivity.this, getMessage());
			}
		});
	}

	protected void getMoreData(Order order) {
		String orderId = order.getId();	
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/moreorder?orderId=" + orderId;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(OrderActivity.this, new TypeToken<List<Order>>(){}, orders, response.body().string(), order_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				NetworkUtils.showErrorMessage(OrderActivity.this, getMessage());
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
