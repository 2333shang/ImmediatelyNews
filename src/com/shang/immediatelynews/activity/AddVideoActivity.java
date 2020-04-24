package com.shang.immediatelynews.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.longner.lib.JCVideoPlayerStandard;
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.callback.FileUploadCallback;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Response;

@ContentView(R.layout.activity_add_video)
public class AddVideoActivity extends BaseActivity {

	@ViewInject(R.id.news_video_content)
	private JCVideoPlayerStandard news_video_content;
	@ViewInject(R.id.news_video_titie)
	private EditText news_video_titie;
	@ViewInject(R.id.news_video_img)
	private ImageView news_video_img;
	@ViewInject(R.id.news_video_title)
	private TextView news_video_title;
	@ViewInject(R.id.news_video_img_changebtn)
	private Button news_video_img_changebtn;
	
	private String preId;
	private Attachment attachment_video;
	private String path;
	private String imagepath;
	public Handler file_handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.d("news", attachment_video.getUrl());
				if(news_video_content.getVisibility() == View.GONE) {
					news_video_content.setVisibility(View.VISIBLE);
				}
//				news_video_content.loadUrl(attachment_video.getUrl());
//				getVideoImage(attachment_video.getUrl());
				news_video_content.setUp(path, attachment_video.getFileName());
//				news_video_content.start();
				getVideoImage(path);
				break;
			case 2:
				FormBody.Builder params = (Builder) msg.obj;
				String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/content/addnews";
				HttpRequestUtils.getPost(url, params, new okhttp3.Callback() {
					
					@Override
					public void onResponse(Call call, Response response) throws IOException {
						String object = response.body().string();
						if("login_invalid".equals(object)) {
							NetworkUtils.toSessionInvalid(AddVideoActivity.this);
						}else {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(AddVideoActivity.this, "发布成功!", 0).show();
									finish();
								}
							});
						}
					}
					
					@Override
					public void onFailure(Call call, IOException response) {
						NetworkUtils.showErrorMessage(AddVideoActivity.this, getMessage());
					}
				});
				break;
			case 3:
				params = new FormBody.Builder();
				params.add("content", attachment_video.getUrl());
				params.add("title", news_video_titie.getText().toString());
				params.add("newsType", "1");
				params.add("businesskey", preId);
				msg = Message.obtain();
				msg.what = 2;
				msg.obj = params;
				file_handler.sendMessage(msg);
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
		ActivityUtils.addActivities(this);
		preId = UUID.randomUUID().toString().replaceAll("-", "");
	}

	public void openAlum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("video/*");
		startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				if ("file".equalsIgnoreCase(uri.getScheme())) {// 使用第三方应用打开
					imagepath = uri.getPath();
				}else {
					imagepath = getPath(this, uri);
				}
				news_video_img.setImageBitmap(BitmapFactory.decodeFile(imagepath));
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
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
		uploadImageFile(imagepath);
	}

	private void uploadFile(final String path) {
		File file = new File(path);
		Map<String, String> params = new HashMap<String, String>();
		params.put("preId", preId);
		params.put("attachmentType", "3");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("APP-Key", "APP-Secret222");
		headers.put("APP-Secret", "APP-Secret111");
		String host = FileUploadConstant.FILE_NET.split("//")[1].split(":")[0];
		headers.put("Cookie", HttpRequestUtils.cookieStore.get(host).get(0).toString());

		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH
				+ "/attachment/uploadattachment";
		
		final ProgressDialog progressDialog = new ProgressDialog(AddVideoActivity.this);
		progressDialog.setTitle("文件上传");
		//设置水平进度
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMax(100);
		progressDialog.setCancelable(false);
		progressDialog.show();
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
						progressDialog.dismiss();
						NetworkUtils.showErrorMessage(AddVideoActivity.this, getMessage());
					}

					@Override
					public void onResponse(Attachment attachment, int arg1) {
						progressDialog.dismiss();
						attachment_video = attachment;
						file_handler.sendEmptyMessage(1);
					}

					@Override
					public void inProgress(float progress, long total, int id) {
//						super.inProgress(progress, total, id);
						progressDialog.setProgress((int)(progress * 100));
					}
					
				});

	}
	
	private void uploadImageFile(final String path) {
		if(path == null) {
			Toast.makeText(this, "请上传视频", 0).show();
			return;
		}
		if(news_video_titie.getText().toString() == null || news_video_titie.getText().toString().equals("")) {
			Toast.makeText(this, "请输入新闻标题", 0).show();
			return;
		}
		if(attachment_video == null || attachment_video.getUrl().equals("")) {
			Toast.makeText(this, "请上传视频", 0).show();
			return;
		}
		File file = new File(path);
		Map<String, String> params = new HashMap<String, String>();
		params.put("preId", preId);
		params.put("attachmentType", "2");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("APP-Key", "APP-Secret222");
		headers.put("APP-Secret", "APP-Secret111");
		String host = FileUploadConstant.FILE_NET.split("//")[1].split(":")[0];
		headers.put("Cookie", HttpRequestUtils.cookieStore.get(host).get(0).toString());

		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH
				+ "/attachment/uploadattachment";
		
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(AddVideoActivity.this, "上传中！");
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
						NetworkUtils.dismissLoading2(showLoading2);
						NetworkUtils.showErrorMessage(AddVideoActivity.this, getMessage());
					}

					@Override
					public void onResponse(Attachment attachment, int arg1) {
						NetworkUtils.dismissLoading2(showLoading2);
						file_handler.sendEmptyMessage(3);
					}
					
				});

	}
	
	public void getVideoImage(String path) {
		Glide.with(this)
        .setDefaultRequestOptions(
                new RequestOptions()
                        .frame(1000000)
                        .centerCrop())
        .asBitmap()
        .load(path)
        .into(new SimpleTarget<Bitmap>() {

			@Override
			public void onResourceReady(Bitmap bm, Transition<? super Bitmap> arg1) {
				news_video_img.setImageBitmap(bm);
				news_video_title.setVisibility(View.VISIBLE);
				news_video_img.setVisibility(View.VISIBLE);
				news_video_img_changebtn.setVisibility(View.VISIBLE);
				save(bm);
			}
		});
	}
	
	public void save(Bitmap bitmap) {
		try {
			File file = new File(getExternalFilesDir(null).getAbsolutePath(), "temp.jpg");
			if(file.exists()) {
				file.delete();
			}
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			imagepath = file.getAbsolutePath();
		    out.flush();
		    out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void updateimg(View v) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
