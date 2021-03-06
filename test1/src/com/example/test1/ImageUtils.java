package com.example.test1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * 处理图片的工具类
 */
public class ImageUtils {

	/**
	 * 图片基准路径
	 */
	public static final String BASE_SDCARD_IMAGES = Util.getInstance()
			.getExtPath() + "/IM/";

	private static final String TAG = "ImageUtils";

	/**
	 * 判断文件是否存在
	 * 
	 * @param 文件在本地的完整名
	 * @return
	 */
	private static boolean judgeExists(String fullName) {

		File file = new File(fullName);

		return file.exists();
	}

	/**
	 * 获取最后的‘/’后的文件名
	 * 
	 * @param name
	 * @return
	 */
	private static String getLastName(String name) {
		int lastIndexOf = 0;
		try {
			lastIndexOf = name.lastIndexOf('/');
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !name.equals("") ? name.substring(lastIndexOf + 1) : "";
	}

	/**
	 * 拼接一个完整的本地文件名
	 * 
	 * @param 文件的网络路径
	 * @return
	 */
	public static String getImageFullName(String name) {
		return BASE_SDCARD_IMAGES + getLastName(name);
	}

	/**
	 * 通过该网络路径获取Bitmap
	 * 
	 * @param 该图片的网络路径
	 */
	public static Bitmap getBitmap(String urlPath) {

		Bitmap bitmap = null;
		String fullName = getImageFullName(urlPath);
		if (ImageUtils.judgeExists(fullName)) {
			/* 存在就直接使用 */
//			Log.e(TAG, "使用了sdcard里的图片");
			bitmap = BitmapFactory.decodeFile(fullName);
		} else {
			
			/* 去下载图片,下载完成之后返回该对象 */
//			Log.e(TAG, "去下载了图片");
			bitmap = downloadAndSaveBitmap(urlPath, fullName);
		}
		return bitmap;
	}
	
	
	/**
	 * 判断图片是否存在
	 * @param urlPath
	 * @return true 不存在  false 存在
	 */
	public static Boolean getBitmapBoolean(String urlPath) {

		String fullName = getImageFullName(urlPath);
		if (ImageUtils.judgeExists(fullName)) {
			return false;
		} else {
			return true;

		}
	}

	/**
	 * 下载保存图片
	 * 
	 * @param urlPath
	 *            下载路径
	 * @param fullName
	 *            文件保存路径+文件名
	 * @return
	 */
	private static Bitmap downloadAndSaveBitmap(String urlPath, String fullName) {

		Bitmap bitmap = downloadImage(urlPath);

		if (bitmap != null) {

			saveBitmap(fullName, bitmap);

		}

		return bitmap;
	}

	/**
	 * 保存图片
	 * 
	 * @param fullName
	 * @param bitmap
	 */
	private static void saveBitmap(String fullName, Bitmap bitmap) {

		if (bitmap != null) {

			try {
				File file = new File(fullName);
				if (!file.exists()) {
					creatFolder(fullName);
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, "图片保存失败，异常信息是：" + e.toString());
			}
		} else {
			Log.e(TAG, "没有下载成功图片，无法保存");
		}
	}

	/**
	 * 创建保存文件的文件夹
	 * 
	 * @param fullName
	 *            带文件名的文件路径
	 * @return
	 */
	private static void creatFolder(String fullName) {
		if (getLastName(fullName).contains(".")) {
			String newFilePath = fullName.substring(0,
					fullName.lastIndexOf('/'));
			File file = new File(newFilePath);
			file.mkdirs();
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param urlPath
	 * @return
	 */
	private static Bitmap downloadImage(String urlPath) {

		try {
			byte[] byteData = getImageByte(urlPath);
			if (byteData == null) {
				Log.e(TAG, "没有得到图片的byte，问题可能是path：" + urlPath);
				return null;
			}
			int len = byteData.length;

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = false;
			if (len > 200000) {// 大于200K的进行压缩处理
				options.inSampleSize = 2;
			}

			return BitmapFactory.decodeByteArray(byteData, 0, len);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "图片下载失败，异常信息是：" + e.toString());
			return null;
		}
	}

	/**
	 * 获取图片的byte数组
	 * 
	 * @param urlPath
	 * @return
	 */
	private static byte[] getImageByte(String urlPath) {
		InputStream in = null;
		byte[] result = null;
		try {
			URL url = new URL(urlPath);
			HttpURLConnection httpURLconnection = (HttpURLConnection) url
					.openConnection();
			httpURLconnection.setDoInput(true);
			httpURLconnection.connect();
			if (httpURLconnection.getResponseCode() == 200) {
				in = httpURLconnection.getInputStream();
				result = readInputStream(in);
				in.close();
			} else {
				Log.e(TAG, "下载图片失败，状态码是：" + httpURLconnection.getResponseCode());
			}
		} catch (Exception e) {
			Log.e(TAG, "下载图片失败，原因是：" + e.toString());
			e.printStackTrace();
		} finally {
			Log.e(TAG, "下载图片失败!");
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 将输入流转为byte数组
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream in) throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		baos.close();
		in.close();
		return baos.toByteArray();

	}

	/**
	 * 此方法用来异步加载图片
	 * 
	 * @param imageview
	 * @param path
	 */

	public static void downloadAsyncTask(final ImageView imageview,
			final String path) {
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return getBitmap(path);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				if (result != null && imageview != null) {
					imageview.setImageBitmap(result);
				} else {
					Log.e(TAG, "在downloadAsyncTask里异步加载图片失败！");
				}
			}

		}.execute(new String[] {});

	}
	
	public static void downloadAsyncTaskBnt(final ImageButton imageview,
			final String path) {
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return getBitmap(path);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				if (result != null && imageview != null) {
					imageview.setImageBitmap(result);
				} else {
					Log.e(TAG, "在downloadAsyncTask里异步加载图片失败！");
				}
			}

		}.execute(new String[] {});

	}
	
	
	
	
	
	public static void download(
			final String path,final int count) {
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return getBitmap(path);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.execute(new String[] {});

	}

}
