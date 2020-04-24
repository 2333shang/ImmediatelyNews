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
import com.shang.immediatelynews.activity.NewsVideoActivity;
import com.shang.immediatelynews.activity.TopManagerActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.TopManagerAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.Top;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_top_manager_detail)
public class TopManagerFragment extends BaseFragment {

	private String title;
		/**
	 * type: 0:未处理
	 * 		 1:已同意
	 * 		 2:已退回
	 */
	private String type;
	private List<Top> tops = new ArrayList<Top>();
	private TopManagerAdapter adapter;
	
	@ViewInject(R.id.top_manager_recycler)
	private RecyclerView top_manager_recycler;
	@ViewInject(R.id.top_manager_refresh)
	private MaterialRefreshLayout top_manager_refresh;
	@ViewInject(R.id.top_manager_empty)
	private TextView top_manager_empty;
	
	private Handler top_manager_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//结束下拉刷新
				top_manager_refresh.finishRefresh();
				adapter.notifyDataSetChanged();
				break;
			case 2:
				//结束上拉刷新
				top_manager_refresh.finishRefreshLoadMore();
				adapter.notifyDataSetChanged();
				break;
			case 3:
				top_manager_refresh.finishRefresh();
				top_manager_refresh.finishRefreshLoadMore();
				break;
			case 4:
				setTopManagerAdapter();
				setRefreshListener();
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

	protected void setTopManagerAdapter() {
		if(tops.size() == 0) {
			top_manager_empty.setVisibility(View.VISIBLE);
			top_manager_recycler.setVisibility(View.GONE);
			return;
		}
		adapter = new TopManagerAdapter(getActivity(), tops, type);
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
				Top top = (Top) data;
				Intent intent = null;
				if("0".equals(top.getContent().getNewsType())) {
					intent = new Intent(getActivity(), NewsContentActivity.class);
					intent.putExtra("tops", top);
				}else {
					intent = new Intent(getActivity(), NewsVideoActivity.class);
					intent.putExtra("video", top.getContent());
				}
				startActivity(intent);
			}
		});
	}

	private void getNewsManagerDatas() {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(getActivity(), "数据加载中！");
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
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					if(tops.isEmpty()) {
						tops.addAll((List<Top>)GsonUtils.getGsonWithLocalDate(new TypeToken<List<Top>>(){}, object));
					}
					top_manager_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.dismissLoading2(showLoading2);
				TopManagerActivity activity = (TopManagerActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}
	
	private void setRefreshListener() {
//		owner_content_recyclerview_refresh.autoRefresh();//设置自动下拉刷新
		top_manager_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
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
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Top>>() {}, tops, response.body().string(), top_manager_handler);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				TopManagerActivity activity = (TopManagerActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	protected void getMoreOwnerData() {
		String url = null;
		if(tops.isEmpty()) {
			Toast.makeText(getActivity(), "当前没有新闻了", 0).show();
			top_manager_handler.sendEmptyMessage(3);
			return;
		}
		String topId = tops.get(tops.size() - 1).getId();
		if("0".equals(type)) {
			url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/moretodoTop?topId=" + topId;
		}else if("1".equals(type)) {
			String applyStatus = "2";
			url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/morehistop?applyStatus=" + applyStatus + "&topId=" + topId;
		}else if("2".equals(type)) {
			String applyStatus = "0";
			url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/morehistop?applyStatus=" + applyStatus + "&topId=" + topId;
		}
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Top>>() {}, tops, response.body().string(), top_manager_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				TopManagerActivity activity = (TopManagerActivity) getActivity();
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
