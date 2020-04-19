package com.shang.immediatelynews.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.TopManagerActivity;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Top;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TopManagerAdapter extends RecyclerView.Adapter<TopManagerAdapter.ViewHolder> {

	private Context context;
	private String type;
	private List<Top> tops = new ArrayList<Top>();
	
	public TopManagerAdapter(Context context, List<Top> tops, String type) {
		super();
		this.context = context;
		this.tops = tops;
		this.type = type;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{

		private View view;
		private TextView top_manager_detail_title;
		private TextView top_manager_detail_apply_reason;
		private List<ImageView> top_manager_detail_contents;
		private JCVideoPlayerStandard top_manager_detail_video;
		private LinearLayout top_manager_detail_content_head;
		private LinearLayout top_manager_detail_video_head;
		private RelativeLayout top_manager_detail_apply;
		private Button top_manager_detail_apply_agree;
		private Button top_manager_detail_apply_rejust;
		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			top_manager_detail_video = (JCVideoPlayerStandard) itemView.findViewById(R.id.top_manager_detail_video);
			top_manager_detail_title = (TextView) itemView.findViewById(R.id.top_manager_detail_title);
			top_manager_detail_apply_reason = (TextView) itemView.findViewById(R.id.top_manager_detail_apply_reason);
			top_manager_detail_contents = new ArrayList<ImageView>();
			ImageView top_manager_detail_content_1 = (ImageView) itemView.findViewById(R.id.top_manager_detail_content_1);
			ImageView top_manager_detail_content_2 = (ImageView) itemView.findViewById(R.id.top_manager_detail_content_2);
			ImageView top_manager_detail_content_3 = (ImageView) itemView.findViewById(R.id.top_manager_detail_content_3);
			top_manager_detail_contents.add(top_manager_detail_content_1);
			top_manager_detail_contents.add(top_manager_detail_content_2);
			top_manager_detail_contents.add(top_manager_detail_content_3);
			top_manager_detail_content_head = (LinearLayout) itemView.findViewById(R.id.top_manager_detail_content_head);
			top_manager_detail_video_head = (LinearLayout) itemView.findViewById(R.id.top_manager_detail_video_head);
			top_manager_detail_apply = (RelativeLayout) itemView.findViewById(R.id.top_manager_detail_apply);
			top_manager_detail_apply_agree = (Button) itemView.findViewById(R.id.top_manager_detail_apply_agree);
			top_manager_detail_apply_rejust = (Button) itemView.findViewById(R.id.top_manager_detail_apply_rejust);
		}
	}

	@Override
	public int getItemCount() {
		return tops.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int postiopn) {
		final Top top = tops.get(postiopn);
		viewHolder.top_manager_detail_title.setText(top.getContent().getTitle());
		viewHolder.top_manager_detail_apply_reason.setText("申请类型:" + ("0".equals(top.getResousel())?"普通头条":"轮播头条"));
		if("0".equals(type)) {
			viewHolder.top_manager_detail_apply.setVisibility(View.VISIBLE);
		}
		if("0".equals(top.getContent().getNewsType())){
			List<Attachment> pics = top.getContent().getPics();
			if(!pics.isEmpty()) {
				viewHolder.top_manager_detail_content_head.setVisibility(View.VISIBLE);
				for(int i=0; i<pics.size(); i++) {
					final ImageView imageView = viewHolder.top_manager_detail_contents.get(i);
					Glide.with(context).asBitmap().load(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + pics.get(i).getUrl()).into(new SimpleTarget<Bitmap>() {
						
						@Override
						public void onResourceReady(Bitmap bm, Transition<? super Bitmap> arg1) {
							imageView.setImageBitmap(bm);
						}
					});
					if(i == 2) {
						break;
					}
				}
			}
		}else{
			viewHolder.top_manager_detail_video_head.setVisibility(View.VISIBLE);
			viewHolder.top_manager_detail_video.setUp(Environment.getExternalStorageDirectory()+"/shenxiaoai.mp4", top.getContent()	.getTitle());
		}
		viewHolder.top_manager_detail_apply_agree.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String topId = top.getId();
				String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/applytopagree?topId=" + topId; 
				HttpRequestUtils.getRequest(url, new Callback() {
					
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						Log.d("news", "同意申请=" + response.body().string());
						((Activity)context).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								tops.remove(viewHolder.getAdapterPosition());
								notifyDataSetChanged();
							}
						});
					}
					
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						TopManagerActivity activity = (TopManagerActivity) context;
						NetworkUtils.showErrorMessage(activity, activity.getMessage());
					}
				});
			}
		});
		viewHolder.top_manager_detail_apply_rejust.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String topId = top.getId();
				String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/applytopreject?topId=" + topId; 
				HttpRequestUtils.getRequest(url, new Callback() {
					
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						Log.d("news", "拒绝申请=" + response.body().string());
						((Activity)context).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								tops.remove(viewHolder.getAdapterPosition());
								notifyDataSetChanged();
							}
						});
					}
					
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						TopManagerActivity activity = (TopManagerActivity) context;
						NetworkUtils.showErrorMessage(activity, activity.getMessage());
					}
				});
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_top_manager_detail_recyclerview, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if(itemListener != null) {
					itemListener.onItemClick(v, position, tops.get(position));
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