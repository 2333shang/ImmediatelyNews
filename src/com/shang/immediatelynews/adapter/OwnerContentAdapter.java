package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.Content;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OwnerContentAdapter extends RecyclerView.Adapter<OwnerContentAdapter.ViewHolder>{
	
	private List<Content> contents = new ArrayList<Content>();
	
	public OwnerContentAdapter() {
		super();
	}
	
	public OwnerContentAdapter(List<Content> contents) {
		this.contents = contents;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView owner_content_content;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			owner_content_content = (TextView) itemView.findViewById(R.id.owner_content_content);
		}
		
	}

	@Override
	public int getItemCount() {
		return contents.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Content content = contents.get(position);
		holder.owner_content_content.setText(content.getTitle());
//		holder.gridimage.setImageResource(R.drawable.ic_launcher);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.owner_content_layout, null);
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
