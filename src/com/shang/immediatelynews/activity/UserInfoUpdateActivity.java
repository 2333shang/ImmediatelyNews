package com.shang.immediatelynews.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.callback.FileUploadCallback;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.entities.User;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.GlideUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Response;

@ContentView(R.layout.activity_user_info_update)
public class UserInfoUpdateActivity extends BaseActivity {

	@ViewInject(R.id.user_update_name)
	private TextView user_update_name;
	@ViewInject(R.id.user_update_gender)
	private TextView user_update_gender;
	@ViewInject(R.id.user_update_birth)
	private TextView user_update_birth;
	@ViewInject(R.id.user_update_company)
	private TextView user_update_company;
	@ViewInject(R.id.user_update_headicon)
	private CircleImageView user_update_headicon;
	@ViewInject(R.id.user_update_headicon_layout)
	private RelativeLayout user_update_headicon_layout;
	@ViewInject(R.id.user_update_name_layout)
	private RelativeLayout user_update_name_layout;
	@ViewInject(R.id.user_update_gender_layout)
	private RelativeLayout user_update_gender_layout;
	@ViewInject(R.id.user_update_birth_layout)
	private RelativeLayout user_update_birth_layout;

	private String path;
	private String preId;
	private User user;
	private Uri uri;
	private Handler user_handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				setUserInfo();
				FormBody.Builder params = new Builder();
				if(user.getHeadIcon() != null && user.getHeadIcon().getBusinesskey().equals(preId)) {
					params.add("businesskey", preId);
				}
				params.add("id", user.getId());
				String username = user_update_name.getText().toString();
				if(!user.getUsername().equals(username)) {
					user.setUsername(username);
					params.add("username", username);
				}
				params.add("gender", user.getGender());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				params.add("birthdate", format.format((user.getBirthdate())));
				String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/user/userupdate";
				HttpRequestUtils.getPost(url, params, new okhttp3.Callback() {
					
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						String object = response.body().string();
						if("login_invalid".equals(object)) {
							NetworkUtils.toSessionInvalid(UserInfoUpdateActivity.this);
						}else {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Intent data = new Intent();
									data.putExtra("updateuser", user);
									setResult(2, data);
									finish();
								}
							});
						}
					}
					
					@Override
					public void onFailure(Call arg0, IOException arg1) {
						NetworkUtils.showErrorMessage(UserInfoUpdateActivity.this, getMessage());
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
		ActivityUtils.addActivities(this);
		preId = UUID.randomUUID().toString().replaceAll("-", "");
		user = (User) getIntent().getSerializableExtra("user");
		// user_handler.sendEmptyMessage(1);
		setUserInfo();
		user_update_headicon_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPicsSelect();
			}
		});	
