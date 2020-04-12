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
import com.shang.immediatelynews.entities.Collect;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.entities.Order;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder>{

	private List<Collect> collects = new ArrayList<Collect>();
	private Context context;
	private String type;
	
	public CollectAdapter(Context context, List<Collect> collects) {
		super();
		this.context = context;
		this.collects = collects;
	}

	@Override
	public int getItemCount() {
		return collects.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView collect_detail_title;
		private List<ImageView> collect_detail_contents;
		private JCVideoPlayerStandard collect_detail_video;
		private LinearLayout collect_detail_content_head;
		private LinearLayout collect_detail_video_head;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			collect_detail_video = (JCVideoPlayerStandard) itemView.findViewById(R.id.collect_detail_video);
			collect_detail_title = (TextView) itemView.findViewById(R.id.collect_detail_title);
			collect_detail_contents = new ArrayList<ImageView>();
			ImageView collect_detail_content_1 = (ImageView) itemView.findViewById(R.id.collect_detail_content_1);
			ImageView collect_detail_content_2 = (ImageView) itemView.findViewById(R.id.collect_detail_content_2);
			ImageView collect_detail_content_3 = (ImageView) itemView.findViewById(R.id.collect_detail_content_3);
			collect_detail_contents.add(collect_detail_content_1);
			collect_detail_contents.add(collect_detail_content_2);
			collect_detail_contents.add(collect_detail_content_3);
			collect_detail_content_head = (LinearLayout) itemView.findViewById(R.id.collect_detail_content_head);
			collect_detail_video_head = (LinearLayout) itemView.findViewById(R.id.collect_detail_video_head);
		}
		
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
		Collect collect = collects.get(position);
		Content content = collect.getContent();
		viewHolder.collect_detail_title.setText(content.getTitle());
		if("0".equals(content.getNewsType())){
			List<Attachment> pics = content.getPics();
			if(!pics.isEmpty()) {
				viewHolder.collect_detail_content_head.setVisibility(View.VISIBLE);
				for(int i=0; i<pics.size(); i++) {
					final ImageView imageView = viewHolder.collect_detail_contents.get(i);
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
			viewHolder.collect_detail_video_head.setVisibility(View.VISIBLE);
			viewHolder.collect_detail_video.setUp(Environment.getExternalStorageDirectory()+"/shenxiaoai.mp4", content.getTitle());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_collect_detail, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, collects.get(position));
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
