package com.shang.immediatelynews.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.entities.Order;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

	private Context context;
	private List<Order> orders = new ArrayList<Order>();
	
	public OrderAdapter(Context context, List<Order> orders) {
		super();
		this.context = context;
		this.orders = orders;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView order_detail_companyname;
		private TextView order_detail_content_title;
		private CircleImageView order_detail_headicon;
		private Button order_btn;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			order_detail_companyname = (TextView) itemView.findViewById(R.id.order_detail_companyname);
			order_detail_content_title = (TextView) itemView.findViewById(R.id.order_detail_content_title);
			order_detail_headicon = (CircleImageView) itemView.findViewById(R.id.order_detail_headicon);
			order_btn = (Button) itemView.findViewById(R.id.order_btn);
		}
	}

	@Override
	public int getItemCount() {
		return orders.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int postiopn) {
		Order order = orders.get(postiopn);
		viewHolder.order_detail_companyname.setText(order.getCompany().getCompanyName());
		viewHolder.order_detail_content_title.setText("最新新闻！");
		Glide.with(context).load(Environment.getExternalStorageDirectory() + "/first.jpg").into(viewHolder.order_detail_headicon);
		viewHolder.order_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button btn = (Button)v;
				if(btn.getText().toString().equals("已关注")) {
					btn.setText("+关注");
				}else {
					btn.setText("已关注");
				}
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_order_detail, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, orders.get(position));
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