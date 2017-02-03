package com.example.test1;

import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Conn {
	/*-----------------------------    发布版本修改信息      ---------------------------------------*/
	// URL,webview
	public static String URL = "http://124.95.128.252:2323/Home/Index?";//"http://ser.syccy.com/";//"http://124.95.128.252:8083/";//192.168.0.173
	
	
	public static String phoneNumber = "";
	
	public static String deviceID = "";
	
	public static String getPhoneNumber(Context context){
		String phoneNum = "0";
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			phoneNum = tm.getLine1Number();
			phoneNum = phoneNum.substring(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(phoneNum)) {
			phoneNum = "0";
		}
		return phoneNum;
	}
	
	public static String getDeviceInfo(Context context) {
	    try{
	      JSONObject json = new JSONObject();
	      TelephonyManager tm = (TelephonyManager) context
	          .getSystemService(Context.TELEPHONY_SERVICE);

	      String device_id = tm.getDeviceId();

	      WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	      String mac = wifi.getConnectionInfo().getMacAddress();
	      json.put("mac", mac);

	      if( TextUtils.isEmpty(device_id) ){
	        device_id = mac;
	      }

	      if( TextUtils.isEmpty(device_id) ){
	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
	      }
	      json.put("device_id", device_id);
	      return device_id;
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  return "0";
	}
	
	//获取本地手机参数
	public static String getParamString(String param){
//		if (TextUtils.isEmpty(param)) {
//			return "";
//		}
		StringBuffer result = new StringBuffer("");
//		try {
//			if (param.contains("?")) {
//				result.append("&");
//			}else {
//				result.append("?");
//			}
//			if (isLogin()) {
//				result.append("ActionPhone=").append(loginUser.getMen_Phone()).append("&");
//				result.append("ActionMemID=").append(loginUser.getMem_ID()).append("&");
//			}else {
//				if (TextUtils.isEmpty(phoneNumber)) {
//					phoneNumber = getPhoneNumber(context1);
//				}
//				result.append("ActionPhone=").append(phoneNumber).append("&");
//				result.append("ActionMemID=").append("0").append("&");
//			}
//			if (TextUtils.isEmpty(deviceID)) {
//				deviceID = getDeviceInfo(context1);
//			}
//			result.append("ActionPhoneKey=").append(deviceID).append("&");
//			result.append("ActionPhoneType=Android");
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
		return result.toString();
	}
	
	
	
	
	
	
	
}
