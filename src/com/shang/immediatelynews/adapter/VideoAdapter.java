package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.Content;

import android.app.Activity;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
	
	private List<Content> videos = new ArrayList<Content>();
	private Activity activity;
	
	public VideoAdapter(Activity activity, List<Content> videos) {
		super();
		this.activity = activity;
		this.videos = videos;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private JCVideoPlayerStandard video_content_video;
		private TextView video_content_content;
		private CircleImageView video_user_image;
		private TextView video_content_order;
		private ImageView video_content_comment;
		private TextView video_content_comment_text;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			video_content_video = (JCVideoPlayerStandard) itemView.findViewById(R.id.video_content_video);
			video_content_content = (TextView) itemView.findViewById(R.id.video_content_content);
			video_user_image = (CircleImageView) itemView.findViewById(R.id.video_user_image);
			video_content_order = (TextView) itemView.findViewById(R.id.video_content_order);
			video_content_comment = (ImageView) itemView.findViewById(R.id.video_cotent_comment);
			video_content_comment_text = (TextView) itemView.findViewById(R.id.video_cotent_comment_text);
		}
		
	}

	@Override
	public int getItemCount() {
		return videos.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		Content video = videos.get(position);
		Log.d("news", video.toString());
		final String content = video.getAuthorName();
		holder.video_content_video.setUp(Environment.getExternalStorageDirectory() + "/shenxiaoai.mp4" , content);
		holder.video_content_video.thumbImageView.setImageResource(R.drawable.first);
		holder.video_content_content.setText(content);
		holder.video_user_image.setImageResource(R.drawable.first);
		holder.video_content_order.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("关注".equals(holder.video_content_order.getText())) {
					holder.video_content_order.setText("已关注");
					holder.video_content_order.setTextColor(Color.BLACK);
				}else {
					holder.video_content_order.setText("关注");
					holder.video_content_order.setTextColor(Color.RED);
				}
			}
		});
		holder.video_content_comment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(v.getContext(), VideoContentActivity.class);
//				intent.putExtra("video", videos.get(position));
//				v.getContext().startActivity(intent);
//				activity.overridePendingTransition(R.anim.video_content_detail_right_in, R.anim.video_content_detail_alpha_dismiss);
			}
		});
		holder.video_content_comment_text.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(v.getContext(), VideoContentActivity.class);
//				intent.putExtra("video", videos.get(position));
//				v.getContext().startActivity(intent);
//				activity.overridePendingTransition(R.anim.video_content_detail_right_in, R.anim.video_content_detail_alpha_dismiss);
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.video_content_layout, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, videos.get(position));
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
