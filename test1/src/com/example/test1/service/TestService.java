package com.example.test1.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class TestService extends Service{
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
		Toast.makeText(getApplicationContext(), "���Է�������", Toast.LENGTH_LONG).show();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
