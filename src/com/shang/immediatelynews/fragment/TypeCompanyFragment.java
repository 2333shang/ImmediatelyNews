package com.shang.immediatelynews.fragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TypeOrderHeadAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.customview.OrderHeadDialogFragment;
import com.shang.immediatelynews.entities.Company;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.utils.HttpRequestUtils;

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
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.type_fragment_layout)
public class TypeCompanyFragment extends BaseFragment {
	
	@ViewInject(R.id.type_head_recyclerview)
	private RecyclerView type_head_recyclerView;
	@ViewInject(R.id.type_recyclerview_refresh)
	private MaterialRefreshLayout type_recyclerview_refresh;
	@ViewInject(R.id.company_name)
	private TextView company_name;
	
	private List<Order> orders = new ArrayList<Order>();
	private List<Company> rands = new ArrayList<Company>();
	
	private Handler type_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				
				break;
			case 2:
				
				break;
			case 3:
				//设置头部适配器
				setHeadAdapter(orders);
				break;
			case 4:
				///设置内容适配器
				setContent(rands);
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
		getRandContent();
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void setContent(List<Company> rand) {
		Company company = (Company) rand.get(0);
		company_name.setText(company.getCompanyName());
	}
	
	private void setHeadAdapter(List<Order> order) {
		TypeOrderHeadAdapter orderAdapter = new TypeOrderHeadAdapter(order);
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
				new OrderHeadDialogFragment(position).show(getChildFragmentManager(), null);
			}
		});
	}
	
	public void getOrderContent() {
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/orders", new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<List<Order>>(){}.getType();
				orders.addAll((List<Order>)gson.fromJson(data, type));
				type_handler.sendEmptyMessage(3);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				Log.d("news", "error=" + exception);
			}
		});
	}
	
	public void getRandContent() {
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/company/rand", new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<List<Company>>(){}.getType();
				rands.addAll((List<Company>)gson.fromJson(data, type));
				type_handler.sendEmptyMessage(4);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				Log.d("news", "error=" + exception);
			}
		});
	}
}
