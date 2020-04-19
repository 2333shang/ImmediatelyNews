package com.shang.immediatelynews.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.activity.AddNewsActivity;
import com.shang.immediatelynews.activity.NewsManagerActivity;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.Content;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsManagerAdapter extends RecyclerView.Adapter<NewsManagerAdapter.ViewHolder> {

	private Context context;
	private String newsType;
	private String type;
	private List<Content> contents = new ArrayList<Content>();
	private Fragment fragment;
	
	public NewsManagerAdapter(Fragment fragment, Context context, String newsType, List<Content> coontents, String type) {
		super();
		this.fragment = fragment;
		this.context = context;
		this.contents = coontents;
		this.newsType = newsType;
		this.type = type;
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		private View view;
		private TextView news_manager_detail_title;
		private List<ImageView> news_manager_detail_contents;
		private JCVideoPlayerStandard news_manager_detail_video;
		private LinearLayout news_manager_detail_content_head;
		private LinearLayout news_manager_detail_video_head;
		private LinearLayout news_manager_detail_apply_head;
		private Button news_manager_detail_topapply;
		private Button news_manager_detail_update;
		private Button news_manager_detail_delete;

		public ViewHolder(View itemView) {
			super(itemView);
			view = itemView;
			news_manager_detail_video = (JCVideoPlayerStandard) itemView.findViewById(R.id.news_manager_detail_video);
			news_manager_detail_title = (TextView) itemView.findViewById(R.id.news_manager_detail_title);
			news_manager_detail_contents = new ArrayList<ImageView>();
			ImageView news_manager_detail_content_1 = (ImageView) itemView
					.findViewById(R.id.news_manager_detail_content_1);
			ImageView news_manager_detail_content_2 = (ImageView) itemView
					.findViewById(R.id.news_manager_detail_content_2);
			ImageView news_manager_detail_content_3 = (ImageView) itemView
					.findViewById(R.id.news_manager_detail_content_3);
			news_manager_detail_contents.add(news_manager_detail_content_1);
			news_manager_detail_contents.add(news_manager_detail_content_2);
			news_manager_detail_contents.add(news_manager_detail_content_3);
			news_manager_detail_content_head = (LinearLayout) itemView
					.findViewById(R.id.news_manager_detail_content_head);
			news_manager_detail_video_head = (LinearLayout) itemView.findViewById(R.id.news_manager_detail_video_head);
			news_manager_detail_apply_head = (LinearLayout) itemView.findViewById(R.id.news_manager_detail_apply_head);
			news_manager_detail_topapply = (Button) itemView.findViewById(R.id.news_manager_detail_topapply);
			news_manager_detail_update = (Button) itemView.findViewById(R.id.news_manager_detail_update);
			news_manager_detail_delete = (Button) itemView.findViewById(R.id.news_manager_detail_delete);
		}
	}

	@Override
	public int getItemCount() {
		return contents.size();
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int postiopn) {
		final Content content = contents.get(postiopn);
		viewHolder.news_manager_detail_title.setText(content.getTitle());
		if ("0".equals(newsType)) {
			List<Attachment> pics = content.getPics();
			if (!pics.isEmpty()) {
				viewHolder.news_manager_detail_content_head.setVisibility(View.VISIBLE);
				for (int i = 0; i < pics.size(); i++) {
					final ImageView imageView = viewHolder.news_manager_detail_contents.get(i);
					Glide.with(context).asBitmap()
							.load(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH
									+ FileUploadConstant.FILE_REAL_PATH + pics.get(i).getUrl())
							.into(new SimpleTarget<Bitmap>() {

								@Override
								public void onResourceReady(Bitmap bm, Transition<? super Bitmap> arg1) {
									imageView.setImageBitmap(bm);
								}
							});
					if (i == 2) {
						break;
					}
				}
			}
		} else {
			viewHolder.news_manager_detail_video_head.setVisibility(View.VISIBLE);
			viewHolder.news_manager_detail_video.setUp(Environment.getExternalStorageDirectory() + "/shenxiaoai.mp4",
					content.getTitle());
		}
		if ("0".equals(type) || "1".equals(type)) {
			viewHolder.news_manager_detail_apply_head.setVisibility(View.VISIBLE);
			if("1".equals(type)) {
				viewHolder.news_manager_detail_update.setVisibility(View.GONE);
			}
			if(content.getApplyStatus() != null) {
				viewHolder.news_manager_detail_topapply.setEnabled(false);
				if(content.getApplyStatus().equals("1")) {
					viewHolder.news_manager_detail_topapply.setText("待处理中");
				}else if(content.getApplyStatus().equals("0")) {
					viewHolder.news_manager_detail_topapply.setText("已拒绝");
				}else if(content.getApplyStatus().equals("2")) {
					viewHolder.news_manager_detail_topapply.setText("已同意");
				}
				viewHolder.news_manager_detail_update.setEnabled(false);
				viewHolder.news_manager_detail_delete.setEnabled(false);
			}
			viewHolder.news_manager_detail_topapply.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final String items[] = new String[] { "普通头条", "轮播头条"};
					new AlertDialog.Builder(context).setTitle("单选")
							/*
							 * 第一个参数items：单选列表的值，是一个CharSequence数组 第二个参数：默认选中第几个item 第三个参数：单选列表选中的点击监听事件
							 */
							.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
								/*
								 * which：点击的item的位置
								 */
								@Override
								public void onClick(final DialogInterface dialog, int which) {
									String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH
											+ "/top/applytop?newsId=" + content.getId() + "&resousel=" + which;
									HttpRequestUtils.getRequest(url, new Callback() {

										@Override
										public void onResponse(Call arg0, Response arg1) throws IOException {
											dialog.dismiss();//移除dialog
											((Activity)context).runOnUiThread(new Runnable() {
												
												@Override
												public void run() {
													viewHolder.news_manager_detail_topapply.setText("待处理中");
													viewHolder.news_manager_detail_topapply.setEnabled(false);
													viewHolder.news_manager_detail_update.setEnabled(false);
													viewHolder.news_manager_detail_delete.setEnabled(false);
												}
											});
										}

										@Override
										public void onFailure(Call arg0, IOException arg1) {
											dialog.dismiss();
											NewsManagerActivity activity = (NewsManagerActivity) context;
											NetworkUtils.showErrorMessage(activity, activity.getMessage());
										}
									});
								}
							}).show();

				}
			});
			viewHolder.news_manager_detail_update.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, AddNewsActivity.class);
					intent.putExtra("news", content);
					intent.putExtra("update", "1");
					fragment.startActivityForResult(intent, 1);
				}
			});
			viewHolder.news_manager_detail_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final int position = viewHolder.getAdapterPosition();
					String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/delcontent?newsId=" + content.getId();
					HttpRequestUtils.getRequest(url, new Callback() {
						
						@Override
						public void onResponse(Call call, Response response) throws IOException {
							((Activity)context).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(context, "删除成功！", 0).show();
									contents.remove(position);
									notifyDataSetChanged();
//									notifyItemRemoved(position);
//									notifyItemRangeChanged(position,contents.size()-position);
								}
							});
						}
						
						@Override
						public void onFailure(Call v, IOException exception) {
							
						}
					});
				}
			});
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTtpe) {
		View view = View.inflate(parent.getContext(), R.layout.activity_news_manager_detail_recyclerview, null);
		final ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = viewHolder.getAdapterPosition();
				if (itemListener != null) {
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
	
	private OnRecyclerViewItemRemoveListener itemRemoveListener;

	public void setOnItemRemoveistener(OnRecyclerViewItemRemoveListener itemRemoveListener) {
		this.itemRemoveListener = itemRemoveListener;
	}
}