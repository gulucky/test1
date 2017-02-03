package com.example.test1;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	    private MediaPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);
		try {
			JSONObject printJsonObject = new JSONObject();
			
			JSONArray jsonArray = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("key", "acc");
			jsonObject.put("value", "login");
			JSONObject jsonObject1 = new JSONObject();
			jsonObject1.put("key", "token");
			jsonObject1.put("value", "token");
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("key", "type");
			jsonObject2.put("value", "type");
			
			jsonArray.put(jsonObject);
			jsonArray.put(jsonObject1);
			jsonArray.put(jsonObject2);
			
			printJsonObject.put("cparam", jsonArray);
			Log.w("uuu", printJsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPause() {
		try {
			player.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onPause();
	}
	
	
	
	// �����˳�����
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				this.startActivity(intent);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
		
}
