package com.shang.immediatelynews.utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class GlideUtils {

	public static void loadImage(Context context, final ImageView iv, String url) {
		RequestOptions options = new RequestOptions()
		        .error(R.drawable.news_error)
		        .placeholder(R.drawable.news_loading);	
		Glide.with(context).asBitmap().apply(options).load(url).into(new SimpleTarget<Bitmap>() {

			@Override
			public void onResourceReady(Bitmap bm, Transition<? super Bitmap> arg1) {
				iv.setImageBitmap(bm);
			}
		});
	}
}
