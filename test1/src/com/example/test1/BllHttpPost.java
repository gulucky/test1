package com.example.test1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Environment;
import android.util.Base64;

public class BllHttpPost {

//	public static String send(String url, String List) {
//		String result = "";
////		String target = Conn.HttpUrl + url; // Ҫ�ύ��Ŀ���ַ
//		HttpClient httpclient = new DefaultHttpClient(); // ����HttpClient����
//		HttpPost httpRequest = new HttpPost(target); // ����HttpPost����
//		// ��Ҫ���ݵĲ������浽List������
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("List", List)); // ��ǲ���
//		// params.add(new BasicNameValuePair("nickname",
//		// nickname.getText().toString())); //�ǳ�
//		// params.add(new BasicNameValuePair("content",
//		// content.getText().toString())); //����
//		try {
//			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // ���ñ��뷽ʽ
//			HttpResponse httpResponse = httpclient.execute(httpRequest); // ִ��HttpClient����
//			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // �������ɹ�
//				result += EntityUtils.toString(httpResponse.getEntity()); // ��ȡ���ص��ַ���
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	public static String sendUrl(String url,List<NameValuePair> params) {
		String result = "";
		String target = Conn.URL+url;	//Ҫ�ύ��Ŀ���ַ
		HttpClient httpclient = new DefaultHttpClient();	//����HttpClient����
		HttpPost httpRequest = new HttpPost(target);	//����HttpPost����
		//params.add(new BasicNameValuePair("nickname", nickname.getText().toString()));	//�ǳ�
		//params.add(new BasicNameValuePair("content", content.getText().toString()));	//����
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //���ñ��뷽ʽ
			HttpResponse httpResponse = httpclient.execute(httpRequest);	//ִ��HttpClient����
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//�������ɹ�
				result += EntityUtils.toString(httpResponse.getEntity());	//��ȡ���ص��ַ���
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String sendUrlChe(String url , String str) {
		String result = "";
		 try {
			// �����ռ�  
			String nameSpace = "WcfService";  
			// ���õķ�������  
			String methodName = "FileUploadByBase64String";  
  
			// ָ��WebService�������ռ�͵��õķ�����  
			SoapObject rpc = new SoapObject(nameSpace, methodName);  
  
			// ���������WebService�ӿ���Ҫ�������������mobileCode��userId  
//        	rpc.addProperty("mobileCode", phoneSec);  
			String localUrl = Environment.getExternalStorageDirectory().getPath() + "/aaa.jpg";
//			rpc.addProperty("base64string", GetImageStr());
//			rpc.addProperty("fileName", "aaa.jpg");
//			rpc.addProperty("DirName", "aaa");  
//			rpc.addProperty("Limit", "");  
//			rpc.addProperty("FileName", localUrl);  
//			rpc.addProperty("Stream", new FileInputStream(new File(localUrl)));  
//			rpc.addProperty("IsSuccessed", true);  
  
			// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾  
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
  
			envelope.bodyOut = rpc;  
			// �����Ƿ���õ���dotNet������WebService  
			envelope.dotNet = true;  
			// �ȼ���envelope.bodyOut = rpc;  
			envelope.setOutputSoapObject(rpc);  
  
			HttpTransportSE transport = new HttpTransportSE("http://192.168.0.173:8084/Service.svc?wsdl");  
			try {  
			    // ����WebService  
			    transport.call("WcfService", envelope);  
			    if(envelope.getResponse() != null){
			        SoapObject bodyObject = (SoapObject) envelope.bodyIn;
			        result = bodyObject.getProperty(0).toString();
			    }else{
			    	
			    }
			} catch (Exception e) {  
			    e.printStackTrace();  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
		return result;
	}
	
	public static String GetImageStr() {// ��ͼƬ�ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦��
		String imgFile = "d:\\hwy.jpg";// �������ͼƬ
		String localUrl = Environment.getExternalStorageDirectory().getPath()
				+ "/aaa.jpg";
		InputStream in = null;
		byte[] data = null;
		// ��ȡͼƬ�ֽ�����
		try {
			in = new FileInputStream(localUrl);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ���ֽ�����Base64����
		return Base64.encodeToString(data, Base64.DEFAULT);
		// return encoder.encode(data);//����Base64��������ֽ������ַ���
	}
	
	/**
	 * ����ͼƬ
	 * @param url		
	 * @param fileUrl
	 * @return
	 */
	public static String sendPicture(){
		String result = "";
		String target = "http://192.168.0.173:8084/WebService/FileUploadByBase64String"; // Ҫ�ύ��Ŀ���ַ
		try {
			String localUrl = Environment.getExternalStorageDirectory().getPath() + "/bbb.mp3";
			HttpPost httpRequest = new HttpPost(target); // ����HttpPost����
			HttpClient httpClient = new DefaultHttpClient();
			MultipartEntity mpEntity = new MultipartEntity(); //�ļ�����
			mpEntity.addPart("aaa", new FileBody(new File(localUrl)));
			httpRequest.setEntity(mpEntity);
			HttpResponse httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // �������ɹ�
				result += EntityUtils.toString(httpResponse.getEntity()); // ��ȡ���ص��ַ���
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * ����ͼƬ
	 * @param url		
	 * @param fileUrl
	 * @return
	 */
	public static String sendPicture(String url, String fileUrl){
		String result = "";
		String target = "http://192.168.0.173:8732/AndroidUpload.svc/update_pictrue"; // Ҫ�ύ��Ŀ���ַ
		String localUrl = Environment.getExternalStorageDirectory().getPath() + "/aaa.jpg";
		try {
//			rpc.addProperty("DirName", "aaa");  
//			rpc.addProperty("Limit", "");  
//			rpc.addProperty("FileName", localUrl);  
//			rpc.addProperty("Stream", new FileInputStream(new File(localUrl)));  
//			rpc.addProperty("IsSuccessed", true);  
			HttpPost httpRequest = new HttpPost(target); // ����HttpPost����
			HttpClient httpClient = new DefaultHttpClient();
//			httpRequest.setHeader("Content-Type", "application/soap+xml; charset=utf-8");
//			setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
			MultipartEntity mpEntity = new MultipartEntity(); //�ļ�����
//			mpEntity.addPart("Stream", new FileBody(new File(localUrl)));
//			mpEntity.addPart("DirName", new StringBody("aaa"));
//			mpEntity.addPart("Limit", new StringBody(""));
//			mpEntity.addPart("FileName", new StringBody(localUrl));
//			mpEntity.addPart("IsSuccessed", new StringBody(""));
			httpRequest.setEntity(mpEntity);
			HttpResponse httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // �������ɹ�
				result += EntityUtils.toString(httpResponse.getEntity()); // ��ȡ���ص��ַ���
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	public static byte[] httpPost(String url, String entity) {
		if (url == null || url.length() == 0) {
			return null;
		}

		HttpClient httpClient = getNewHttpClient();

		HttpPost httpPost = new HttpPost(url);

		try {
			httpPost.setEntity(new StringEntity(entity));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				return null;
			}

			return EntityUtils.toByteArray(resp.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	private static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
