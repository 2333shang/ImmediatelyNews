package com.shang.immediatelynews;

import org.xutils.x;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import com.shang.immediatelynews.fragment.OrderFragment;
import com.shang.immediatelynews.fragment.TopFragment;
import com.shang.immediatelynews.fragment.TypeFragment;
import com.shang.immediatelynews.fragment.VideoFragment;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//初始化xUtils
		x.view().inject(this);
		//默认选中第一项
		radiogroup.check(R.id.bottom_title_radio_top);
		//添加第一个页面的Fragment
		TopFragment topFragment = new TopFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.main_container, topFragment).commit();
		//自定义ToolBar并替换默认的ActionBar
		setToorbar();
		//监听RadioButton点击并切换不同的页面
		replaceFragmentByRadioButton();
		//设置NavigationView默认选中项
		nav_view.setCheckedItem(R.id.nav_search);
		//设置NavigationView的选中监听
		nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			
			@Override
			public boolean onNavigationItemSelected(MenuItem arg0) {
				drawer.closeDrawers();
				return true;
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
					fragmentTransaction.replace(R.id.main_container, topFragment).commit();
					break;
				case R.id.bottom_title_radio_video:
					VideoFragment videoFragment = new VideoFragment();
					fragmentTransaction.replace(R.id.main_container, videoFragment).commit();
					break;
				case R.id.bottom_title_radio_type:
					TypeFragment typeFragment = new TypeFragment();
					fragmentTransaction.replace(R.id.main_container, typeFragment).commit();
					break;
				case R.id.bottom_title_radio_order:
					OrderFragment orderFragment = new OrderFragment();
					fragmentTransaction.replace(R.id.main_container, orderFragment).commit();
					break;
				default:
					break;
				}
			}
		});
	}

	private void setToorbar() {
		//设置标题
		toolbar.setTitle("haha");
		//设置子标题
		toolbar.setSubtitle("笑屁呀");
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
		getMenuInflater().inflate(R.menu.toolbar_test, menu);
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
	
	public void addTabItem(View v) {
		Toast.makeText(this, "Hello", 0).show();
	}
}
