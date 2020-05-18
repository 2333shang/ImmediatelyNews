package com.shang.immediatelynews.activity;

import java.io.IOException;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.MainActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.User;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.username)
	public EditText username;
	@ViewInject(R.id.password)
	public EditText password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		x.view().inject(this);
		//获取焦点，弹出软键盘
		username.setFocusable(true);
		username.setFocusableInTouchMode(true);
		username.requestFocus();
	    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}
	
	public void login(View v) {
		final String username_text = username.getText().toString();
		final String password_text = password.getText().toString();
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(this, "登陆中,请勿执行其他操作！");
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/user/login?username=" + username_text + "&password=" + password_text, new Callback() {

			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.dismissLoading2(showLoading2);
				NetworkUtils.showErrorMessage(LoginActivity.this, getMessage());
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String object = response.body().string();
				final User user = GsonUtils.getGsonWithLocalDate(new TypeToken<User>() {}, object);
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(user.getId() == null || "0".equals(user.getLoginMessage())){
							Toast.makeText(LoginActivity.this, "用户名或密码出错!", 0).show();
						}else {
							//添加第一个页面的Fragment
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							intent.putExtra("user", user);
							startActivity(intent);
							finish();
						}
						NetworkUtils.dismissLoading2(showLoading2);
					}
				});
			}
		});
	}
	
}
