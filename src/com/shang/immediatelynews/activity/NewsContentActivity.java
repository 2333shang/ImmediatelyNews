package com.shang.immediatelynews.activity;

import java.io.IOException;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.richeditor.RichEditor;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Collect;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.Top;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.GlideUtils;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_news_content)
public class NewsContentActivity extends BaseActivity {

	@ViewInject(R.id.news_content_title)
	private TextView news_content_title;
	@ViewInject(R.id.news_content_show)
	private RichEditor news_content_show;
	@ViewInject(R.id.content_user_img)
	private CircleImageView content_user_img;
	@ViewInject(R.id.content_user_name)
	private TextView content_user_name;
	@ViewInject(R.id.content_user_company_name)
	private TextView content_user_company_name;
	@ViewInject(R.id.content_user_collectbtn)
	private Button content_user_collectbtn;
	private Content content;
	private Top top;
	private Collect collect;
	private String newsId;
	
	private Handler news_content_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Collect c = (Collect) msg.obj;
				if(c == null || c.getCollectFlag().equals("0")) {
					content_user_collectbtn.setText("+收藏");
				}else {
					content_user_collectbtn.setText("取消收藏");
				}
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
		Intent intent = getIntent();
		content = (Content) intent.getSerializableExtra("news");
		top = (Top) intent.getSerializableExtra("tops");
		collect = (Collect) intent.getSerializableExtra("collects");
		news_content_show.setPadding(10, 10, 10, 10);
		news_content_show.setInputEnabled(false);
		if(top != null) {
			newsId = top.getNewsId();
			news_content_title.setText(top.getContent().getTitle());
			news_content_show.setHtml(top.getContent().getContent());
			content_user_name.setText(top.getContent().getAuthorName());
			content_user_company_name.setText(top.getContent().getCompanyName());
			if(top.getContent().getUser().getHeadIcon() != null)
				GlideUtils.loadImage(this, content_user_img, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + top.getContent().getUser().getHeadIcon().getUrl());
		}else if(content != null){
			newsId = content.getId();
			news_content_show.setHtml(content.getContent());
			content_user_name.setText(content.getAuthorName());
			news_content_title.setText(content.getTitle());
			content_user_company_name.setText(content.getCompanyName());
			if(content.getUser().getHeadIcon() != null)
				GlideUtils.loadImage(this, content_user_img, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + content.getUser().getHeadIcon().getUrl());
		}else if(collect != null){
			newsId = collect.getRelatedId();
			news_content_title.setText(collect.getContent().getTitle());
			news_content_show.setHtml(collect.getContent().getContent());
			content_user_name.setText(collect.getContent().getAuthorName());
			content_user_company_name.setText(collect.getContent().getCompanyName());
			if(collect.getContent().getUser().getHeadIcon() != null)
				GlideUtils.loadImage(this, content_user_img, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + collect.getContent().getUser().getHeadIcon().getUrl());
		}
		content_user_collectbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url;	
				if("+收藏".equals(content_user_collectbtn.getText().toString())) {
					url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/collect?newsId=" + newsId;
				}else {
					url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/cancelcollect?newsId=" + newsId;
				}
				HttpRequestUtils.getRequest(url, new Callback() {
					
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if("+收藏".equals(content_user_collectbtn.getText().toString())) {
									content_user_collectbtn.setText("取消收藏");
								}else {
									content_user_collectbtn.setText("+收藏");
								}
							}
						});
					}
					
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						NetworkUtils.showErrorMessage(NewsContentActivity.this, getMessage());
					}
				});
			}
		});
		setCollect();
	}
	
	private void setCollect() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/collected?newsId=" + newsId;
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				Collect collected = GsonUtils.getGsonWithLocalDate(new TypeToken<Collect>(){}, response.body().string());
				Message message = Message.obtain();
				message.obj = collected;
				message.what = 1;
				news_content_handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
