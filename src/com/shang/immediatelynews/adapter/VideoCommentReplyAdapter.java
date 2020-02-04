package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.NewsCommentReplies;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VideoCommentReplyAdapter extends RecyclerView.Adapter<VideoCommentReplyAdapter.ViewHolder>{
	
	private List<NewsCommentReplies> replies = new ArrayList<NewsCommentReplies>();
	private Activity activity;
	
	{
		for(int i = 0; i<7 ; i++) {
			String reply = "reply" + i;
			String username = "replyusername" + i;
			NewsCommentReplies videoreply = new NewsCommentReplies(reply, username);
			replies.add(videoreply);
		}
	}
	
	public VideoCommentReplyAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	public List<NewsCommentReplies> getReplies() {
		return replies;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView video_comment_reply_user;
		private TextView video_comment_reply_content;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			video_comment_reply_user = (TextView) itemView.findViewById(R.id.video_comment_reply_user);
			video_comment_reply_content = (TextView) itemView.findViewById(R.id.video_comment_reply_content);
		}
		
	}

	@Override
	public int getItemCount() {
		return replies.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		NewsCommentReplies videoCommentReplies = replies.get(position);
		holder.video_comment_reply_user.setText(videoCommentReplies.getUsername());
		holder.video_comment_reply_content.setText(videoCommentReplies.getReply());
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_video_comment_reply_content, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				NewsCommentReplies data = replies.get(position);
				if(itemListener != null) {
					itemListener.onItemClick(v, position, data);
				}
			}
		});
		return viewHolder;
	}

	private OnRecyclerViewItemClickListener itemListener;

	public void setOnItemClickListener(OnRecyclerViewItemClickListener itemListener) {
		this.itemListener = itemListener;
	}

}
