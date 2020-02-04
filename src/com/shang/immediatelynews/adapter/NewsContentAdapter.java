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

public class NewsContentAdapter extends RecyclerView.Adapter<NewsContentAdapter.ViewHolder>{
	
	private List<NewsComments> comments = new ArrayList<NewsComments>();
	private Activity activity;
	
	{
		String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
		for(int i = 0; i<7 ; i++) {
			NewsComments newsComments = new NewsComments(absolutePath, "comment" + i, "username" + i);
			comments.add(newsComments);
		}
	}
	
	public List<NewsComments> getComments() {
		return comments;
	}

	public NewsContentAdapter() {
		super();
	}
	
	public NewsContentAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private CircleImageView news_detail_comment_image;
		private TextView news_detail_comment_user;
		private TextView news_detail_comment;
		private ImageView news_detail_comment_praise;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			news_detail_comment_image = (CircleImageView) itemView.findViewById(R.id.news_detail_comment_image);
			news_detail_comment_user = (TextView) itemView.findViewById(R.id.news_detail_comment_user);
			news_detail_comment = (TextView) itemView.findViewById(R.id.news_detail_comment);
			news_detail_comment_praise = (ImageView) itemView.findViewById(R.id.news_detail_comment_praise);
		}
	}

	@Override
	public int getItemCount() {
		return comments.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		NewsComments newsComments = comments.get(position);
		holder.news_detail_comment_user.setText(newsComments.getUsername());
		holder.news_detail_comment.setText(newsComments.getComment());
		Glide.with(activity).load(newsComments.getNewsUserImageUrl()).into(holder.news_detail_comment_image);
		holder.news_detail_comment_praise.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Animation loadAnimation = AnimationUtils.loadAnimation(activity, R.anim.order_btn_ani);
				holder.news_detail_comment_praise.startAnimation(loadAnimation);
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.news_content_detail_layout, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, comments.get(position));
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
