package com.example.test1;

import android.content.Context;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECNotifyOptions;

public class YTXSDKCoreHelper1 implements ECDevice.InitListener {
	
	private static YTXSDKCoreHelper1 sInstance;

	private YTXSDKCoreHelper1() {
	}

	public static YTXSDKCoreHelper1 getInstance() {
		if (sInstance == null) {
			synchronized (YTXSDKCoreHelper1.class) {
				if (sInstance == null) {
					sInstance = new YTXSDKCoreHelper1();
				}
			}
		}
		return sInstance;
	}

	@Override
	public void onError(Exception arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitialized() {
		// TODO Auto-generated method stub
		
	}
}
