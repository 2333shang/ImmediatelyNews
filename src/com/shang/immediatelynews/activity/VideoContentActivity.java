package com.shang.immediatelynews.activity;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.VideoCommentAdapter;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.NewsComments;
import com.shang.immediatelynews.entities.Videos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.activity_video_content)
public class VideoContentActivity extends AppCompatActivity {

	@ViewInject(R.id.video_content_detail_video)
	private JCVideoPlayerStandard video_content;
	@ViewInject(R.id.video_content_detail_user)
	private TextView video_content_detail_user;
	@ViewInject(R.id.video_content_detail_fans)
	private TextView video_content_detail_fans;
	@ViewInject(R.id.video_detail_user_image)
	private ImageView video_detail_user_image;
	@ViewInject(R.id.video_content_detail_order)
	private Button video_content_detail_order;
	@ViewInject(R.id.video_detail_recyclerview)
	private RecyclerView video_detail_recyclerview;
	@ViewInject(R.id.video_comment_send_layout)
	private LinearLayout video_comment_send_layout;
	@ViewInject(R.id.video_comment_send_edit)
	private EditText video_comment_send_edit;
	@ViewInject(R.id.video_content_detail_phraisebtn)
	private ImageView video_content_detail_phraisebtn;
	@ViewInject(R.id.video_content_detail_collectbtn)
	private ImageView video_content_detail_collectbtn;
	private VideoCommentAdapter videoCommentAdapter;
	private int i = 7;
	private Videos video;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		Intent intent = getIntent();
		video = (Videos) intent.getSerializableExtra("video");
		video_content.setUp(video.getVideoUrl(), video.getVideoUserName());
		video_content_detail_user.setText(video.getVideoUserName());
		video_detail_user_image.setImageResource(R.drawable.first);
		video_content_detail_fans.setText(video.getFans() + "粉丝");
		video_content_detail_order.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("关注".equals(video_content_detail_order.getText())) {
					video_content_detail_order.setBackgroundResource(R.drawable.ordered_button_custom);
					video_content_detail_order.setText("已关注");
				}else {
					video_content_detail_order.setBackgroundResource(R.drawable.order_button_custom);
					video_content_detail_order.setText("关注");
				}
			}
		});
		videoCommentAdapter = new VideoCommentAdapter(this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		video_detail_recyclerview.setLayoutManager(layoutManager);
		video_detail_recyclerview.setAdapter(videoCommentAdapter);
		video_detail_recyclerview.addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		video_detail_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		videoCommentAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				video_comment_send_layout.setVisibility(View.GONE);
				hideInput();
				Intent intent = new Intent(VideoContentActivity.this, NewsCommentReplyActivity.class);
				intent.putExtra("data", (NewsComments)data);
				startActivity(intent);
			}
		});
		video_detail_recyclerview.setAdapter(videoCommentAdapter);
	}
	
	public void addComments(View v) {
		video_comment_send_layout.setVisibility(View.VISIBLE);
	}
	
	public void sendComments(View v) {
		String comment = video_comment_send_edit.getText().toString();
		video_comment_send_edit.setText("");
		String videoUserImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
		String username = "username" + i++;
		NewsComments videocomment = new NewsComments(videoUserImageUrl, comment, username);
		videoCommentAdapter.getComments().add(videocomment);
		video_detail_recyclerview.scrollToPosition(videoCommentAdapter.getItemCount()-1);
		video_comment_send_layout.setVisibility(View.GONE);
		hideInput();
	}
	
	public void phraise(View v) {
		if(!video.isPhraised()) {
			video.setPhraised(true);
			video_content_detail_phraisebtn.setImageResource(R.drawable.phraise_press);
		}else{
			video.setPhraised(false);
			video_content_detail_phraisebtn.setImageResource(R.drawable.phraise);
		}
	}
	
	public void collect(View v) {
		if(!video.isCollected()) {
			video.setCollected(true);
			video_content_detail_collectbtn.setImageResource(R.drawable.collect_press);
		}else{
			video.setCollected(false);
			video_content_detail_collectbtn.setImageResource(R.drawable.collect);
		}
	}
	
	public void cancelComments(View v) {
		if(video_comment_send_layout.getVisibility() == View.VISIBLE) {
			video_comment_send_layout.setVisibility(View.GONE);
			hideInput();
		}
	}
	
    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
