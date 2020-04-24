package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.Order;
import com.shang.immediatelynews.utils.GlideUtils;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TypeOrderHeadAdapter extends RecyclerView.Adapter<TypeOrderHeadAdapter.ViewHolder>{
	
	private List<Order> orders = new ArrayList<Order>();
	private Context context;
	
	public TypeOrderHeadAdapter(Context context, List<Order> orders) {
		super();
		this.orders = orders;
		this.context = context;
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
		return orders.size() + 1;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if(position == orders.size()) {
			String image = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
			GlideUtils.loadImage(context, holder.order_head_recyclerview_image, image);
//			Glide.with(holder.view.getContext()).load(image).into(holder.order_head_recyclerview_image);
			holder.order_head_user.setText("关注更多");
		}else {
			Order order = (Order) orders.get(position);
			String image = Environment.getExternalStorageDirectory().getAbsolutePath() + "/first.jpg";
			holder.order_head_user.setText(order.getCompany().getCompanyName());
			GlideUtils.loadImage(context, holder.order_head_recyclerview_image, image);
//			Glide.with(holder.view.getContext()).load(image).into(holder.order_head_recyclerview_image);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.order_head_layout, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(position != orders.size()) {
					Object data = orders.get(position);
					if(itemListener != null) {
						itemListener.onItemClick(v, position, data);
					}
				}else {
					if(itemListener != null) {
						itemListener.onItemClick(v, position, null);
					}
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
