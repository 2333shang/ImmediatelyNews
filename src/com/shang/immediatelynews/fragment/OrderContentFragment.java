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
import com.shang.immediatelynews.adapter.OrderContentDetailAdapter;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.Company;
import com.shang.immediatelynews.entities.Content;
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

@ContentView(R.layout.activity_order_cotent_detail)
public class OrderContentFragment extends BaseFragment {
	
	private String title;
	private String companyId;
	private String newsType;
	private Company company;
	
	@ViewInject(R.id.order_content_detail_recyclerview)
	private RecyclerView order_content_detail_recyclerview;
	
	private Handler order_content_detail_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setDetailAdapter();
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
		OrderContentDetailAdapter adapter = new OrderContentDetailAdapter(getActivity(), newsType, company);
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
				Intent intent = new Intent(getActivity(), NewsContentActivity.class);
				intent.putExtra("news", (Content)data);
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
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
//				Type type = new TypeToken<Company>(){}.getType();
//				Log.d("news", "order=" + gson.fromJson(object, type).toString());
//				company = (Company)gson.fromJson(object, type);
				Type type = new TypeToken<List<Content>>(){}.getType();
				List<Content> contents = (List<Content>)gson.fromJson(object, type);
				company = new Company();
				if(newsType.equals("0"))
					company.setContent(contents);
				else
					company.setVideoContent(contents);
				order_content_detail_handler.sendEmptyMessage(1);
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
