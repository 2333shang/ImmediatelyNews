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
import com.shang.immediatelynews.adapter.CollectAdapter;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Collect;
import com.shang.immediatelynews.entities.Content;
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
import android.view.View;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_collect)
public class CollectActivity extends BaseActivity {

	@ViewInject(R.id.collect_recyclerview)
	private RecyclerView collect_recyclerview;
	@ViewInject(R.id.collect_refresh)
	private MaterialRefreshLayout collect_refresh;
	@ViewInject(R.id.collect_empty)
	private TextView collect_empty;
	
	private List<Collect> collects = new ArrayList<Collect>();
	private CollectAdapter collectAdapter;
	
	private Handler collect_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//结束下拉刷新
				collect_refresh.finishRefresh();
				collectAdapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				collect_refresh.finishRefreshLoadMore();
				collectAdapter.notifyDataSetChanged();
				break;
			case 3:
				collect_refresh.finishRefreshLoadMore();
				collect_refresh.finishRefresh();
				break;
			case 4:
				setCollectAdapter();
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

	protected void setCollectAdapter() {
		if(collects.size() == 0) {
			collect_empty.setVisibility(View.VISIBLE);
			collect_recyclerview.setVisibility(View.GONE);
			return;
		}
		collectAdapter = new CollectAdapter(CollectActivity.this, collects);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		collect_recyclerview.setLayoutManager(layoutManager);
		collect_recyclerview.setAdapter(collectAdapter);
		collect_recyclerview.addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		collect_recyclerview.setItemAnimator(new DefaultItemAnimator());
		collectAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = null;
				if("1".equals(((Collect) data).getContent().getNewsType())) {
					intent = new Intent(CollectActivity.this, NewsVideoActivity.class);
					Collect c = (Collect) data;
					Content content = c.getContent();
					content.setId(c.getRelatedId());
					intent.putExtra("video", content);
				}else {
					intent = new Intent(CollectActivity.this, NewsContentActivity.class);
					intent.putExtra("collects", (Collect) data);
				}
				startActivity(intent);
			}
		});
	}

	private void getOrderDatas() {
		String url  = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/collects";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(CollectActivity.this);
				}else {
					collects.addAll((List<Collect>)GsonUtils.getGsonWithLocalDate(new TypeToken<List<Collect>>(){}, object));
					collect_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.showErrorMessage(CollectActivity.this, getMessage());
			}
		});
	}
	
	private void setRefreshListener() {
		collect_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				getNewData();
			}
			
			@Override
	        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				getMoreData(collects.get(collects.size()-1));
	       }
		});
	}
	
	protected void getNewData() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/collects";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(CollectActivity.this, new TypeToken<List<Collect>>(){}, collects, response.body().string(), collect_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				NetworkUtils.showErrorMessage(CollectActivity.this, getMessage());
			}	
		});
	}

	protected void getMoreData(Collect collect) {
		String collectId = collect.getId();
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/morecollects?collectId=" + collectId;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(CollectActivity.this, new TypeToken<List<Collect>>(){}, collects, response.body().string(), collect_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				NetworkUtils.showErrorMessage(CollectActivity.this, getMessage());
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
