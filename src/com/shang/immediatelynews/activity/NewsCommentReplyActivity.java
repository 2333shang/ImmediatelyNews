package com.shang.immediatelynews.activity;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.adapter.OnRecyclerViewItemClickListener;
import com.shang.immediatelynews.adapter.VideoCommentReplyAdapter;
import com.shang.immediatelynews.decoration.DividerListItemDecoration;
import com.shang.immediatelynews.entities.NewsCommentReplies;
import com.shang.immediatelynews.entities.NewsComments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@ContentView(R.layout.activity_video_comment_reply)
public class NewsCommentReplyActivity extends AppCompatActivity {

	@ViewInject(R.id.video_content_detail_comment_user)
	private TextView video_content_detail_comment_user;
	@ViewInject(R.id.video_detail_comment_image)
	private ImageView video_detail_comment_image;
	@ViewInject(R.id.video_content_detail_comment)
	private TextView video_content_detail_comment;
	@ViewInject(R.id.video_detail_reply_recyclerview)
	private RecyclerView video_detail_reply_recyclerview;
	@ViewInject(R.id.video_reply_send_layout)
	private LinearLayout video_reply_send_layout;
	@ViewInject(R.id.video_reply_send_edit)
	private EditText video_reply_send_edit;
/*	@ViewInject(R.id.video_content_detail_phraisebtn)
	private ImageView video_content_detail_phraisebtn;
	@ViewInject(R.id.video_content_detail_collectbtn)
	private ImageView video_content_detail_collectbtn;*/
	private VideoCommentReplyAdapter videoCommentReplyAdapter;
	private int i = 7;
	private NewsComments comments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		Intent intent = getIntent();
		comments = (NewsComments) intent.getSerializableExtra("data");
		video_content_detail_comment_user.setText(comments.getUsername());
		video_detail_comment_image.setImageBitmap(BitmapFactory.decodeFile(comments.getNewsUserImageUrl()));
		video_content_detail_comment.setText(comments.getComment());
		videoCommentReplyAdapter = new VideoCommentReplyAdapter(this);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		video_detail_reply_recyclerview.setLayoutManager(layoutManager);
		video_detail_reply_recyclerview.setAdapter(videoCommentReplyAdapter);
		video_detail_reply_recyclerview.addItemDecoration(new DividerListItemDecoration(this, DividerListItemDecoration.VERTICAL_LIST));
		//设置动画
		video_detail_reply_recyclerview.setItemAnimator(new DefaultItemAnimator());
		//设置点击事件
		videoCommentReplyAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View v, int position, Object data) {
				video_reply_send_layout.setVisibility(View.GONE);
				hideInput();
				/*if(popupWindow.isShowing()) {
					popupWindow.dismiss();//只是不显示了, 但对象仍然在内存中
				}*/
			}
		});
		video_detail_reply_recyclerview.setAdapter(videoCommentReplyAdapter);
	}
	
	public void addReplies(View v) {
		video_reply_send_layout.setVisibility(View.VISIBLE);
	}
	
	public void sendReplies(View v) {
		String reply = video_reply_send_edit.getText().toString();
		video_reply_send_edit.setText("");
		String username = "username" + i++;
		NewsCommentReplies videoCommentReplies = new NewsCommentReplies(reply, username);
		videoCommentReplyAdapter.getReplies().add(videoCommentReplies);
		video_detail_reply_recyclerview.scrollToPosition(videoCommentReplyAdapter.getItemCount()-1);
		video_reply_send_layout.setVisibility(View.GONE);
		hideInput();
	}
	
	public void cancelComments(View v) {
		if(video_reply_send_layout.getVisibility() == View.VISIBLE) {
			video_reply_send_layout.setVisibility(View.GONE);
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
/*	
	public void phraise(View v) {
		if(!comments.isPhraised()) {
			comments.setPhraised(true);
			video_content_detail_phraisebtn.setImageResource(R.drawable.phraise_press);
		}else{
			comments.setPhraised(false);
			video_content_detail_phraisebtn.setImageResource(R.drawable.phraise);
		}
	}
	
	public void collect(View v) {
		if(!comments.isCollected()) {
			comments.setCollected(true);
			video_content_detail_collectbtn.setImageResource(R.drawable.collect_press);
		}else{
			comments.setCollected(false);
			video_content_detail_collectbtn.setImageResource(R.drawable.collect);
		}
	}*/
}
