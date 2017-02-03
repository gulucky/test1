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
////		String target = Conn.HttpUrl + url; // 要提交的目标地址
//		HttpClient httpclient = new DefaultHttpClient(); // 创建HttpClient对象
//		HttpPost httpRequest = new HttpPost(target); // 创建HttpPost对象
//		// 将要传递的参数保存到List集合中
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("List", List)); // 标记参数
//		// params.add(new BasicNameValuePair("nickname",
//		// nickname.getText().toString())); //昵称
//		// params.add(new BasicNameValuePair("content",
//		// content.getText().toString())); //内容
//		try {
//			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 设置编码方式
//			HttpResponse httpResponse = httpclient.execute(httpRequest); // 执行HttpClient请求
//			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
//				result += EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
	
	public static String sendUrl(String url,List<NameValuePair> params) {
		String result = "";
		String target = Conn.URL+url;	//要提交的目标地址
		HttpClient httpclient = new DefaultHttpClient();	//创建HttpClient对象
		HttpPost httpRequest = new HttpPost(target);	//创建HttpPost对象
		//params.add(new BasicNameValuePair("nickname", nickname.getText().toString()));	//昵称
		//params.add(new BasicNameValuePair("content", content.getText().toString()));	//内容
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //设置编码方式
			HttpResponse httpResponse = httpclient.execute(httpRequest);	//执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
				result += EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String sendUrlChe(String url , String str) {
		String result = "";
		 try {
			// 命名空间  
			String nameSpace = "WcfService";  
			// 调用的方法名称  
			String methodName = "FileUploadByBase64String";  
  
			// 指定WebService的命名空间和调用的方法名  
			SoapObject rpc = new SoapObject(nameSpace, methodName);  
  
			// 设置需调用WebService接口需要传入的两个参数mobileCode、userId  
//        	rpc.addProperty("mobileCode", phoneSec);  
			String localUrl = Environment.getExternalStorageDirectory().getPath() + "/aaa.jpg";
//			rpc.addProperty("base64string", GetImageStr());
//			rpc.addProperty("fileName", "aaa.jpg");
//			rpc.addProperty("DirName", "aaa");  
//			rpc.addProperty("Limit", "");  
//			rpc.addProperty("FileName", localUrl);  
//			rpc.addProperty("Stream", new FileInputStream(new File(localUrl)));  
//			rpc.addProperty("IsSuccessed", true);  
  
			// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本  
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);  
  
			envelope.bodyOut = rpc;  
			// 设置是否调用的是dotNet开发的WebService  
			envelope.dotNet = true;  
			// 等价于envelope.bodyOut = rpc;  
			envelope.setOutputSoapObject(rpc);  
  
			HttpTransportSE transport = new HttpTransportSE("http://192.168.0.173:8084/Service.svc?wsdl");  
			try {  
			    // 调用WebService  
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
	
	public static String GetImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile = "d:\\hwy.jpg";// 待处理的图片
		String localUrl = Environment.getExternalStorageDirectory().getPath()
				+ "/aaa.jpg";
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(localUrl);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		return Base64.encodeToString(data, Base64.DEFAULT);
		// return encoder.encode(data);//返回Base64编码过的字节数组字符串
	}
	
	/**
	 * 发送图片
	 * @param url		
	 * @param fileUrl
	 * @return
	 */
	public static String sendPicture(){
		String result = "";
		String target = "http://192.168.0.173:8084/WebService/FileUploadByBase64String"; // 要提交的目标地址
		try {
			String localUrl = Environment.getExternalStorageDirectory().getPath() + "/bbb.mp3";
			HttpPost httpRequest = new HttpPost(target); // 创建HttpPost对象
			HttpClient httpClient = new DefaultHttpClient();
			MultipartEntity mpEntity = new MultipartEntity(); //文件传输
			mpEntity.addPart("aaa", new FileBody(new File(localUrl)));
			httpRequest.setEntity(mpEntity);
			HttpResponse httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
				result += EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * 发送图片
	 * @param url		
	 * @param fileUrl
	 * @return
	 */
	public static String sendPicture(String url, String fileUrl){
		String result = "";
		String target = "http://192.168.0.173:8732/AndroidUpload.svc/update_pictrue"; // 要提交的目标地址
		String localUrl = Environment.getExternalStorageDirectory().getPath() + "/aaa.jpg";
		try {
//			rpc.addProperty("DirName", "aaa");  
//			rpc.addProperty("Limit", "");  
//			rpc.addProperty("FileName", localUrl);  
//			rpc.addProperty("Stream", new FileInputStream(new File(localUrl)));  
//			rpc.addProperty("IsSuccessed", true);  
			HttpPost httpRequest = new HttpPost(target); // 创建HttpPost对象
			HttpClient httpClient = new DefaultHttpClient();
//			httpRequest.setHeader("Content-Type", "application/soap+xml; charset=utf-8");
//			setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
			MultipartEntity mpEntity = new MultipartEntity(); //文件传输
//			mpEntity.addPart("Stream", new FileBody(new File(localUrl)));
//			mpEntity.addPart("DirName", new StringBody("aaa"));
//			mpEntity.addPart("Limit", new StringBody(""));
//			mpEntity.addPart("FileName", new StringBody(localUrl));
//			mpEntity.addPart("IsSuccessed", new StringBody(""));
			httpRequest.setEntity(mpEntity);
			HttpResponse httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
				result += EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
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
