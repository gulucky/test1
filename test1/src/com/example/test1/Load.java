package com.example.test1;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;

public class Load extends BaseActivity {
	// 服务器返回数据首页大图，首页列表
	String resultPicture = "";
	private static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	//加载条
	private ImageView mSplashItem_iv;
	
	private RelativeLayout waitLoadLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);
		init();
		sendRequest("载入页面");
	}
	
	// 初始化后运行程序
	private void init() {
		new Thread(new Runnable() {
			public void run() {
				SystemClock.sleep(2000);
				runOnUiThread(new Runnable() {
					public void run() {
						Intent intent = new Intent();
						intent.setClass(Load.this, MainActivity.class);
						Load.this.startActivity(intent);
						Load.this.finish();
					}
				});
			}
		}).start();
	}

	// 加载退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			MobclickAgent.onKillProcess(this);
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
