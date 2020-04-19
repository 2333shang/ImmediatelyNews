package com.shang.immediatelynews.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.activity.LoginActivity;
import com.shang.immediatelynews.entities.Content;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class NetworkUtils {

	private static ProgressDialog progressDialog;
	
	public static void showErrorMessage(final Activity context, final String message) {
		Log.d("news", message);
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if("-1".equals(message)){
					Toast.makeText(context, "网络中断！", 0).show();
				}else {
					Toast.makeText(context, "服务器异常！", 0).show();
				}
			}
		});
	}
	
	public static void toSessionInvalid(final Activity context) {
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				ActivityUtils.finishAll();
				Toast.makeText(context, "登录信息过期，请重新登陆！", 0).show();
			}
		});
	}
	
//	public static void showLoading(Activity context, String message) {
//		progressDialog = new ProgressDialog(context);
//		progressDialog.setMessage(message);
////		progressDialog.setCancelable(false);
//		progressDialog.show();
//		Log.d("news", "loading");
//	}
//	
//	public static void dismissLoading() {
//		if(progressDialog != null) {
//			progressDialog.dismiss();
//			progressDialog = null;
//			Log.d("news", "dismiss");
//		}
//	}
	
	public static ProgressDialog showLoading2(Activity context, String message) {
		ProgressDialog progressDialog2 = new ProgressDialog(context);
		progressDialog2.setMessage(message);
//		progressDialog.setCancelable(false);
		progressDialog2.show();
		return progressDialog2;
	}
	
	public static void dismissLoading2(ProgressDialog progressDialog) {
		if(progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	public static <T> void addMoreDataResponse(final Activity context, TypeToken<T> type, List list, String response, final Handler handler) {
		if("login_invalid".equals(response)) {
			NetworkUtils.toSessionInvalid(context);
		}else {
			List data = (List) GsonUtils.getGsonWithLocalDate(type, response);
			if(data == null || data.isEmpty()) {
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(context, "没有更多的新闻了！", 0).show();
						handler.sendEmptyMessage(3);
					}
				});
			}else {
				list.addAll(data);
				handler.sendEmptyMessage(2);
			}
		}
	}
	
	public static <T> void addNewsDataResponse(final Activity context, TypeToken<T> type, List list, String response, final Handler handler) {
		if("login_invalid".equals(response)) {
			NetworkUtils.toSessionInvalid(context);
		}else {
			List data = (List) GsonUtils.getGsonWithLocalDate(type, response);
			if(data == null || data.isEmpty()) {
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(context, "当前没有新闻！", 0).show();
						handler.sendEmptyMessage(3);
					}
				});
				return;
			}
			if(!getBusinessKey(data.get(0)).equals(getBusinessKey(list.get(0)))) {
				list.clear();
				list.addAll(data);
				handler.sendEmptyMessage(1);
			}else {
				context.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(context, "没有更新的新闻了！", 0).show();
						handler.sendEmptyMessage(3);
					}
				});
			}
		}
	}
	
	private static String getBusinessKey(Object obj) {
		Method[] m = obj.getClass().getMethods();
		for(int i = 0;i < m.length;i++){
			if(("getId").equals(m[i].getName())){
				try {
					return (String) m[i].invoke(obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
