package com.shang.immediatelynews.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.GlideUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TypeContentAdapter extends RecyclerView.Adapter<TypeContentAdapter.ViewHolder>{
	
	private List<Content> contents = new ArrayList<Content>();
	private Context context;
	
	public TypeContentAdapter(Context context, List<Content> contents) {
		super();
		this.contents = contents;
		this.context = context;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView news_content_detail_title;
		private TextView news_content_detail_user;
		private TextView news_content_detail_time;
		private List<ImageView> news_content_detail_contents;
		private ImageView news_content_detail_video;
		private LinearLayout news_content_detail_content_head;
		private FrameLayout news_content_detail_video_head;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			news_content_detail_video = (ImageView) itemView.findViewById(R.id.news_content_detail_video);
			news_content_detail_title = (TextView) itemView.findViewById(R.id.news_content_detail_title);
			news_content_detail_user = (TextView) itemView.findViewById(R.id.news_content_detail_user);
			news_content_detail_time = (TextView) itemView.findViewById(R.id.news_content_detail_time);
			news_content_detail_contents = new ArrayList<ImageView>();
			ImageView news_content_detail_content_1 = (ImageView) itemView.findViewById(R.id.news_content_detail_content_1);
			ImageView news_content_detail_content_2 = (ImageView) itemView.findViewById(R.id.news_content_detail_content_2);
			ImageView news_content_detail_content_3 = (ImageView) itemView.findViewById(R.id.news_content_detail_content_3);
			news_content_detail_contents.add(news_content_detail_content_1);
			news_content_detail_contents.add(news_content_detail_content_2);
			news_content_detail_contents.add(news_content_detail_content_3);
			news_content_detail_content_head = (LinearLayout) itemView.findViewById(R.id.news_content_detail_content_head);
			news_content_detail_video_head = (FrameLayout) itemView.findViewById(R.id.news_content_detail_video_head);
		}
		
	}

	@Override
	public int getItemCount() {
		return contents.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		viewHolder.news_content_detail_content_head.setVisibility(View.GONE);
		viewHolder.news_content_detail_video_head.setVisibility(View.GONE);
		Content content = contents.get(position);
		viewHolder.news_content_detail_title.setText(content.getTitle());
		viewHolder.news_content_detail_user.setText(content.getAuthorName());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		viewHolder.news_content_detail_time.setText(format.format(content.getSendtime()));
		List<Attachment> pics = content.getPics();
		if(!pics.isEmpty()) {
			viewHolder.news_content_detail_content_head.setVisibility(View.VISIBLE);
			for(int i=0; i<viewHolder.news_content_detail_contents.size(); i++) {
				ImageView imageView = viewHolder.news_content_detail_contents.get(i);
				imageView.setVisibility(View.GONE);
			}
			for(int i=0; i<pics.size(); i++) {
				final ImageView imageView = viewHolder.news_content_detail_contents.get(i);
				imageView.setImageResource(R.drawable.news);
				GlideUtils.loadImage(context, imageView, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + pics.get(i).getUrl());
				if(i == 2) {
					break;
				}
			}
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_news_content_recyclerview_detail, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				Object data = contents.get(position);
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
