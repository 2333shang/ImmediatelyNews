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
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OwnerContentAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.NewsContent;
import com.shang.immediatelynews.utils.HttpRequestUtils;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.owner_tab_content_layout)
public class OwnerTabFragment extends BaseFragment{

	@ViewInject(R.id.owner_content_recyclerview)
	private RecyclerView owner_content_recyclerview;
	@ViewInject(R.id.owner_content_recyclerview_refresh)
	private MaterialRefreshLayout owner_content_recyclerview_refresh;
	
	private String title;
	private String type;
	private List<Content> contents = new ArrayList<Content>();
	
	private Handler refresh_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				owner_content_recyclerview_refresh.finishRefresh();
				break;
			case 2:
				//结束上拉刷新
				owner_content_recyclerview_refresh.finishRefreshLoadMore();
				break;
			case 3:
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
	
	public OwnerTabFragment() {
		super();
	}

	public OwnerTabFragment(String title, String type) {
		this.title = title;
		this.type = type;
	}
	
	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		getOwnerContent(type);
	}

	private void setRefreshListener() {
		owner_content_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
		owner_content_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				Toast.makeText(getActivity(), "下拉刷新", 0).show();
				refresh_handler.sendEmptyMessageDelayed(1, 2000);
			}
			
			@Override
	       public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				Toast.makeText(getActivity(), "上拉刷新", 0).show();
				refresh_handler.sendEmptyMessageDelayed(2, 2000);
	       }
		});
	}
	
	private void setRecyclerAdapter() {
		OwnerContentAdapter ownerContentAdapter = new OwnerContentAdapter(contents);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		owner_content_recyclerview.setLayoutManager(layoutManager);
		owner_content_recyclerview.setAdapter(ownerContentAdapter);
		owner_content_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		owner_content_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		ownerContentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("news", (Content)data);
				startActivity(intent);
			}
		});
		owner_content_recyclerview.setAdapter(ownerContentAdapter);
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
	
	public void getOwnerContent(String type) {
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?newsType=" + type, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String data = response.body().string();
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<List<Content>>(){}.getType();
				contents.addAll((List<Content>)gson.fromJson(data, type));
				refresh_handler.sendEmptyMessage(3);
			}
			
			@Override
			public void onFailure(Call call, IOException response) {
				
			}
		});
	}
}
