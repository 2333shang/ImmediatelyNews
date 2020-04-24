package com.shang.immediatelynews.fragment;

import java.io.IOException;
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
import com.shang.immediatelynews.activity.OrderCotentActivity;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.OrderContentDetailAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Company;
import com.shang.immediatelynews.entities.Content;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_order_cotent_detail)
public class OrderContentFragment extends BaseFragment {
	
	private String title;
	private String companyId;
	private String newsType;
	private Company company;
	private OrderContentDetailAdapter adapter;
	
	@ViewInject(R.id.order_content_detail_recyclerview)
	private RecyclerView order_content_detail_recyclerview;
	@ViewInject(R.id.order_content_recyclerview_refresh)
	private MaterialRefreshLayout order_content_recyclerview_refresh;
	
	private Handler order_content_detail_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				order_content_recyclerview_refresh.finishRefresh();
				adapter.notifyDataSetChanged();
				break;
			case 2:
				order_content_recyclerview_refresh.finishRefreshLoadMore();
				adapter.notifyDataSetChanged();
				break;
			case 3:
				order_content_recyclerview_refresh.finishRefresh();
				order_content_recyclerview_refresh.finishRefreshLoadMore();
				break;
			case 4:
				setDetailAdapter();
				setRefreshListener();
				break;
			default:
				break;
			}
		};
	};
	
	public OrderContentFragment(String title, String companyId) {
		super();
		this.title = title;
		this.companyId = companyId;
	}

	protected void setDetailAdapter() {
		adapter = new OrderContentDetailAdapter(getActivity(), newsType, company);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		order_content_detail_recyclerview.setLayoutManager(layoutManager);
		order_content_detail_recyclerview.setAdapter(adapter);
		order_content_detail_recyclerview.addItemDecoration(new DividerListItemDecoration(getActivity(),DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		order_content_detail_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = null;
				if("1".equals(newsType)) {
					intent = new Intent(getActivity(), NewsVideoActivity.class);
					intent.putExtra("video", (Content)data);
				}else {
					intent = new Intent(getActivity(), NewsContentActivity.class);
					intent.putExtra("news", (Content)data);
				}
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.video_content_detail_right_in, R.anim.video_content_detail_alpha_dismiss);
			}
		});
	}

	@Override
	public void initFragmentData(Bundle savedInstanceState) {
		if("新闻".equals(title)) {
			newsType = "0";
		}else {
			newsType = "1";
		}
		getOrderCompanyNews(companyId);
	}

	@Override
	public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return x.view().inject(this, inflater, container);
	}
	
	private void getOrderCompanyNews(String companyId) {
//		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/company/companybyid?companyId=" + companyId + "&newsType=" + newsType;
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?newsType=" + newsType;
		Log.d("news", url);
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(getActivity());
				}else {
					List<Content> contents = GsonUtils.getGsonWithLocalDate(new TypeToken<List<Content>>(){}, object);
					company = new Company();
					if(newsType.equals("0"))
						company.setContent(contents);
					else
						company.setVideoContent(contents);
					order_content_detail_handler.sendEmptyMessage(4);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				OrderCotentActivity activity = (OrderCotentActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
		
	}
	
	private void setRefreshListener() {
		order_content_recyclerview_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
			
			@Override
			public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
				//下拉刷新
				getNewData();
			}
			
			@Override
	        public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
	       	  	//上拉刷新
				getMoreData();
	       }
		});
	}

	protected void getMoreData() {
		String newsId = null;
		if(newsType.equals("0"))
			newsId = company.getContent().get(company.getContent().size() - 1).getId();
		else
			newsId = company.getVideoContent().get(company.getVideoContent().size() - 1).getId();
//		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addmore?newsId=" + newsId + "&newsType=" + newsType + "&companyId=" + companyId;
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addmore?newsId=" + newsId + "&newsType=" + newsType ;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				if(newsType.equals("0"))
					NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Content>>(){}, company.getContent(), response.body().string(), order_content_detail_handler);
				else
					NetworkUtils.addMoreDataResponse(getActivity(), new TypeToken<List<Content>>(){}, company.getVideoContent(), response.body().string(), order_content_detail_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				OrderCotentActivity activity = (OrderCotentActivity) getActivity();
				NetworkUtils.showErrorMessage(activity, activity.getMessage());
			}
		});
	}

	protected void getNewData() {
//		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addmore?newsId=" + newsId + "&newsType=" + newsType + "&companyId=" + companyId;
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/owner?newsType=" + newsType ;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				if(newsType.equals("0"))
					NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Content>>(){}, company.getContent(), response.body().string(), order_content_detail_handler);
				else
					NetworkUtils.addNewsDataResponse(getActivity(), new TypeToken<List<Content>>(){}, company.getVideoContent(), response.body().string(), order_content_detail_handler);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				OrderCotentActivity activity = (OrderCotentActivity) getActivity();
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
