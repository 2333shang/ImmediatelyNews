package com.shang.immediatelynews.activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shang.immediatelynews.R;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.entities.User;
import com.shang.immediatelynews.utils.HttpRequestUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_user_info)
public class UserInfoActivity extends Activity {

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
		
		getUserInfoDatas();
	}

	protected void setUserInfo() {
//		user_headicon
		user_name.setText(user.getUsername());
		user_gender.setText(user.getGender());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		user_birth.setText(format.format(user.getBirthdate()));
		user_company.setText(user.getCompanyId());;
	}

	private void getUserInfoDatas() {
		String url = FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/user/userinfo";
		HttpRequestUtils.getRequest(url, new Callback() {
			
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String object = response.body().string();
				Gson gson = new GsonBuilder()
						.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.create();
				Type type = new TypeToken<User>(){}.getType();
				user = gson.fromJson(object, type);
				user_handler.sendEmptyMessage(1);
			}
			
			@Override
			public void onFailure(Call call, IOException exception) {
				
			}
		});
	}
}
