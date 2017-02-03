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
	 * �������ģʽ������Ͳģʽ
	 * @param on true ��� false ��Ͳ
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
//				audioManager.setSpeakerphoneOn(false);// �ر�������
//				audioManager.setRouting(AudioManager.MODE_NORMAL,
//						AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
//				// setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//				// �������趨��Earpiece����Ͳ���������趨Ϊ����ͨ����
//				//����Ϊͨ��״̬  
//                ((Activity)context).setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);  
//			}
			
			if (on) {
				audioManager.setMicrophoneMute(false);
				audioManager.setSpeakerphoneOn(true);
				audioManager.setMode(AudioManager.MODE_NORMAL);

				// setForceUse.invoke(null, 1, 1);
			} else {
				// ������Ƶ������
				((Activity) context)
						.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
				audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);// �õ���Ͳģʽ�����ֵ
				audioManager.setMicrophoneMute(false);
				audioManager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
				audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
				// ��õ�ǰ��
				Class audioSystemClass = Class
						.forName("android.media.AudioSystem");
				// �õ��������
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
	 * ������������ģʽ��Ϣ������
	 * @param on true ��� false ��Ͳ
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
	 * ��ȡ��������ģʽ��Ϣ(Ĭ��Ϊ���)
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
