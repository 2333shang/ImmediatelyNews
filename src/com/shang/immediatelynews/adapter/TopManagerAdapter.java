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
import com.shang.immediatelynews.entities.Top;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopManagerAdapter extends RecyclerView.Adapter<TopManagerAdapter.ViewHolder> {

	private Context context;
	private List<Top> tops = new ArrayList<Top>();
	
	public TopManagerAdapter(Context context, List<Top> tops) {
		super();
		this.context = context;
		this.tops = tops;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView top_manager_detail_title;
		private List<ImageView> top_manager_detail_contents;
		private JCVideoPlayerStandard top_manager_detail_video;
		private LinearLayout top_manager_detail_content_head;
		private LinearLayout top_manager_detail_video_head;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			top_manager_detail_video = (JCVideoPlayerStandard) itemView.findViewById(R.id.top_manager_detail_video);
			top_manager_detail_title = (TextView) itemView.findViewById(R.id.top_manager_detail_title);
			top_manager_detail_contents = new ArrayList<ImageView>();
			ImageView top_manager_detail_content_1 = (ImageView) itemView.findViewById(R.id.top_manager_detail_content_1);
			ImageView top_manager_detail_content_2 = (ImageView) itemView.findViewById(R.id.top_manager_detail_content_2);
			ImageView top_manager_detail_content_3 = (ImageView) itemView.findViewById(R.id.top_manager_detail_content_3);
			top_manager_detail_contents.add(top_manager_detail_content_1);
			top_manager_detail_contents.add(top_manager_detail_content_2);
			top_manager_detail_contents.add(top_manager_detail_content_3);
			top_manager_detail_content_head = (LinearLayout) itemView.findViewById(R.id.top_manager_detail_content_head);
			top_manager_detail_video_head = (LinearLayout) itemView.findViewById(R.id.top_manager_detail_video_head);
		}
	}

	@Override
	public int getItemCount() {
		return tops.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int postiopn) {
		Top top = tops.get(postiopn);
		viewHolder.top_manager_detail_title.setText(top.getContent().getTitle());
		if("0".equals(top.getContent().getNewsType())){
			List<Attachment> pics = top.getContent().getPics();
			if(!pics.isEmpty()) {
				viewHolder.top_manager_detail_content_head.setVisibility(View.VISIBLE);
				for(int i=0; i<pics.size(); i++) {
					final ImageView imageView = viewHolder.top_manager_detail_contents.get(i);
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
			viewHolder.top_manager_detail_video_head.setVisibility(View.VISIBLE);
			viewHolder.top_manager_detail_video.setUp(Environment.getExternalStorageDirectory()+"/shenxiaoai.mp4", top.getContent()	.getTitle());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_top_manager_detail_recyclerview, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, tops.get(position));
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