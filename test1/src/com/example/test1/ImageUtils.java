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
 * ����ͼƬ�Ĺ�����
 */
public class ImageUtils {

	/**
	 * ͼƬ��׼·��
	 */
	public static final String BASE_SDCARD_IMAGES = Util.getInstance()
			.getExtPath() + "/IM/";

	private static final String TAG = "ImageUtils";

	/**
	 * �ж��ļ��Ƿ����
	 * 
	 * @param �ļ��ڱ��ص�������
	 * @return
	 */
	private static boolean judgeExists(String fullName) {

		File file = new File(fullName);

		return file.exists();
	}

	/**
	 * ��ȡ���ġ�/������ļ���
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
	 * ƴ��һ�������ı����ļ���
	 * 
	 * @param �ļ�������·��
	 * @return
	 */
	public static String getImageFullName(String name) {
		return BASE_SDCARD_IMAGES + getLastName(name);
	}

	/**
	 * ͨ��������·����ȡBitmap
	 * 
	 * @param ��ͼƬ������·��
	 */
	public static Bitmap getBitmap(String urlPath) {

		Bitmap bitmap = null;
		String fullName = getImageFullName(urlPath);
		if (ImageUtils.judgeExists(fullName)) {
			/* ���ھ�ֱ��ʹ�� */
//			Log.e(TAG, "ʹ����sdcard���ͼƬ");
			bitmap = BitmapFactory.decodeFile(fullName);
		} else {
			
			/* ȥ����ͼƬ,�������֮�󷵻ظö��� */
//			Log.e(TAG, "ȥ������ͼƬ");
			bitmap = downloadAndSaveBitmap(urlPath, fullName);
		}
		return bitmap;
	}
	
	
	/**
	 * �ж�ͼƬ�Ƿ����
	 * @param urlPath
	 * @return true ������  false ����
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
	 * ���ر���ͼƬ
	 * 
	 * @param urlPath
	 *            ����·��
	 * @param fullName
	 *            �ļ�����·��+�ļ���
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
	 * ����ͼƬ
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
				Log.e(TAG, "ͼƬ����ʧ�ܣ��쳣��Ϣ�ǣ�" + e.toString());
			}
		} else {
			Log.e(TAG, "û�����سɹ�ͼƬ���޷�����");
		}
	}

	/**
	 * ���������ļ����ļ���
	 * 
	 * @param fullName
	 *            ���ļ������ļ�·��
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
	 * ����ͼƬ
	 * 
	 * @param urlPath
	 * @return
	 */
	private static Bitmap downloadImage(String urlPath) {

		try {
			byte[] byteData = getImageByte(urlPath);
			if (byteData == null) {
				Log.e(TAG, "û�еõ�ͼƬ��byte�����������path��" + urlPath);
				return null;
			}
			int len = byteData.length;

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = false;
			if (len > 200000) {// ����200K�Ľ���ѹ������
				options.inSampleSize = 2;
			}

			return BitmapFactory.decodeByteArray(byteData, 0, len);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "ͼƬ����ʧ�ܣ��쳣��Ϣ�ǣ�" + e.toString());
			return null;
		}
	}

	/**
	 * ��ȡͼƬ��byte����
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
				Log.e(TAG, "����ͼƬʧ�ܣ�״̬���ǣ�" + httpURLconnection.getResponseCode());
			}
		} catch (Exception e) {
			Log.e(TAG, "����ͼƬʧ�ܣ�ԭ���ǣ�" + e.toString());
			e.printStackTrace();
		} finally {
			Log.e(TAG, "����ͼƬʧ��!");
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
	 * ��������תΪbyte����
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
	 * �˷��������첽����ͼƬ
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
					Log.e(TAG, "��downloadAsyncTask���첽����ͼƬʧ�ܣ�");
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
					Log.e(TAG, "��downloadAsyncTask���첽����ͼƬʧ�ܣ�");
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
