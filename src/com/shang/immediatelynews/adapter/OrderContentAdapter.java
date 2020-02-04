package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ViewInject;

import com.bumptech.glide.Glide;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.NewsContent;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderContentAdapter extends RecyclerView.Adapter<OrderContentAdapter.ViewHolder>{
	
	private List<String> images = new ArrayList<String>();
	private List<NewsContent> newsContents = new ArrayList<NewsContent>();
	
	{
		String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
		for(int i = 0; i<7 ; i++) {
			String content = "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n";
			content += "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i + "\n" + "content" + i;
			NewsContent newsContent = new NewsContent("title" + i, absolutePath, "username" + i, content);
			newsContent.setAuthorType("游戏作者");
			newsContents.add(newsContent);
		}
	}
	{
		String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
		for(int i = 0; i<7 ; i++) {
			images.add(absolutePath);
		}
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private CircleImageView order_content_recyclerview_image;
		private TextView order_content_content;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			order_content_recyclerview_image = (CircleImageView) itemView.findViewById(R.id.order_content_recyclerview_image);
			order_content_content = (TextView) itemView.findViewById(R.id.order_content_content);
		}
		
	}

	@Override
	public int getItemCount() {
		return newsContents.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String image = images.get(position);
		NewsContent newsContent = newsContents.get(position);
		holder.order_content_content.setText(newsContent.getTitle());
		Glide.with(holder.view.getContext()).load(image).into(holder.order_content_recyclerview_image);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.order_content_layout, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, newsContents.get(position));
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
