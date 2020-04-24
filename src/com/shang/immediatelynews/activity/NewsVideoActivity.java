package com.shang.immediatelynews.activity;

import java.io.IOException;
import java.util.List;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.reflect.TypeToken;
import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Collect;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.GlideUtils;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

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

@ContentView(R.layout.activity_news_video)
public class NewsVideoActivity extends BaseActivity {

	@ViewInject(R.id.news_video_content)
	private JCVideoPlayerStandard news_video_content;
	@ViewInject(R.id.news_video_user_img)
	private CircleImageView news_video_user_img;
	@ViewInject(R.id.news_video_user_name)
	private TextView news_video_user_name;
	@ViewInject(R.id.news_video_user_company_name)
	private TextView news_video_user_company_name;
	@ViewInject(R.id.video_user_collectbtn)
	private Button video_user_collectbtn;
	private String newsId;
	
	private Handler news_video_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Collect c = (Collect) msg.obj;
				if(c == null || c.getCollectFlag().equals("0")) {
					video_user_collectbtn.setText("+收藏");
				}else {
					video_user_collectbtn.setText("取消收藏");
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
		Content content = (Content) getIntent().getSerializableExtra("video");
		newsId = content.getId();
		news_video_content.setUp(content.getContent(), content.getTitle());
		news_video_user_name.setText(content.getAuthorName());
		news_video_user_company_name.setText(content.getCompanyName());
		if(content.getUser().getHeadIcon() != null)
			GlideUtils.loadImage(this, news_video_user_img, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + content.getUser().getHeadIcon().getUrl());
		List<Attachment> pics = content.getPics();
		boolean hasPics = false;
		if(pics != null && !pics.isEmpty()) {
			for(Attachment a:pics) {
				if(a.getAttachmentType().equals("2")) {
					hasPics = true;
					GlideUtils.loadImage(this, news_video_content.thumbImageView, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + a.getUrl());
					break;
				}
			}
		}
		if(!hasPics) {
			GlideUtils.loadImage(this, news_video_content.thumbImageView, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + FileUploadConstant.FILE_DEFAULT_HEAD);
		}
		video_user_collectbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url;	
				if("+收藏".equals(video_user_collectbtn.getText().toString())) {
					url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/collect?newsId=" + newsId;
				}else {
					url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/collect/cancelcollect?newsId=" + newsId;
				}
				HttpRequestUtils.getRequest(url, new Callback() {
					
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						String object = response.body().string();
						if("login_invalid".equals(object)) {
							NetworkUtils.toSessionInvalid(NewsVideoActivity.this);
						}else {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									if("+收藏".equals(video_user_collectbtn.getText().toString())) {
										video_user_collectbtn.setText("取消收藏");
									}else {
										video_user_collectbtn.setText("+收藏");
									}
								}
							});
						}
					}
					
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						NetworkUtils.showErrorMessage(NewsVideoActivity.this, getMessage());
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
				news_video_handler.sendMessage(message);
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				NetworkUtils.showErrorMessage(NewsVideoActivity.this, getMessage());
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		news_video_content.release();
		ActivityUtils.removeActivities(this);
	}
}
