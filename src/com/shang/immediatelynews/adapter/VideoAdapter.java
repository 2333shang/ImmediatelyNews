package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.GlideUtils;

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
		private ImageView video_content_video;
		private TextView video_content_content;
		private TextView video_content_title;
		private TextView video_content_companyname;
		private CircleImageView video_user_image;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			video_content_video = (ImageView) itemView.findViewById(R.id.video_content_video);
			video_content_content = (TextView) itemView.findViewById(R.id.video_content_content);
			video_content_title = (TextView) itemView.findViewById(R.id.video_content_title);
			video_content_companyname = (TextView) itemView.findViewById(R.id.video_content_companyname);
			video_user_image = (CircleImageView) itemView.findViewById(R.id.video_user_image);
		}
		
	}

	@Override
	public int getItemCount() {
		return videos.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		Content video = videos.get(position);
		List<Attachment> pics = video.getPics();
		boolean hasPic = false;
		holder.video_content_video.setBackgroundResource(R.drawable.news);
		for(Attachment a:pics) {
			if(a.getAttachmentType().equals("2")) {
				hasPic = true;
				GlideUtils.loadBackgroupImage(activity, holder.video_content_video, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + a.getUrl());
				break;
			}
		}
		if(!hasPic) {
			GlideUtils.loadBackgroupImage(activity, holder.video_content_video, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + FileUploadConstant.FILE_DEFAULT_HEAD);
		}
		holder.video_content_content.setText(video.getAuthorName());
		holder.video_content_title.setText(video.getTitle());
		holder.video_content_companyname.setText(video.getCompanyName());
		GlideUtils.loadImage(activity, holder.video_user_image, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + video.getUser().getHeadIcon().getUrl());
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
