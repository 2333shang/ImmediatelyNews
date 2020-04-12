package com.shang.immediatelynews.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.shang.immediatelynews.constant.FileUploadConstant;

import android.util.Log;
import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpRequestUtils {
	
	public final static HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

	public static void getRequest(String url, okhttp3.Callback callback) {
		// 1.创建OkHttpClient对象
		OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
			@Override
			public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
				cookieStore.put(httpUrl.host(), list);
			}

			@Override
			public List<Cookie> loadForRequest(HttpUrl httpUrl) {
				List<Cookie> cookies = cookieStore.get(httpUrl.host());
				return cookies != null ? cookies : new ArrayList<Cookie>();
			}
		}).build();
		// 2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
		Request request = new Request.Builder().url(url).method("GET", null).build();
		// 3.创建一个call对象,参数就是Request请求对象
		Call call = okHttpClient.newCall(request);
		// 4.请求加入调度，重写回调方法
		call.enqueue(callback);
	}
	
	public static void getPost(String url, FormBody.Builder params, okhttp3.Callback callback) {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
			@Override
			public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
				cookieStore.put(httpUrl.host(), list);
			}

			@Override
			public List<Cookie> loadForRequest(HttpUrl httpUrl) {
				List<Cookie> cookies = cookieStore.get(httpUrl.host());
				return cookies != null ? cookies : new ArrayList<Cookie>();
			}
		}).build();
		// 2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
		Request request = null;
		if(params != null) {
			request = new Request.Builder()
					.url(url)
					.post(params.build())
					.build();
		}else {
			request = new Request.Builder()
					.url(url)
					.build();
		}
		// 3.创建一个call对象,参数就是Request请求对象
		Call call = okHttpClient.newCall(request);
		// 4.请求加入调度，重写回调方法
		call.enqueue(callback);
	}
	
}
