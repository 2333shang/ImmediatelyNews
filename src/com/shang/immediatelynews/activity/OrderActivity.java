package com.shang.immediatelynews.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OrderAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Collect;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.utils.HttpRequestUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_order)
public class OrderActivity extends Activity {

	@ViewInject(R.id.order_cyclerview)
	private RecyclerView order_recyclerview;
	
	private List<Order> orders = new ArrayList<Order>();
	private Handler order_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setOrderAdapter();
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
		getOrderDatas();
	}

	protected void setOrderAdapter() {
		OrderAdapter orderAdapter = new OrderAdapter(this, orders);
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
				Log.d("news", object);
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<List<Order>>(){}.getType();
				orders.addAll((List<Order>)gson.fromJson(object, type));
				order_handler.sendEmptyMessage(1);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				
			}
		});
	}
}
