package com.shang.immediatelynews.callback;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shang.immediatelynews.entities.Attachment;
import com.zhy.http.okhttp.callback.Callback;

import android.util.Log;
import okhttp3.Call;
import okhttp3.Response;

public abstract class FileUploadCallback extends Callback<Attachment> {

	@Override
	public abstract void onError(Call call, Exception exception, int arg2);

	@Override
	public abstract void onResponse(Attachment attachment, int arg1);

	@Override
	public Attachment parseNetworkResponse(Response response, int id) throws Exception {
		 String string = response.body().string();
		 Log.d("news", "response=" + string);
		 Gson gson = new GsonBuilder()
					.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
					.create();
		 Attachment attachment = gson.fromJson(string, Attachment.class);
	     return attachment;
	}

}
