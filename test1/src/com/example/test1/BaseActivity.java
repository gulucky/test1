package com.example.test1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity{
	
	private static List<Activity> activities = new ArrayList<Activity>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities.add(this);
		if (savedInstanceState != null) {
			finish();
			return;
		}
	}
	
	public void sendRequest(final String content){
//		new Thread(new Runnable() {
//			public void run() {
//				String content1 = "";
//				try {
//					content1 = URLEncoder.encode(content, "utf-8");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				BllHttpGet.sendUrl("PhoneKey=" + Conn.getDeviceInfo(BaseActivity.this) 
//						+ "&PageName=" + content1);
//			}
//		}).start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashScreen");
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashScreen");
		MobclickAgent.onPause(this);
	}
	
	public static void clearSameActivity(Activity activity){
		try {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).getClass().getName().equals(activity.getClass().getName())) {
					activities.get(i).finish();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		activities.remove(this);
		super.onDestroy();
	}
	
	public static void addActivity(Activity activity){
		try {
			activities.add(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeActivity(Activity activity){
		try {
			activities.remove(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void clearAllActivitys(){
		for (int i = 0; i < activities.size(); i++) {
			try {
				activities.get(i).finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void showToastOnActivity(final String text){
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
