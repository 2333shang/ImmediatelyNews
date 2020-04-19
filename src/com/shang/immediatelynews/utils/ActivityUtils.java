package com.shang.immediatelynews.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityUtils {

	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivities(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivities(Activity activity) {
		activities.remove(activity);
	}
	
	public static void finishAll() {
		for(Activity a:activities) {
			if(!a.isFinishing()) {
				a.finish();
			}
		}
		activities.clear();
	}
}
