package com.shang.immediatelynews.fragment;

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
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TopManagerAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.Top;
import com.shang.immediatelynews.utils.HttpRequestUtils;

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

@ContentView(R.layout.activity_top_manager_detail)
public class TopManagerFragment extends BaseFragment {

	private String title;
		/**
	 * type: 0:本日新闻
	 * 		 1:本日视频
	 * 		 2:历史新闻
	 * 		 3:历史视频
	 */
	private String type;
	private List<Top> tops = new ArrayList<Top>();
	
	@ViewInject(R.id.top_manager_recycler)
	private RecyclerView top_manager_recycler;
	
	private Handler top_manager_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setNewsManagerAdapter();
				break;

			default:
				break;
			}
		};
	};
	
	public TopManagerFragment(String title, String type) {
		super();
		this.title = title;
		this.type = type;
	}

	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		getNewsManagerDatas();
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}

	protected void setNewsManagerAdapter() {
		TopManagerAdapter adapter = new TopManagerAdapter(getActivity(), tops);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		top_manager_recycler.setLayoutManager(layoutManager);
		top_manager_recycler.setAdapter(adapter);
		top_manager_recycler.addItemDecoration(new DividerListItemDecoration(getActivity(), DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		top_manager_recycler.setItemAnimator(new DefaultItemAnimator());
		adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("news", (Content) data);
				startActivity(intent);
			}
		});
	}

	private void getNewsManagerDatas() {
		String url = null;
		if("0".equals(type)) {
			url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/todotop";
		}else if("1".equals(type)) {
			String applyStatus = "2";
			url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/histop?applyStatus=" + applyStatus;
		}else if("2".equals(type)) {
			String applyStatus = "0";
			url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/histop?applyStatus=" + applyStatus;
		}
		Log.d("news", "url=" + url);
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String object = response.body().string();
				Log.d("news", object);
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<List<Top>>(){}.getType();
				tops.addAll((List<Top>)gson.fromJson(object, type));
				top_manager_handler.sendEmptyMessage(1);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				
			}
		});
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
