package com.shang.immediatelynews.activity;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.richeditor.RichEditor;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.Collect;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.Top;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

@ContentView(R.layout.activity_news_content)
public class NewsContentActivity extends AppCompatActivity {

	@ViewInject(R.id.news_content_title)
	private TextView news_content_title;
	@ViewInject(R.id.news_content_show)
	private RichEditor news_content_show;
	private Content content;
	private Top top;
	private Collect collect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		Intent intent = getIntent();
		content = (Content) intent.getSerializableExtra("news");
		top = (Top) intent.getSerializableExtra("tops");
		collect = (Collect) intent.getSerializableExtra("collects");
		news_content_show.setPadding(10, 10, 10, 10);
		news_content_show.setInputEnabled(false);
		if(top != null) {
			news_content_title.setText(top.getContent().getTitle());
			news_content_show.setHtml(top.getContent().getContent());
		}else if(content != null){
			news_content_title.setText(content.getTitle());
			news_content_show.setHtml(content.getContent());
		}else if(collect != null){
			news_content_title.setText(collect.getContent().getTitle());
			news_content_show.setHtml(collect.getContent().getContent());
		}
	}
}
