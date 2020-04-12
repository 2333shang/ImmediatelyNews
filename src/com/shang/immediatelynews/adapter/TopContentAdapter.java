package com.shang.immediatelynews.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.NewsContent;
import com.shang.immediatelynews.entities.Top;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopContentAdapter extends RecyclerView.Adapter<TopContentAdapter.ViewHolder>{
	
	private List<Top> topNews = new ArrayList<Top>();
	private Context context;
	
	public TopContentAdapter() {
		super();
	}

	public TopContentAdapter(Context context, List<Top> topNews) {
		this.context = context;
		this.topNews = topNews;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView top_content_content;
		private TextView top_user;
		private TextView top_time;
		private LinearLayout top_linearlayout;
		private List<ImageView> top_content_view;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			top_content_content = (TextView) itemView.findViewById(R.id.top_content_content);
			top_user = (TextView) itemView.findViewById(R.id.top_user);
			top_time = (TextView) itemView.findViewById(R.id.top_time);
			top_linearlayout = (LinearLayout) itemView.findViewById(R.id.top_linearlayout);
			ImageView top_content_view1 = (ImageView) itemView.findViewById(R.id.top_content_view1);
			ImageView top_content_view2 = (ImageView) itemView.findViewById(R.id.top_content_view2);
			ImageView top_content_view3 = (ImageView) itemView.findViewById(R.id.top_content_view3);
			top_content_view = new ArrayList<ImageView>();
			top_content_view.add(top_content_view1);
			top_content_view.add(top_content_view2);
			top_content_view.add(top_content_view3);
		}
		
	}

	@Override
	public int getItemCount() {
		return topNews.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Top top = topNews.get(position);
		holder.top_content_content.setText(top.getContent().getTitle());
		holder.top_user.setText(top.getContent().getAuthorName());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		holder.top_time.setText(dateFormat.format(top.getContent().getSendtime()));
		List<Attachment> pics = top.getContent().getPics();
		if(!pics.isEmpty()) {
			holder.top_linearlayout.setVisibility(View.VISIBLE);
			for(int i=0; i<pics.size(); i++) {
				final ImageView imageView = holder.top_content_view.get(i);
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
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.top_content_layout, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, topNews.get(position));
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
