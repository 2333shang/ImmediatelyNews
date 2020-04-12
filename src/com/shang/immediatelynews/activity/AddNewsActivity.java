package com.shang.immediatelynews.activity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.example.richeditor.RichEditor;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.callback.FileUploadCallback;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.Attachment;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Request;
import okhttp3.Response;

@ContentView(R.layout.activity_add_news)
public class AddNewsActivity extends AppCompatActivity {

	@ViewInject(R.id.news_content)
	private RichEditor news_content;
	@ViewInject(R.id.news_titie)
	private EditText news_titie;
	private String preId;
	
	private boolean mHasAddImg = false;
	public Handler file_handler = new Handler() {
		
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			Attachment attachment = (Attachment) msg.obj;
			Log.d("news", attachment.getUrl());
			news_content.insertImage(attachment.getUrl(), "picvision\" style=\"max-width:100%");
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
		news_content.setPadding(10, 10, 10, 10);
		WebSettings webSettings = news_content.getSettings();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {         
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		news_content.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
			@Override
			public void onTextChange(String text) {
				if (mHasAddImg) {
		            mHasAddImg = false;	
		            news_content.setNewLine();
		            news_content.setAlignLeft();
				}
			}
		});
		findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.undo();
			}
		});

		findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.redo();
			}
		});

		findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.setBold();
			}
		});

		findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.setItalic();
			}
		});

		findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.setStrikeThrough();
			}
		});

		findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.setUnderline();
			}
		});

		findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.setAlignLeft();
			}
		});

		findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.setAlignCenter();
			}
		});

		findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				news_content.setAlignRight();
			}
		});

		findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAlum();
			}
		});
	}

	public void openAlum() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 2:
			if(resultCode == RESULT_OK) {
				mHasAddImg = true;
				String path = handleImageOnKitKat(data);
				uploadFile(path);
			}
			break;
		default:
			break;
		}
	}
	
	public String handleImageOnKitKat(Intent data) {
		String imagePath = null;
		Uri uri = data.getData();
		if(DocumentsContract.isDocumentUri(AddNewsActivity.this, uri)) {
			String docId = DocumentsContract.getDocumentId(uri);
			if("com.android.providers.media.documents".equals(uri.getAuthority())) {
				String id = docId.split(":")[1];
				String selection = MediaStore.Images.Media._ID+"="+id;
				imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
			}else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
				imagePath = getImagePath(contentUri, null);
			}
		}else if("content".equalsIgnoreCase(uri.getScheme())) {
			imagePath = getImagePath(uri, null);
		}else if("file".equalsIgnoreCase(uri.getScheme())) {
			imagePath = uri.getPath();
		}
		return imagePath;
	}

	public String getImagePath(Uri uri, String selection) {
		String path = null;
		Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
		if(cursor!=null) {
			if(cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cursor.close();
		}
		return path;
	}
	
	public void cancel(View v) {
		Toast.makeText(this, "cancel", 0).show();
		finish();
	}
	
	public void submit(View v) {
		Log.d("news", news_content.getHtml());
		FormBody.Builder params=new FormBody.Builder();
		params.add("content", news_content.getHtml());
		params.add("title", news_titie.getText().toString());
		params.add("newsType", "0");
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

        String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/attachment/uploadattachment";

        Log.d("news", path.substring(path.lastIndexOf("/") + 1));
        OkHttpUtils.post()//
            .addFile("file", path.substring(path.lastIndexOf("/") + 1), file)//指定接收文件参数名，文件名和文件
            .url(url)
            .params(params)//设置参数
            .headers(headers)//请求头设置
            .build()
            .execute(new FileUploadCallback() {

				@Override
				public void onError(Call call, Exception exception, int arg2) {
					Log.d("news", exception.toString());
				}

				@Override
				public void onResponse(Attachment attachment, int arg1) {
					//设置上传文件的路径
			        Message msg = Message.obtain();
			        msg.what = 1;
			        msg.obj = attachment;
			        file_handler.sendMessage(msg);
				}
            	
            });
        
    }
}
