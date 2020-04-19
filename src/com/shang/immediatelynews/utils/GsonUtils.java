package com.shang.immediatelynews.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.util.Log;

public class GsonUtils {
	
	public static <T> T getGsonWithLocalDate(TypeToken<T> typeToken, String object) {
		Log.d("news", object);
		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
		Type type = typeToken.getType();
		return gson.fromJson(object, type);
	}
}
