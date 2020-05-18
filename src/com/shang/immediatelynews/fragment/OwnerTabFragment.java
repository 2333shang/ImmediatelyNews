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
import com.shang.immediatelynews.adapter.OwnerContentAdapter;
import com.shang.immediatelynews.adapter.TopContentAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.ProgressDialog;
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

@ContentView(R.layout.owner_tab_content_layout)
public class OwnerTabFragment extends BaseFragment{

	protected static final Object Content = null;
	@ViewInject(R.id.owner_content_recyclerview)
	private RecyclerView owner_content_recyclerview;
	@ViewInject(R.id.owner_content_recyclerview_refresh)
	private MaterialRefreshLayout owner_content_recyclerview_refresh;
	
	private String title;
	private String type;
	private List<Content> contents = new ArrayList<Content>();
	private OwnerContentAdapter ownerContentAdapter;
	
	private Handler refresh_handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 1:
				//结束下拉刷新
				owner_content_recyclerview_refresh.finishRefresh();
				ownerContentAdapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				owner_content_recyclerview_refresh.finishRefreshLoadMore();
				ownerContentAdapter.notifyDataSetChanged();
				break;
			case 3:
				owner_content_recyclerview_refresh.finishRefresh();
				owner_content_recyclerview_refresh.finishRefreshLoadMore();
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
//		owner_content_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
		owner_content_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
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
	
	protected void getMoreOwnerData() {
		if(contents.size() == 0) {
			refresh_handler.sendEmptyMessage(3);
			return;
		}
		String newsId = contents.get(contents.size() - 1).getId();
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addmore?newsType=" + type + "&newsId=" + newsId + "&history=1";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Content>>(){}, contents, response.body().string(), refresh_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	protected void getNewsOwnerData() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?newsType=" + type + "&history=1";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Content>>(){}, contents, response.body().string(), refresh_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	private void setRecyclerAdapter() {
		ownerContentAdapter = new OwnerContentAdapter(getActivity(), contents);
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
				Intent intent = null;
				if("1".equals(type)) {
					intent = new Intent(getActivity(), NewsVideoActivity.class);
					intent.putExtra("video", (Content)data);
				}else {
					intent = new Intent(getActivity(), NewsContentActivity.class);
					intent.putExtra("news", (Content)data);
				}
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

	public void getOwnerContent(String type) {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(getActivity(), "数据加载中！");
		String url= FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?newsType=" + type + "&history=1";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					contents.addAll(GsonUtils.getGsonWithLocalDate(new TypeToken<List<Content>>(){}, object));
					refresh_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException response) {
				NetworkUtils.dismissLoading2(showLoading2);
				MainActivity activity = (MainActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}
}
