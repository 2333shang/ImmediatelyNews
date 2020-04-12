package com.shang.immediatelynews.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.CollectAdapter;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Collect;
import com.shang.immediatelynews.entities.NewsContent;
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

@ContentView(R.layout.activity_collect)
public class CollectActivity extends Activity {

	@ViewInject(R.id.collect_recyclerview)
	private RecyclerView collect_recyclerview;
	
	private List<Collect> collects = new ArrayList<Collect>();
	
	private Handler collect_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setCollectAdapter();
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

	protected void setCollectAdapter() {
		CollectAdapter collectAdapter = new CollectAdapter(CollectActivity.this, collects);
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
				Intent intent = new Intent(CollectActivity.this, NewsContentActivity.class);
				intent.putExtra("collects", (Collect) data);
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
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<List<Collect>>(){}.getType();
				Log.d("news", object);
				collects.addAll((List<Collect>)gson.fromJson(object, type));
				collect_handler.sendEmptyMessage(1);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				
			}
		});
	}
}
