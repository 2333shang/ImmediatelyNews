package com.shang.immediatelynews.utils;

import java.util.HashMap;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.shang.immediatelynews.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class GlideUtils {
	
	public static Map<String, Bitmap> maps = new HashMap<String, Bitmap>();

	public static void loadImage(final Context context, final ImageView iv, final String url) {
		iv.setVisibility(View.VISIBLE);
		if(maps.get(url) != null) {
			Log.d("news", maps.get(url).isRecycled() + "");
			if(maps.get(url).isRecycled()) {
				maps.remove(url);
				System.gc();
			}else {
				iv.setImageBitmap(maps.get(url));
				return;
			}
		}
		iv.setTag(url);
		RequestOptions options = new RequestOptions()
		        .error(R.drawable.news_error)
		        .placeholder(R.drawable.news)
		        .fallback(R.drawable.news);	
		Glide.with(context).asBitmap().load(url).apply(options).into(new SimpleTarget<Bitmap>() {

			@Override
			public void onResourceReady(Bitmap bm, Transition<? super Bitmap> arg1) {
				maps.put(url, bm);
				if(iv.getTag().equals(url)) {
					iv.setImageBitmap(bm);
				}
			}
		});
	}
	
	public static void loadBackgroupImage(final Context context, final ImageView iv, final String url) {
		iv.setTag(url);
		iv.setBackground(null);
		iv.setVisibility(View.VISIBLE);
		if(maps.get(url) != null) {
			iv.setBackground(new BitmapDrawable(((Activity)context).getResources(),maps.get(url)));
			return;
		}
		RequestOptions options = new RequestOptions()
		        .error(R.drawable.news_error)
		        .placeholder(R.drawable.news)
		        .fallback(R.drawable.news);	
		Glide.with(context).asBitmap().load(url).apply(options).into(new SimpleTarget<Bitmap>() {

			@Override
			public void onResourceReady(Bitmap bm, Transition<? super Bitmap> arg1) {
				maps.put(url, bm);
				if(iv.getTag().equals(url)) {
					iv.setBackground(new BitmapDrawable(((Activity)context).getResources(),bm));
				}
			}
		});
	}
}
