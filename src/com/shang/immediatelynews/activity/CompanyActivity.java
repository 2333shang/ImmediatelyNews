package com.shang.immediatelynews.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.R.id;
import com.shang.immediatelynews.R.layout;
import com.shang.immediatelynews.adapter.CompanyAdapter;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Company;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.utils.ActivityUtils;
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
import android.view.View;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_order)
public class CompanyActivity extends BaseActivity {
	
	@ViewInject(R.id.order_cyclerview)
	private RecyclerView order_cyclerview;
	@ViewInject(R.id.order_refresh)
	private MaterialRefreshLayout order_refresh;
	
	private List<Company> company = new ArrayList<Company>();
	private CompanyAdapter companyAdapter;
	private Handler company_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				order_refresh.finishRefresh();
				companyAdapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				order_refresh.finishRefreshLoadMore();
				companyAdapter.notifyDataSetChanged();
				break;
			case 3:
				order_refresh.finishRefresh();
				order_refresh.finishRefreshLoadMore();
				break;
			case 4:
				//设置适配器
				setRecyclerAdapter();
				//设置刷新监听
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
		getCompany();
	}

	private void getCompany() {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(this, "数据加载中！");
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/company/company";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				company.addAll(GsonUtils.getGsonWithLocalDate(new TypeToken<List<Company>>(){}, response.body().string()));
				Log.d("news", company.toString());
				company_handler.sendEmptyMessage(4);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.dismissLoading2(showLoading2);
				NetworkUtils.showErrorMessage(CompanyActivity.this, getMessage());
			}
		});
	}
	
	private void setRecyclerAdapter() {
		companyAdapter = new CompanyAdapter(this, company);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		order_cyclerview.setLayoutManager(layoutManager);
		order_cyclerview.setAdapter(companyAdapter);
		order_cyclerview.addItemDecoration(new DividerListItemDecoration(this,DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		order_cyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		companyAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(CompanyActivity.this, OrderCotentActivity.class);
				Order order = new Order();
				order.setOrderCompany(((Company)data).getId());
				intent.putExtra("orders", order);
				startActivity(intent);
			}
		});
		order_cyclerview.setAdapter(companyAdapter);
	}
	
	private void setRefreshListener() {
//		owner_content_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
		order_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
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
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/company/company";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(CompanyActivity.this, new TypeToken<List<Company>>(){}, company, response.body().string(), company_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				NetworkUtils.showErrorMessage(CompanyActivity.this, getMessage());
			}
		});
	}

	protected void getMoreOwnerData() {
		String companyId = company.get(company.size() - 1).getId();
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/company/morecompany?companyId=" + companyId;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(CompanyActivity.this, new TypeToken<List<Company>>(){}, company, response.body().string(), company_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				NetworkUtils.showErrorMessage(CompanyActivity.this, getMessage());
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
