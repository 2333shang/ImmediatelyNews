package com.shang.immediatelynews;

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
import com.shang.immediatelynews.entities.User;
import com.shang.immediatelynews.fragment.OwnerFragment;
import com.shang.immediatelynews.fragment.TopFragment;
import com.shang.immediatelynews.fragment.TypeCompanyFragment;
import com.shang.immediatelynews.fragment.VideoFragment;
import com.shang.immediatelynews.utils.ActivityUtils;
import com.shang.immediatelynews.utils.GlideUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import de.hdodenhof.circleimageview.CircleImageView;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@ViewInject(R.id.toolbar)
	private Toolbar toolbar;
	@ViewInject(R.id.drawer)
	private DrawerLayout drawer;
	@ViewInject(R.id.nav_view)
	private NavigationView nav_view;
	@ViewInject(R.id.bottom_title_radio_group)
	private RadioGroup radiogroup;
	private User user;
	private CircleImageView iv;
	private TextView username;
	
	private TopFragment topFragment;
	private OwnerFragment ownerFragment;
	private VideoFragment videoFragment;
	private TypeCompanyFragment typeFragment;
	private Fragment currentFragment;//当前Fragment

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//初始化xUtils
		x.view().inject(this);
		ActivityUtils.addActivities(this);
		user = (User)getIntent().getSerializableExtra("user");
		setToorbar();
		setSlideMenu(user);
		setTop();
		//默认选中第一项
		radiogroup.check(R.id.bottom_title_radio_top);
		//监听RadioButton点击并切换不同的页面
		replaceFragmentByRadioButton();
		//设置NavigationView默认选中项
//		nav_view.setCheckedItem(R.id.nav_search);
		//设置NavigationView的选中监听
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
					startActivityForResult(intent, 3);
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

	private void setSlideMenu(User user) {
		if("0".equals(user.getStatus())){
			nav_view.inflateMenu(R.menu.nav_menu2);
		}else if("1".equals(user.getStatus())){
			nav_view.inflateMenu(R.menu.nav_menu);
		}else if("2".equals(user.getStatus())){
			nav_view.inflateMenu(R.menu.nav_menu_admin);
		}
		View headerLayout = nav_view.inflateHeaderView(R.layout.nav_head);
		iv = (CircleImageView) headerLayout.findViewById(R.id.cicle_image);
		username = (TextView) headerLayout.findViewById(R.id.username);
		if(user.getHeadIcon()!=null) {
			GlideUtils.loadImage(this, iv, user.getHeadIcon().getUrl());
		}else {
			GlideUtils.loadImage(this, iv, FileUploadConstant.FILE_NET + FileUploadConstant.FILE_CONTEXT_PATH + FileUploadConstant.FILE_REAL_PATH + FileUploadConstant.FILE_DEFAULT_HEAD);
		}	
		username.setText(user.getUsername());	
	}

	private void setTop() {
		if(topFragment == null)
			topFragment = new TopFragment();
		currentFragment = topFragment;
		//添加第一个页面的Fragment
		getSupportFragmentManager().beginTransaction().add(R.id.main_container, topFragment).commit();
	}
	
	private void replaceFragmentByRadioButton() {
		radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				switch(checkedId) {
				case R.id.bottom_title_radio_top:
					if(topFragment == null)
						topFragment = new TopFragment();
					fragmentTransaction.hide(currentFragment);
					currentFragment = topFragment;
					fragmentTransaction.show(topFragment).commit();
					break;
				case R.id.bottom_title_radio_ours:
					if(ownerFragment == null)
						ownerFragment = new OwnerFragment();
					fragmentTransaction.hide(currentFragment);
					currentFragment = ownerFragment;
					if(!ownerFragment.isAdded()){
						fragmentTransaction.add(R.id.main_container, ownerFragment).show(ownerFragment).commit();
					}else {
						fragmentTransaction.show(ownerFragment).commit();
					}
					break;
				case R.id.bottom_title_radio_video:
					if(videoFragment == null)
						videoFragment = new VideoFragment();
					fragmentTransaction.hide(currentFragment);
					currentFragment = videoFragment;
					if(!videoFragment.isAdded()){
						fragmentTransaction.add(R.id.main_container, videoFragment).show(videoFragment).commit();
					}else {
						fragmentTransaction.show(videoFragment).commit();
					}
					break;
				case R.id.bottom_title_radio_company:
					if(typeFragment == null)
						typeFragment = new TypeCompanyFragment();
					fragmentTransaction.hide(currentFragment);
					currentFragment = typeFragment;
					if(!typeFragment.isAdded()){
						fragmentTransaction.add(R.id.main_container, typeFragment).show(typeFragment).commit();
					}else {
						fragmentTransaction.show(typeFragment).commit();
					}
					break;
				default:
					break;
				}
			}
		});
	}

	private void setToorbar() {
		//设置标题
		toolbar.setTitle(user.getCompanyName());
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 3 && resultCode == 2) {
			String headicon = data.getStringExtra("headicon");
			String username_str = data.getStringExtra("username");
			if(headicon!=null) {
				GlideUtils.loadImage(this, iv, headicon);
			}
			if(username_str!=null && !username_str.equals(username.getText().toString())){
				username.setText(username_str);
			}
		}
	}
	
	private Boolean backFlag = false;
	private Handler back_handler = new Handler() {
		
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				backFlag = false;
				break;

			default:
				break;
			}
	    }
	};
	
	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		if(!backFlag) {
			Toast.makeText(this, "再按一次退出", 0).show();
			back_handler.sendEmptyMessageDelayed(1, 2000);
			backFlag = true;
		}else {
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityUtils.removeActivities(this);
	}
}
