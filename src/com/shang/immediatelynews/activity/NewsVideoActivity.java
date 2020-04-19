package com.shang.immediatelynews.activity;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.ActivityUtils;

import android.app.Activity;
import android.os.Bundle;

@ContentView(R.layout.activity_news_video)
public class NewsVideoActivity extends Activity {

	@ViewInject(R.id.news_video_content)
	private JCVideoPlayerStandard news_video_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		ActivityUtils.addActivities(this);
		Content content = (Content) getIntent().getSerializableExtra("video");
		news_video_content.setUp(content.getContent(), content.getTitle());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		news_video_content.release();
		ActivityUtils.removeActivities(this);
	}
}
