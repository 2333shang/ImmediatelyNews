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
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

@ContentView(R.layout.activity_news_content)
public class NewsContentActivity extends AppCompatActivity {

	@ViewInject(R.id.news_content_detail_image)
	private CircleImageView news_content_detail_image;
	@ViewInject(R.id.news_content_detail_user)
	private TextView news_content_detail_user;
	@ViewInject(R.id.news_content_detail_authortype)
	private TextView news_content_detail_authortype;
	@ViewInject(R.id.news_content_detail_order)
	private Button news_content_detail_order;
	@ViewInject(R.id.news_content_text)
	private TextView news_content_text;
	@ViewInject(R.id.news_content_detail_phraisebtn)
	private ImageView news_content_detail_phraisebtn;
	@ViewInject(R.id.news_content_detail_collectbtn)
	private ImageView news_content_detail_collectbtn;
	@ViewInject(R.id.news_comment_send_edit)
	private EditText news_comment_send_edit;
	@ViewInject(R.id.news_comment_send_btn)
	private Button news_comment_send_btn;
	@ViewInject(R.id.new_content_detail_recyclerview)
	private RecyclerView new_content_detail_recyclerview;
	@ViewInject(R.id.news_content_detail_title)
	private TextView news_content_detail_title;
	@ViewInject(R.id.video_detail_recyclerview)
	private ScrollView video_detail_recyclerview;
	private PopupWindow popupWindow;
	private int i = 7;
	private NewsContent newsContent;
	private NewsContentAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		Intent intent = getIntent();
		newsContent = (NewsContent) intent.getSerializableExtra("news");
		news_content_detail_title.setText(newsContent.getTitle());
		Glide.with(this).load(newsContent.getAuthorImageUrl()).into(news_content_detail_image);
		news_content_detail_user.setText(newsContent.getAuthorName());
		news_content_detail_authortype.setText(newsContent.getAuthorType());
		news_content_text.setText(newsContent.getContent());
		news_content_detail_order.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("关注".equals(news_content_detail_order.getText())) {
					news_content_detail_order.setBackgroundResource(R.drawable.ordered_button_custom);
					news_content_detail_order.setText("已关注");
				}else {
					news_content_detail_order.setBackgroundResource(R.drawable.order_button_custom);
					news_content_detail_order.setText("关注");
				}
			}
		});
		adapter = new NewsContentAdapter(this);
		FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		new_content_detail_recyclerview.setLayoutManager(layoutManager);
		new_content_detail_recyclerview.setAdapter(adapter);
		new_content_detail_recyclerview.addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		new_content_detail_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
					
			@Override
			public void onItemClick(View v, int position, Object data) {
				Intent intent = new Intent(NewsContentActivity.this, NewsCommentReplyActivity.class);
				intent.putExtra("data", (NewsComments)data);
				startActivity(intent);
			}
		});
		new_content_detail_recyclerview.setAdapter(adapter);
		//使得RecyclerView能够固定自身size不受adapter变化的影响
//		new_content_detail_recyclerview.setHasFixedSize(true);
		//关闭RecyclerView的嵌套滑动特性,整个页面滑动仅依靠ScrollView实现
//		new_content_detail_recyclerview.setNestedScrollingEnabled(false);
	}
	
	public void addComments(View v) {
		if(popupWindow == null) {
			View inflate = View.inflate(this, R.layout.news_comment_popup, null);
			popupWindow = new PopupWindow(inflate, ((View)v.getParent()).getWidth(), 100);
			popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
			//popupwindow中含有EditText，Edtitext光标闪烁但是无法弹出软键盘的解决办法
			popupWindow.setFocusable(true);
			news_comment_send_edit = (EditText) inflate.findViewById(R.id.news_comment_send_edit);
			news_comment_send_btn = (Button) inflate.findViewById(R.id.news_comment_send_btn);
		}
		//设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		//软键盘不会挡着popupwindow
		popupWindow.showAtLocation((View) v.getParent(), Gravity.BOTTOM, 0, -100);
	}
	
	public void sendComments(View v) {
		String comment = news_comment_send_edit.getText().toString();
		news_comment_send_edit.setText("");
		String newsUserImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
		String username = "username" + i++;
		NewsComments newsComments = new NewsComments(newsUserImageUrl, comment, username);
		adapter.getComments().add(newsComments);
		new_content_detail_recyclerview.scrollToPosition(0);
		video_detail_recyclerview.scrollTo(0, news_content_text.getHeight());
		if(popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}
	
	public void phraise(View v) {
		if(!newsContent.isPhraised()) {
			newsContent.setPhraised(true);
			news_content_detail_phraisebtn.setImageResource(R.drawable.phraise_press);
		}else{
			newsContent.setPhraised(false);
			news_content_detail_phraisebtn.setImageResource(R.drawable.phraise);
		}
	}
	
	public void collect(View v) {
		if(!newsContent.isCollected()) {
			newsContent.setCollected(true);
			news_content_detail_collectbtn.setImageResource(R.drawable.collect_press);
		}else{
			newsContent.setCollected(false);
			news_content_detail_collectbtn.setImageResource(R.drawable.collect);
		}
	}
}
