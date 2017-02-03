package com.example.test1;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class BllHttpGet {
	
	
	public static void sendStatisticsRequest(final String param) {
		try {
			String target = Conn.URL + "Passenger/LogUserVisitCount?ActionControllerName=" + param + Conn.getParamString("?");	//Ҫ�ύ��Ŀ���ַ
			HttpClient httpclient = new DefaultHttpClient();//����HttpClient����
			HttpGet httpRequest = new HttpGet(target);	//����HttpGet���Ӷ���
			HttpResponse httpResponse;
			httpResponse = httpclient.execute(httpRequest);	//ִ��HttpClient����
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
			}
		} catch (Exception e) {
			e.getMessage();
		} 
	}
	
	
	public static String sendUrl(String param) {
		String result = "";
		try {

			String target = Conn.URL + param + Conn.getParamString(param); // Ҫ�ύ��Ŀ���ַ
			HttpClient httpclient = new DefaultHttpClient();// ����HttpClient����
			HttpGet httpRequest = new HttpGet(target); // ����HttpGet���Ӷ���
			HttpResponse httpResponse;

			httpResponse = httpclient.execute(httpRequest); // ִ��HttpClient����
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity()); // ��ȡ���ص��ַ���
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
	
	public static String sendMessage(String param) {
		String result="";
		String target = Conn.URL + param + Conn.getParamString(param);	//Ҫ�ύ��Ŀ���ַ
		HttpClient httpclient = new DefaultHttpClient();//����HttpClient����
		HttpGet httpRequest = new HttpGet(target);	//����HttpGet���Ӷ���
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpRequest);	//ִ��HttpClient����
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				result = EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
			}
		} catch (Exception e) {
			e.getMessage();
		} 
		return result;
	}
	
}
