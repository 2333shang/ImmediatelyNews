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
import com.shang.immediatelynews.entities.Company;
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

public class OrderContentDetailAdapter extends RecyclerView.Adapter<OrderContentDetailAdapter.ViewHolder> {

	private Context context;
	private String newsType;
	private Company company;
	private List<Content> contents = new ArrayList<Content>();
	
	public OrderContentDetailAdapter(Context context, String newsType, Company company) {
		super();
		this.context = context;
		this.company = company;
		this.newsType = newsType;
		if("0".equals(newsType)){
			this.contents = company.getContent();
		}else {
			this.contents = company.getVideoContent();
		}
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView order_content_detail_title;
		private List<ImageView> order_content_detail_contents;
		private JCVideoPlayerStandard order_content_detail_video;
		private LinearLayout order_content_detail_content_head;
		private LinearLayout order_content_detail_video_head;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			order_content_detail_video = (JCVideoPlayerStandard) itemView.findViewById(R.id.order_content_detail_video);
			order_content_detail_title = (TextView) itemView.findViewById(R.id.order_content_detail_title);
			order_content_detail_contents = new ArrayList<ImageView>();
			ImageView order_content_detail_content_1 = (ImageView) itemView.findViewById(R.id.order_content_detail_content_1);
			ImageView order_content_detail_content_2 = (ImageView) itemView.findViewById(R.id.order_content_detail_content_2);
			ImageView order_content_detail_content_3 = (ImageView) itemView.findViewById(R.id.order_content_detail_content_3);
			order_content_detail_contents.add(order_content_detail_content_1);
			order_content_detail_contents.add(order_content_detail_content_2);
			order_content_detail_contents.add(order_content_detail_content_3);
			order_content_detail_content_head = (LinearLayout) itemView.findViewById(R.id.order_content_detail_content_head);
			order_content_detail_video_head = (LinearLayout) itemView.findViewById(R.id.order_content_detail_video_head);
		}
	}

	@Override
	public int getItemCount() {
		return contents.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int postiopn) {
		Content content = contents.get(postiopn);
		viewHolder.order_content_detail_title.setText(content.getTitle());
		if("0".equals(newsType)){
			List<Attachment> pics = content.getPics();
			if(!pics.isEmpty()) {
				viewHolder.order_content_detail_content_head.setVisibility(View.VISIBLE);
				for(int i=0; i<pics.size(); i++) {
					final ImageView imageView = viewHolder.order_content_detail_contents.get(i);
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
			viewHolder.order_content_detail_video_head.setVisibility(View.VISIBLE);
			viewHolder.order_content_detail_video.setUp(Environment.getExternalStorageDirectory()+"/shenxiaoai.mp4", content.getTitle());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_order_cotent_detail_recyclerview, null);
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