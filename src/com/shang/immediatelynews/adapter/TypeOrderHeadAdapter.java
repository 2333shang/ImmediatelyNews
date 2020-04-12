package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.gson.internal.LinkedTreeMap;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.Order;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TypeOrderHeadAdapter extends RecyclerView.Adapter<TypeOrderHeadAdapter.ViewHolder>{
	
	private List<Order> orders = new ArrayList<Order>();
	
	public TypeOrderHeadAdapter(List<Order> orders) {
		super();
		this.orders = orders;
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
		return orders.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Order order = (Order) orders.get(position);
		Log.d("news", "order=" + order.toString());
		String image = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
		Log.d("news", image);
		holder.order_head_user.setText(order.getCompany().getCompanyName());
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
				Object data = orders.get(position);
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
