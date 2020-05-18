package com.shang.immediatelynews.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.CompanyActivity;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Company;
import com.shang.immediatelynews.utils.GlideUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder>{
	
	private List<Company> company = new ArrayList<Company>();
	private Context context;
	
	public CompanyAdapter() {
		super();
	}
	
	public CompanyAdapter(Context context, List<Company> company) {
		this.company = company;
		this.context = context;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView order_detail_companyname;
		private TextView order_detail_content_creater;
		private TextView order_detail_content_createtime;
		private CircleImageView order_detail_headicon;
		private Button order_btn;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			order_detail_companyname = (TextView) itemView.findViewById(R.id.order_detail_companyname);
			order_detail_content_creater = (TextView) itemView.findViewById(R.id.order_detail_content_creater);
			order_detail_content_createtime = (TextView) itemView.findViewById(R.id.order_detail_content_createtime);
			order_detail_headicon = (CircleImageView) itemView.findViewById(R.id.order_detail_headicon);
			order_btn = (Button) itemView.findViewById(R.id.order_btn);
		}
		
	}

	@Override
	public int getItemCount() {
		return company.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
		final Company c = company.get(position);
		viewHolder.order_detail_companyname.setText(c.getCompanyName());
		viewHolder.order_detail_content_creater.setText(c.getCreater());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		viewHolder.order_detail_content_createtime.setText(format.format(c.getCreatetime()));
//		Glide.with(context).load(Environment.getExternalStorageDirectory() + "/first.jpg").into(viewHolder.order_detail_headicon);
		GlideUtils.loadImage(context, viewHolder.order_detail_headicon, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + c.getHeadIcon().getUrl());
		if(c.getOrder() == null || "0".equals(c.getOrder().getOrderFlag())) {
			viewHolder.order_btn.setText("+关注");
		}else {
			viewHolder.order_btn.setText("取消关注");
		}
		viewHolder.order_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = "";
				if("取消关注".equals(viewHolder.order_btn.getText())) {
					url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/cancelorder?companyId=" + c.getId();
				}else {
					url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/order/order?companyId=" + c.getId();
				}
				HttpRequestUtils.getRequest(url, new Callback() {
					
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						String object = response.body().string();
						if("login_invalid".equals(object)) {
							NetworkUtils.toSessionInvalid((Activity)context);
						}else {
							((Activity) context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									if("取消关注".equals(viewHolder.order_btn.getText())) {
										viewHolder.order_btn.setText("+关注");
									}else {
										viewHolder.order_btn.setText("取消关注");
									}
								}
							});
						}
					}
					
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						CompanyActivity activity = (CompanyActivity) context;
						NetworkUtils.showErrorMessage(activity, activity.getMessage());
					}
				});
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
					itemListener.onItemClick(v, position, company.get(position));
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
