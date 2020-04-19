package com.shang.immediatelynews;

import com.shang.immediatelynews.boradcast.NetWorkBroadcastReceiver;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity implements NetWorkBroadcastReceiver.NetworkReceiver {
	
	private NetWorkBroadcastReceiver receiver;
	private String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		receiver = new NetWorkBroadcastReceiver();
		receiver.setNetworkReceiver(this);
	}

	@Override
	public void getMsg(String message) {
		this.message = message;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		if(receiver == null) {
			receiver = new NetWorkBroadcastReceiver();
			receiver.setNetworkReceiver(this);			
		}
		registerReceiver(receiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

	public String getMessage() {
		return message;
	}
}
