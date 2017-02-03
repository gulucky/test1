package com.example.test1;

import java.lang.reflect.Method;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

public class AudioManagerUtil {
	
	private static final String SPEAK_PHONT_FLAG = "speak_phone";
	
	/**
	 * 设置外放模式还是听筒模式
	 * @param on true 外放 false 听筒
	 * @param context
	 */
	public static void setSpeakerphoneOn(boolean on, Context context) {
		try {
			AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
//			if (on) {
//				audioManager.setSpeakerphoneOn(true);
//				audioManager.setMode(AudioManager.MODE_NORMAL);
//			} else {
//				audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//				audioManager.setSpeakerphoneOn(false);// 关闭扬声器
//				audioManager.setRouting(AudioManager.MODE_NORMAL,
//						AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
//				// setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//				// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
//				//设置为通话状态  
//                ((Activity)context).setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);  
//			}
			
			if (on) {
				audioManager.setMicrophoneMute(false);
				audioManager.setSpeakerphoneOn(true);
				audioManager.setMode(AudioManager.MODE_NORMAL);

				// setForceUse.invoke(null, 1, 1);
			} else {
				// 播放音频流类型
				((Activity) context)
						.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
				audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);// 得到听筒模式的最大值
				audioManager.setMicrophoneMute(false);
				audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
				audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
				// 获得当前类
				Class audioSystemClass = Class
						.forName("android.media.AudioSystem");
				// 得到这个方法
				Method setForceUse = audioSystemClass.getMethod("setForceUse",
						int.class, int.class);
				audioManager.setSpeakerphoneOn(false);
				setForceUse.invoke(null, AudioManager.MODE_IN_COMMUNICATION, 0);
				
			}
			saveSpeakerphoneOnToSharedPreferences(on, context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 保存声音播放模式信息到本地
	 * @param on true 外放 false 听筒
	 */
	public static void saveSpeakerphoneOnToSharedPreferences(boolean on,Context context){
		try {
			SharedPreferences mySharedPreferences = context.getSharedPreferences(
					SPEAK_PHONT_FLAG , Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = mySharedPreferences
					.edit();
			editor.putBoolean("Speakerphone", on);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取声音播放模式信息(默认为外放)
	 * @param context
	 * @return
	 */
	public static boolean getSpeakerphoneFromSharedPreferences(Context context){
		boolean on = true;
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					SPEAK_PHONT_FLAG , Activity.MODE_PRIVATE);
			on = sharedPreferences.getBoolean("Speakerphone", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return on;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
