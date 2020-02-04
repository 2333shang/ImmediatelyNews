package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.NewsContent;
import com.shang.immediatelynews.entities.Videos;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TopContentAdapter extends RecyclerView.Adapter<TopContentAdapter.ViewHolder>{
	
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
	
	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
//		private ImageView gridimage;
		private TextView top_content_content;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
//			gridimage = (ImageView) itemView.findViewById(R.id.gridimage);
			top_content_content = (TextView) itemView.findViewById(R.id.top_content_content);
		}
		
	}

	@Override
	public int getItemCount() {
		return newsContents.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		NewsContent newsContent = newsContents.get(position);
		holder.top_content_content.setText(newsContent.getTitle());
//		holder.gridimage.setImageResource(R.drawable.ic_launcher);
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
