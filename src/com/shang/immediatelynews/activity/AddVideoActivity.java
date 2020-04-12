package com.shang.immediatelynews.activity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.R;
import com.shang.immediatelynews.callback.FileUploadCallback;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Response;

@ContentView(R.layout.activity_add_video)
public class AddVideoActivity extends AppCompatActivity {

	@ViewInject(R.id.news_video_content)
	private WebView news_video_content;
	@ViewInject(R.id.news_video_titie)
	private EditText news_video_titie;
	@ViewInject(R.id.news_video_progressbar)
	private ProgressBar news_video_progressbar;
	private String preId;
	private Attachment attachment_video;
	public Handler file_handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.d("news", attachment_video.getUrl());
				if(news_video_content.getVisibility() == View.GONE) {
					news_video_content.setVisibility(View.VISIBLE);
				}
				news_video_content.loadUrl(attachment_video.getUrl());
				break;
			case 2:
				FormBody.Builder params = (Builder) msg.obj;
				Log.d("news", params.toString());
				String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addnews";
				HttpRequestUtils.getPost(url, params, new okhttp3.Callback() {

					@Override
					public void onResponse(Call call, Response response) throws IOException {
						Log.d("news", "上传成功！");
						finish();
					}

					@Override
					public void onFailure(Call call, IOException response) {

					}
				});
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		preId = UUID.randomUUID().toString().replaceAll("-", "");
	}

	public void openAlum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 2:
			if (resultCode == RESULT_OK) {
				String path = null;
				Uri uri = data.getData();
				if ("file".equalsIgnoreCase(uri.getScheme())) {// 使用第三方应用打开
					path = uri.getPath();
				}else {
					path = getPath(this, uri);
				}
				if(path.endsWith(".mp4")) {
					uploadFile(path);
				}else {
					Toast.makeText(AddVideoActivity.this, "请选择mp4格式的文件", 0).show();
				}
			}
			break;
		default:
			break;
		}
	}

	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public void cancel(View v) {
		Toast.makeText(this, "cancel", 0).show();
		finish();
	}

	public void selectvideo(View v) {
		openAlum();
	}

	public void submit(View v) {
		FormBody.Builder params = new FormBody.Builder();
		params.add("content", attachment_video.getUrl());
		params.add("title", news_video_titie.getText().toString());
		params.add("newsType", "1");
		params.add("businesskey", preId);
		Message msg = Message.obtain();
		msg.what = 2;
		msg.obj = params;
		file_handler.sendMessage(msg);
	}

	private void uploadFile(final String path) {
		File file = new File(path);
		Map<String, String> params = new HashMap<String, String>();
		params.put("preId", preId);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("APP-Key", "APP-Secret222");
		headers.put("APP-Secret", "APP-Secret111");
		String host = FileUploadConstant.FILE_NET.split("//")[1].split(":")[0];
		headers.put("Cookie", HttpRequestUtils.cookieStore.get(host).get(0).toString());

		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH
				+ "/attachment/uploadattachment";

		OkHttpUtils.post()//
				.addFile("file", path.substring(path.lastIndexOf("/") + 1), file)// 指定接收文件参数名，文件名和文件
				.url(url).params(params)// 设置参数
				.headers(headers)// 请求头设置
				.build()
				.connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
				.execute(new FileUploadCallback() {

					@Override
					public void onError(Call call, Exception exception, int arg2) {	
						Log.d("news", exception.toString());
						Log.d("news", exception.getMessage());
						Log.d("news", exception.getStackTrace().toString());
						Log.d("news", String.valueOf(arg2));
					}

					@Override
					public void onResponse(Attachment attachment, int arg1) {
						attachment_video = attachment;
						Log.d("new", "video=" + attachment.toString());
						file_handler.sendEmptyMessage(1);
					}

				});

	}
}
