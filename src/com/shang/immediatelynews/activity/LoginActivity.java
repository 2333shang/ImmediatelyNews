package com.shang.immediatelynews.activity;

import java.io.IOException;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.MainActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.R.id;
import com.shang.immediatelynews.R.layout;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.utils.HttpRequestUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

	@ViewInject(R.id.username)
	public EditText username;
	@ViewInject(R.id.password)
	public EditText password;
	private Bundle args;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
	}
	
	public void login(View v) {
		final String username_text = username.getText().toString();
		final String password_text = password.getText().toString();
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/user/login?username=" + username_text + "&password=" + password_text, new Callback() {

			@Override
			public void onFailure(Call call, IOException exception) {
				Log.d("news", "联网失败");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				args = new Bundle();
				String user = response.body().string();
				args.putString("topNews", user);
				Log.d("news", "http://116.62.234.70:8080/news/user/login?username=" + username_text + "&password=" + password_text);
				Log.d("news", user);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						//添加第一个页面的Fragment
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						intent.putExtra("user", args);
						startActivity(intent);
						finish();
					}
				});
			}
			
		});
	}
}
