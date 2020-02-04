package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.NewsComments;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class VideoCommentAdapter extends RecyclerView.Adapter<VideoCommentAdapter.ViewHolder>{
	
	private List<NewsComments> comments = new ArrayList<NewsComments>();
	private Activity activity;
	
	{
		for(int i = 0; i<7 ; i++) {
			String videoUserImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
			String comment = "comment" + i;
			String username = "username" + i;
			NewsComments videocomment = new NewsComments(videoUserImageUrl, comment, username);
			comments.add(videocomment);
		}
	}
	
	public VideoCommentAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	public List<NewsComments> getComments() {
		return comments;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView video_content_detail_comment_user;
		private CircleImageView video_detail_comment_image;
		private TextView video_content_detail_comment;
		private ImageView video_content_detail_comment_praise;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			video_content_detail_comment_user = (TextView) itemView.findViewById(R.id.video_content_detail_comment_user);
			video_detail_comment_image = (CircleImageView) itemView.findViewById(R.id.video_detail_comment_image);
			video_content_detail_comment = (TextView) itemView.findViewById(R.id.video_content_detail_comment);
			video_content_detail_comment_praise = (ImageView) itemView.findViewById(R.id.video_content_detail_comment_praise);
		}
		
	}

	@Override
	public int getItemCount() {
		return comments.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		NewsComments videocomment = comments.get(position);
		holder.video_content_detail_comment.setText(videocomment.getComment());
		holder.video_content_detail_comment_user.setText(videocomment.getUsername());
		Glide.with(activity).load(videocomment.getNewsUserImageUrl()).into(holder.video_detail_comment_image);
		holder.video_content_detail_comment_praise.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Animation loadAnimation = AnimationUtils.loadAnimation(activity, R.anim.order_btn_ani);
				holder.video_content_detail_comment_praise.startAnimation(loadAnimation);
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_video_comment, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				NewsComments data = comments.get(position);
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
