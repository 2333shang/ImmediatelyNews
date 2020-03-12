package com.shang.immediatelynews.activity;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.bumptech.glide.Glide;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.NewsContentAdapter;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.customlayout.FullyLinearLayoutManager;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.NewsComments;
import com.shang.immediatelynews.entities.NewsContent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

@ContentView(R.layout.activity_news_content2)
public class NewsContentActivity2 extends AppCompatActivity {

	@ViewInject(R.id.webview)
	private WebView webview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		webview.loadUrl("http://10.0.2.2:8080/news/employee/hello");
	}
}
