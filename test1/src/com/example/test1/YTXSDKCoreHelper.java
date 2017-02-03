package com.example.test1;

import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.ECNotifyOptions;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

public class YTXSDKCoreHelper implements ECDevice.InitListener {

	private static YTXSDKCoreHelper sInstance;
	private ECNotifyOptions mOptions;
	private ECInitParams mInitParams;
	private static Context context;

	private YTXSDKCoreHelper() {
		initNotifyOptions();
	}

	public static YTXSDKCoreHelper getInstance() {
		if (sInstance == null) {
			synchronized (YTXSDKCoreHelper.class) {
				if (sInstance == null) {
					sInstance = new YTXSDKCoreHelper();
				}
			}
		}
		return sInstance;
	}

	private void initNotifyOptions() {
		if (mOptions == null) {
			mOptions = new ECNotifyOptions();
		}
		// ��������Ϣ�Ƿ�����
		mOptions.setNewMsgNotify(true);
		// ����״̬��֪ͨͼ��
		mOptions.setIcon(R.drawable.ic_launcher);
		// �����Ƿ���������ģʽ����������/�����ѣ�
		mOptions.setSilenceEnable(false);
		// ��������ģʽʱ��Σ���ʼСʱ/��ʼ����-����Сʱ/�������ӣ�
		// Сʱ����24Сʱ��
		// �����������ģʽ�����ã�����������ʱ�����Ч
		// ��ǰ��������11�㵽�ڶ�������8��֮�䲻����
		mOptions.setSilenceTime(23, 0, 8, 0);
		// �����Ƿ�������(������������ģʽ��������Ч��û����)
		mOptions.enableShake(true);
		// �����Ƿ���������(������������ģʽ��������Ч��û������)
		mOptions.enableSound(true);
	}

	/**
	 * ��ʼ����ͨѶSDK
	 * 
	 * @param ctx
	 */
	public static void init(Context ctx) {
		context = ctx;
		if (!ECDevice.isInitialized()) {
			ECDevice.initial(ctx, getInstance());
			return;
		}
		getInstance().onInitialized();
	}

	/**
	 * SDK ��ʼ��ʧ��,����������ԭ����� 1������SDK�Ѿ����ڳ�ʼ��״̬
	 * 2��SDK��������Ҫ��Ȩ��δ���嵥�ļ���AndroidManifest.xml�������á�
	 * ����δ���÷�������android:exported="false"; 3����ǰ�ֻ��豸ϵͳ�汾����ECSDK��֧�ֵ���Ͱ汾����ǰECSDK֧��
	 * Android Build.VERSION.SDK_INT �Լ����ϰ汾��
	 */
	@Override
	public void onError(Exception arg0) {

	}

	/**
	 * SDK�Ѿ���ʼ���ɹ�
	 */
	@Override
	public void onInitialized() {
		if (mInitParams == null) {
			mInitParams = ECInitParams.createParams();
		}
		mInitParams.reset();
		// �磺VoIP�˺�/�ֻ�����/..
		mInitParams.setUserid("aaf98f89529d327b0152a0c9a2f6043e");
		// appkey
		mInitParams.setAppKey("aaf98f8952b6f5730152dde1cdd8222a");
		// mInitParams.setAppKey(/*clientUser.getAppKey()*/"ff8080813d823ee6013d856001000029");
		// appToken
		mInitParams.setToken("9929d6e4ac609c4e73ed8c1120500e99");
		// mInitParams.setToken(/*clientUser.getAppToken()*/"d459711cd14b443487c03b8cc072966e");
		// ECInitParams.LoginMode.FORCE_LOGIN
		mInitParams.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
		// 1�����û���+�����½������ǿ�����ߣ��ߵ��Ѿ����ߵ��豸��
		// 2�����Զ�����ע�ᣨ����˺��Ѿ��������豸��¼�����ʾ��ص�½��
		// 3 LoginMode��ǿ�����ߣ�FORCE_LOGIN Ĭ�ϵ�¼��AUTO��
		mInitParams.setMode(ECInitParams.LoginMode.FORCE_LOGIN);

		ECDevice.setOnDeviceConnectListener(new ECDevice.OnECDeviceConnectListener() {
			public void onConnect() {
				// ����4.0��5.0�ɲ��ش���
			}

			@Override
			public void onDisconnect(ECError error) {
				// ����4.0��5.0�ɲ��ش���
			}

			@Override
			public void onConnectState(ECDevice.ECConnectState state,
					ECError error) {
				if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
					if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
						// �˺���ص�½
						showToastOnActivity("�˺���ص�½");
					} else {
						// ����״̬ʧ��
						showToastOnActivity("����״̬ʧ��");
					}
					return;
				} else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
					// ��½�ɹ�
					showToastOnActivity("��½�ɹ�");
				}
			}
		});

		// �����v5.1.8r�汾 ECDevice.setOnChatReceiveListener(new
		// OnChatReceiveListener())
		// 5.1.7r����ǰ�汾����SDK������Ϣ�ص�
		ECDevice.setOnChatReceiveListener(new OnChatReceiveListener() {
			@Override
			public void OnReceivedMessage(ECMessage msg) {
				// �յ�����Ϣ
			}

			@Override
			public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
				// �յ�Ⱥ��֪ͨ��Ϣ�����˼��롢�˳�...��
				// ���Ը���ECGroupNoticeMessage.ECGroupMessageType�������ֲ�ͬ��Ϣ����
			}

			@Override
			public void onOfflineMessageCount(int count) {
				// ��½�ɹ�֮��SDK�ص��ýӿ�֪ͨ�˺�������Ϣ��
			}

			@Override
			public void onReceiveOfflineMessage(List msgs) {
				// SDK����Ӧ�����õ�������Ϣ��ȥ����֪ͨӦ��������Ϣ
			}

			@Override
			public void onReceiveOfflineMessageCompletion() {
				// SDK֪ͨӦ��������Ϣ��ȡ���
			}

			@Override
			public void onServicePersonVersion(int version) {
				// SDK֪ͨӦ�õ�ǰ�˺ŵĸ�����Ϣ�汾��
			}

			@Override
			public int onGetOfflineMessage() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void onReceiveDeskMessage(ECMessage arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onReceiveMessageNotify(ECMessageNotify arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSoftVersion(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		if (mInitParams.validate()) {
			// �ж�ע������Ƿ���ȷ
			ECDevice.login(mInitParams);
		}

	}
	
	
	
	
	
	
	
	
	protected void showToastOnActivity(final String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