//		user_update_name_layout.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				user_update_name.requestFocus();
//			}
//		});
		user_update_gender_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showGenderChangeDialog();
			}
		});
		user_update_birth_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showBirthChangeDialog();
			}
		});
	}

	protected void showBirthChangeDialog() {
		String date = user_update_birth.getText().toString();
		final int year;
		final int monthOfYear;
		final int dayOfMonth;
		if(date == null || date == "") {
			Calendar instance = Calendar.getInstance();
			year = instance.get(Calendar.YEAR);
			monthOfYear = instance.get(Calendar.MONTH);
			dayOfMonth = instance.get(Calendar.DAY_OF_MONTH);
		}else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parse = null;
			try {
				parse = format.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar instance = Calendar.getInstance();
			instance.setTime(parse);
			year = instance.get(Calendar.YEAR);
			monthOfYear = instance.get(Calendar.MONTH);
			dayOfMonth = instance.get(Calendar.DAY_OF_MONTH);
		}
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {//选择的年月日
		    	user_update_birth.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
		    	Calendar instance = Calendar.getInstance();
		    	instance.set(year, monthOfYear, dayOfMonth);
		    	user.setBirthdate(instance.getTime());
		    }
		}, year, monthOfYear, dayOfMonth).show();
	}

	protected void showGenderChangeDialog() {
		final String items[] = new String[]{"男","女", "保密"};
		new AlertDialog.Builder(this)
		    .setTitle("性别")
		    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            user_update_gender.setText(items[which]);
		            user.setGender(items[which]);
		            dialog.dismiss();//移除dialog
		        }
		    })
		    .setNegativeButton("取消", null)
		    .show();
	}

	protected void showPicsSelect() {
		final String items[] = new String[]{"相册","拍照"};
		new AlertDialog.Builder(this)
		    .setTitle("头像")
		    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        	if(items[which].equals("相册")) {
		        		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        		intent.setType("image/*");
		        		startActivityForResult(intent, 0);
		        	}else {
		        		File out = new File(getExternalFilesDir(null).getAbsolutePath(),"headicon.jpg");
						try {
							if(out.exists())
								out.delete();
							out.createNewFile();
						} catch (Exception e) {
						}
						if(Build.VERSION.SDK_INT>=24) {
							uri = FileProvider.getUriForFile(UserInfoUpdateActivity.this, "com.example.multimedia.fileprovider", out);
						}else {
							uri = Uri.fromFile(out);
						}
						Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						startActivityForResult(intent, 1);
		        	}
		            dialog.dismiss();//移除dialog
		        }
		    })
		    .setNegativeButton("取消", null)
		    .show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				if ("file".equalsIgnoreCase(uri.getScheme())) {// 使用第三方应用打开
					path = uri.getPath();
				}else {
					path = getPath(this, uri);
				}
//				uploadFile(path);
				GlideUtils.loadImage(UserInfoUpdateActivity.this, user_update_headicon, path);
			}
			break;
		case 1:
			if(resultCode == RESULT_OK) {
				try {
//					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//					user_update_headicon.setImageBitmap(bitmap);
//					uploadFile(getExternalFilesDir(null).getAbsolutePath()+"/headicon.jpg");
					path = getExternalFilesDir(null).getAbsolutePath()+"/headicon.jpg";
					GlideUtils.loadImage(UserInfoUpdateActivity.this, user_update_headicon, path);
//					Attachment a = new Attachment();
//					a.setUrl(getExternalFilesDir(null).getAbsolutePath(),"headicon.jpg");
//					user.setHeadIcon(headIcon);
				} catch (Exception e) {
					e.printStackTrace();
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
	
	private void uploadFile(final String path) {
		File file = new File(path);
		Map<String, String> params = new HashMap<String, String>();
		params.put("preId", preId);
		params.put("attachmentType", "0");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("APP-Key", "APP-Secret222");
		headers.put("APP-Secret", "APP-Secret111");
		String host = FileUploadConstant.FILE_NET.split("//")[1].split(":")[0];
		headers.put("Cookie", HttpRequestUtils.cookieStore.get(host).get(0).toString());

		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH
				+ "/attachment/uploadattachment";

		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(UserInfoUpdateActivity.this, "消息更新中!");
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
						NetworkUtils.showErrorMessage(UserInfoUpdateActivity.this, getMessage());
					}

					@Override
					public void onResponse(final Attachment attachment, int arg1) {
						NetworkUtils.dismissLoading2(showLoading2);
						runOnUiThread(new Runnable() {
								
							@Override
							public void run() {
								user.setHeadIcon(attachment);
								user_handler.sendEmptyMessage(1);
							}
						});
					}

				});

	}
	
	// @Event(value = R.id.user_update_headicon_layout)
	// public void headiconUpdate(View v) {
	// Toast.makeText(this, "hello", 0).show();
	// }

	protected void setUserInfo() {
		if(user.getHeadIcon()!=null)
			GlideUtils.loadImage(this, user_update_headicon, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + user.getHeadIcon().getUrl());
		user_update_name.setText(user.getUsername());
		user_update_gender.setText(user.getGender());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		user_update_birth.setText(format.format(user.getBirthdate()));
		user_update_company.setText(user.getCompanyName());
	}

	public void submit(View v) {
		uploadFile(path);
	}

	public void cancel(View v) {
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
