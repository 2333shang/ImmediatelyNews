package com.shang.immediatelynews.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.BaseActivity;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.User;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.GlideUtils;
import com.shang.immediatelynews.utils.GsonUtils;
import com.shang.immediatelynews.utils.HttpRequestUtils;
import com.shang.immediatelynews.utils.NetworkUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_user_info)
public class UserInfoActivity extends BaseActivity {

	@ViewInject(R.id.user_headicon)
	private CircleImageView user_headicon;
	@ViewInject(R.id.user_name)
	private TextView user_name;
	@ViewInject(R.id.user_gender)
	private TextView user_gender;
	@ViewInject(R.id.user_birth)
	private TextView user_birth;
	@ViewInject(R.id.user_company)
	private TextView user_company;
	
	private User user;
	private Handler user_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setUserInfo();
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
		getUserInfoDatas();
	}

	protected void setUserInfo() {
		if(user.getHeadIcon()!=null)
			GlideUtils.loadImage(this, user_headicon, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + user.getHeadIcon().getUrl());
		user_name.setText(user.getUsername());
		user_gender.setText(user.getGender());
		if(user.getBirthdate() != null) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			user_birth.setText(format.format(user.getBirthdate()));
		}
		user_company.setText(user.getCompanyName());;
	}

	private void getUserInfoDatas() {
		final ProgressDialog showLoading2 = NetworkUtils.showLoading2(this, "数据加载中！");
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/user/userinfo";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				NetworkUtils.dismissLoading2(showLoading2);
				String object = response.body().string();
				if("login_invalid".equals(object)) {
					NetworkUtils.toSessionInvalid(UserInfoActivity.this);
				}else {
					user = GsonUtils.getGsonWithLocalDate(new TypeToken<User>(){}, object);
					user_handler.sendEmptyMessage(1);
				}
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				NetworkUtils.dismissLoading2(showLoading2);
				NetworkUtils.showErrorMessage(UserInfoActivity.this, getMessage());
			}
		});
	}
	
	public void cancel(View v) {
		finish();
	}
	
	public void update(View v) {
		Intent intent = new Intent(this, UserInfoUpdateActivity.class);
		intent.putExtra("user", user);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode ==1 & resultCode == 2) {
			User user = (User) data.getSerializableExtra("updateuser");
			if(!user_name.getText().toString().equals(user.getUsername())) {
				user_name.setText(user.getUsername());
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String datestr = format.format(user.getBirthdate());
			if(!user_birth.getText().toString().equals(datestr)) {
				user_birth.setText(datestr);
			}
			if(!user_gender.getText().toString().equals(user.getGender())) {
				user_gender.setText(user.getGender());
			}
			if(user.getHeadIcon() != null) {
				Intent result = new Intent();
				result.putExtra("headicon", user.getHeadIcon().getUrl());
				result.putExtra("username", user.getUsername());
				setResult(resultCode, result);
				GlideUtils.loadImage(this, user_headicon, user.getHeadIcon().getUrl());
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
