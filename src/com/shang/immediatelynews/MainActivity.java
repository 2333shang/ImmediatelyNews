package com.shang.immediatelynews;

import java.io.IOException;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.activity.AddNewsActivity;
import com.shang.immediatelynews.activity.AddVideoActivity;
import com.shang.immediatelynews.activity.CollectActivity;
import com.shang.immediatelynews.activity.NewsManagerActivity;
import com.shang.immediatelynews.activity.OrderActivity;
import com.shang.immediatelynews.activity.TopManagerActivity;
import com.shang.immediatelynews.activity.UserInfoActivity;
import com.shang.immediatelynews.constant.FileUploadConstant;
import com.shang.immediatelynews.fragment.OwnerFragment;
import com.shang.immediatelynews.fragment.TopFragment;
import com.shang.immediatelynews.fragment.TypeCompanyFragment;
import com.shang.immediatelynews.fragment.VideoFragment;
import com.shang.immediatelynews.utils.HttpRequestUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

	@ViewInject(R.id.toolbar)
	private Toolbar toolbar;
	@ViewInject(R.id.drawer)
	private DrawerLayout drawer;
	@ViewInject(R.id.nav_view)
	private NavigationView nav_view;
	@ViewInject(R.id.bottom_title_radio_group)
	private RadioGroup radiogroup;
	private Bundle args;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//初始化xUtils
		x.view().inject(this);
		getTopNews();
		//默认选中第一项
		radiogroup.check(R.id.bottom_title_radio_top);
		//自定义ToolBar并替换默认的ActionBar
		setToorbar();
		//监听RadioButton点击并切换不同的页面
		replaceFragmentByRadioButton();
		//设置NavigationView默认选中项
//		nav_view.setCheckedItem(R.id.nav_search);
		//设置NavigationView的选中监听
		setSlideMenu();
		nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {
				drawer.closeDrawers();
				switch (item.getItemId()) {
				case R.id.nav_order:
					Intent intent = new Intent(MainActivity.this, OrderActivity.class);
					startActivity(intent);
					break;
				case R.id.nav_collect:
					intent = new Intent(MainActivity.this, CollectActivity.class);
					startActivity(intent);
					break;
				case R.id.nav_owner:
					intent = new Intent(MainActivity.this, UserInfoActivity.class);
					startActivity(intent);
					break;
				case R.id.nav_news:
					intent = new Intent(MainActivity.this, NewsManagerActivity.class);
					startActivity(intent);
					break;
				case R.id.nav_top:
					intent = new Intent(MainActivity.this, TopManagerActivity.class);
					startActivity(intent);
					break;
				case R.id.nav_create:
					intent = new Intent(MainActivity.this, AddNewsActivity.class);
					startActivity(intent);
					break;
				case R.id.nav_create_video:
					intent = new Intent(MainActivity.this, AddVideoActivity.class);
					startActivity(intent);
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	private void setSlideMenu() {
		nav_view.inflateMenu(R.menu.nav_menu_admin);
	}

	private void getTopNews() {
		HttpRequestUtils.getRequest(FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + "/top/seltop", new Callback() {

			@Override
			public void onFailure(Call call, IOException exception) {
				Log.d("news", "联网失败");
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				args = new Bundle();
				args.putString("topNews", response.body().string());
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						//添加第一个页面的Fragment
						TopFragment topFragment = new TopFragment();
						topFragment.setArguments(args);
						getSupportFragmentManager().beginTransaction().add(R.id.main_container, topFragment).commit();
					}
				});
			}
			
		});
	}
	
	private void replaceFragmentByRadioButton() {
		radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				switch(checkedId) {
				case R.id.bottom_title_radio_top:
					TopFragment topFragment = new TopFragment();
					topFragment.setArguments(args);
					fragmentTransaction.replace(R.id.main_container, topFragment).commit();
					break;
				case R.id.bottom_title_radio_ours:
					OwnerFragment ownerFragment = new OwnerFragment();
					fragmentTransaction.replace(R.id.main_container, ownerFragment).commit();
					break;
				case R.id.bottom_title_radio_video:
					VideoFragment videoFragment = new VideoFragment();
					fragmentTransaction.replace(R.id.main_container, videoFragment).commit();
					break;
				case R.id.bottom_title_radio_company:
					TypeCompanyFragment typeFragment = new TypeCompanyFragment();
					fragmentTransaction.replace(R.id.main_container, typeFragment).commit();
					break;
				default:
					break;
				}
			}
		});
	}

	private void setToorbar() {
		//设置标题
		toolbar.setTitle("中环集团");
		//设置子标题
		toolbar.setSubtitle("第五研究所");
//		toolbar.setNavigationIcon(R.drawable.ic_launcher);
		//替换默认的ActionBar为ToolBar
		setSupportActionBar(toolbar);
		//得到替换过后的ActionBar，实际上是ToolBar
		ActionBar actionBar = getSupportActionBar();
		//设置左侧导航
		actionBar.setDisplayHomeAsUpEnabled(true);
		//设置导航图案
		actionBar.setHomeAsUpIndicator(R.drawable.ic_action_user_press);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.toolbar_test, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			drawer.openDrawer(Gravity.START);
		}
		return super.onOptionsItemSelected(item);
	}
}
