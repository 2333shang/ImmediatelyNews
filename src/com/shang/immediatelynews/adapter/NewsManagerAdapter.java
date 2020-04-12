package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Content;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsManagerAdapter extends RecyclerView.Adapter<NewsManagerAdapter.ViewHolder> {

	private Context context;
	private String newsType;
	private List<Content> contents = new ArrayList<Content>();
	
	public NewsManagerAdapter(Context context, String newsType, List<Content> coontents) {
		super();
		this.context = context;
		this.contents = coontents;
		this.newsType = newsType;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView news_manager_detail_title;
		private List<ImageView> news_manager_detail_contents;
		private JCVideoPlayerStandard news_manager_detail_video;
		private LinearLayout news_manager_detail_content_head;
		private LinearLayout news_manager_detail_video_head;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			news_manager_detail_video = (JCVideoPlayerStandard) itemView.findViewById(R.id.news_manager_detail_video);
			news_manager_detail_title = (TextView) itemView.findViewById(R.id.news_manager_detail_title);
			news_manager_detail_contents = new ArrayList<ImageView>();
			ImageView news_manager_detail_content_1 = (ImageView) itemView.findViewById(R.id.news_manager_detail_content_1);
			ImageView news_manager_detail_content_2 = (ImageView) itemView.findViewById(R.id.news_manager_detail_content_2);
			ImageView news_manager_detail_content_3 = (ImageView) itemView.findViewById(R.id.news_manager_detail_content_3);
			news_manager_detail_contents.add(news_manager_detail_content_1);
			news_manager_detail_contents.add(news_manager_detail_content_2);
			news_manager_detail_contents.add(news_manager_detail_content_3);
			news_manager_detail_content_head = (LinearLayout) itemView.findViewById(R.id.news_manager_detail_content_head);
			news_manager_detail_video_head = (LinearLayout) itemView.findViewById(R.id.news_manager_detail_video_head);
		}
	}

	@Override
	public int getItemCount() {
		return contents.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int postiopn) {
		Content content = contents.get(postiopn);
		viewHolder.news_manager_detail_title.setText(content.getTitle());
		if("0".equals(newsType)){
			List<Attachment> pics = content.getPics();
			if(!pics.isEmpty()) {
				viewHolder.news_manager_detail_content_head.setVisibility(View.VISIBLE);
				for(int i=0; i<pics.size(); i++) {
					final ImageView imageView = viewHolder.news_manager_detail_contents.get(i);
					Glide.with(context).load(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + pics.get(i).getUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
						
						@Override
						public void onResourceReady(Bitmap bm, GlideAnimation<? super Bitmap> arg1) {
							imageView.setImageBitmap(bm);
						}
					});
					if(i == 2) {
						break;
					}
				}
			}
		}else{
			viewHolder.news_manager_detail_video_head.setVisibility(View.VISIBLE);
			viewHolder.news_manager_detail_video.setUp(Environment.getExternalStorageDirectory()+"/shenxiaoai.mp4", content.getTitle());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_news_manager_detail_recyclerview, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, contents.get(position));
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