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
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.NewsContentActivity;
import com.shang.immediatelynews.activity.NewsManagerActivity;
import com.shang.immediatelynews.activity.NewsVideoActivity;
import com.shang.immediatelynews.adapter.NewsManagerAdapter;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.opengl.Visibility;
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
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_news_manager_detail)
public class NewsManagerFragment extends BaseFragment {

	private String title;
	private String newType;
	private String history;
	/**
	 * type: 0:本日新闻
	 * 		 1:本日视频
	 * 		 2:历史新闻
	 * 		 3:历史视频
	 */
	private String type;
	private List<Content> contents = new ArrayList<Content>();
	private NewsManagerAdapter adapter;
	
	@ViewInject(R.id.news_manager_recycler)
	private RecyclerView news_manager_recycler;
	@ViewInject(R.id.news_manager_refresh)
	private MaterialRefreshLayout news_manager_refresh;
	@ViewInject(R.id.news_manager_empty)
	private TextView news_manager_empty;
	
	private Handler news_manager_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//结束下拉刷新
				news_manager_refresh.finishRefresh();
				adapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				news_manager_refresh.finishRefreshLoadMore();
				adapter.notifyDataSetChanged();
				break;
			case 3:
				news_manager_refresh.finishRefresh();
				news_manager_refresh.finishRefreshLoadMore();
				break;
			case 4:
				setNewsManagerAdapter();
				setRefreshListener();
				break;
			default:
				break;
			}
		};
	};
	
	public NewsManagerFragment(String title, String type) {
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
		if(contents.size() == 0) {
			news_manager_empty.setVisibility(View.VISIBLE);
			news_manager_recycler.setVisibility(View.GONE);
			return;
		}
		adapter = new NewsManagerAdapter(this, getActivity(), newType, contents, type);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		news_manager_recycler.setLayoutManager(layoutManager);
		news_manager_recycler.setAdapter(adapter);
		news_manager_recycler.addItemDecoration(new DividerListItemDecoration(getActivity(), DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		news_manager_recycler.setItemAnimator(new DefaultItemAnimator());
		adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = null;
				if("1".equals(newType)) {
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == 2) {
			news_manager_refresh.autoRefresh();
//			String title = data.getStringExtra("updatetitle");
//			String content = data.getStringExtra("updatecontent");
//			String id = data.getStringExtra("updateid");
//			for(Content c:contents) {
//				if(c.getId().equals(id)) {
//					c.setContent(content);
//					c.setTitle(title);
//					break;
//				}
//			}
//			adapter.notifyDataSetChanged();
		}
	}
	
	private void getNewsManagerDatas() {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(getActivity(), "数据加载中！");
		if("0".equals(type)) {
			newType = "0";
			history = "0";
		}else if("1".equals(type)) {
			newType = "1";
			history = "0";
		}else if("2".equals(type)) {
			newType = "0";
			history = "1";
		}else if("3".equals(type)) {
			newType = "1";
			history = "1";
		}
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?newsType=" + newType + "&history=" + history;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					if(contents.isEmpty()) {
						contents.addAll((List<Content>)GsonUtils.getGsonWithLocalDate(new TypeToken<List<Content>>(){}, object));
					}
					news_manager_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.dismissLoading2(showLoading2);
				NewsManagerActivity activity = (NewsManagerActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}
	
	private void setRefreshListener() {
//		owner_content_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
		news_manager_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
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
		if("0".equals(type)) {
			newType = "0";
			history = "0";
		}else if("1".equals(type)) {
			newType = "1";
			history = "0";
		}else if("2".equals(type)) {
			newType = "0";
			history = "1";
		}else if("3".equals(type)) {
			newType = "1";
			history = "1";
		}
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?newsType=" + newType + "&history=" + history;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Content>>() {}, contents, response.body().string(), news_manager_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NewsManagerActivity activity = (NewsManagerActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	protected void getMoreOwnerData() {
		String newsId;
		if(contents.isEmpty()) {
			Toast.makeText(getActivity(), "当前没有新闻了", 0).show();
			news_manager_handler.sendEmptyMessage(3);
			return;
		}
		newsId = contents.get(contents.size() - 1).getId();
		if("0".equals(type)) {
			newType = "0";
			history = "0";
		}else if("1".equals(type)) {
			newType = "1";
			history = "0";
		}else if("2".equals(type)) {
			newType = "0";
			history = "1";
		}else if("3".equals(type)) {
			newType = "1";
			history = "1";
		}
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addmore?newsType=" + newType + "&history=" + history + "&newsId=" + newsId;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Content>>() {}, contents, response.body().string(), news_manager_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NewsManagerActivity activity = (NewsManagerActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
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
