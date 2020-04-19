package com.shang.immediatelynews.boradcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetWorkBroadcastReceiver extends BroadcastReceiver {

	private NetworkReceiver receiver;

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			int type = networkInfo.getType();
			String typeName = networkInfo.getTypeName();
			switch (type) {
			case 0:// 移动 网络 2G 3G 4G 都是一样的 实测 mix2s 联通卡
				receiver.getMsg("0");
				break;
			case 1: // wifi网络
				receiver.getMsg("1");
				break;
			case 9: // 网线连接
				receiver.getMsg("9");
				break;
			}
		} else {// 无网络
			receiver.getMsg("-1");
		}
	}

	public interface NetworkReceiver {
		public void getMsg(String message);
	}

	public void setNetworkReceiver(NetworkReceiver receiver) {
		this.receiver = receiver;
	}
}
