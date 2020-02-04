package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ViewInject;

import com.bumptech.glide.Glide;
import com.shang.immediatelynews.R;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderHeadAdapter extends RecyclerView.Adapter<OrderHeadAdapter.ViewHolder>{
	
	private List<String> images = new ArrayList<String>();
	
	{
		String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
		for(int i = 0; i<7 ; i++) {
			images.add(absolutePath);
		}
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private CircleImageView order_head_recyclerview_image;
		private TextView order_head_user;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			order_head_recyclerview_image = (CircleImageView) itemView.findViewById(R.id.order_head_recyclerview_image);
			order_head_user = (TextView) itemView.findViewById(R.id.order_head_user);
		}
		
	}

	@Override
	public int getItemCount() {
		return images.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String image = images.get(position);
		holder.order_head_user.setText("user" + position);
		Glide.with(holder.view.getContext()).load(image).into(holder.order_head_recyclerview_image);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.order_head_layout, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				String data = images.get(position);
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
