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
		// 设置新消息是否提醒
		mOptions.setNewMsgNotify(true);
		// 设置状态栏通知图标
		mOptions.setIcon(R.drawable.ic_launcher);
		// 设置是否启用勿扰模式（不会声音/震动提醒）
		mOptions.setSilenceEnable(false);
		// 设置勿扰模式时间段（开始小时/开始分钟-结束小时/结束分钟）
		// 小时采用24小时制
		// 如果设置勿扰模式不启用，则设置勿扰时间段无效
		// 当前设置晚上11点到第二天早上8点之间不提醒
		mOptions.setSilenceTime(23, 0, 8, 0);
		// 设置是否震动提醒(如果处于免打扰模式则设置无效，没有震动)
		mOptions.enableShake(true);
		// 设置是否声音提醒(如果处于免打扰模式则设置无效，没有声音)
		mOptions.enableSound(true);
	}

	/**
	 * 初始化云通讯SDK
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
	 * SDK 初始化失败,可能有如下原因造成 1、可能SDK已经处于初始化状态
	 * 2、SDK所声明必要的权限未在清单文件（AndroidManifest.xml）里配置、
	 * 或者未配置服务属性android:exported="false"; 3、当前手机设备系统版本低于ECSDK所支持的最低版本（当前ECSDK支持
	 * Android Build.VERSION.SDK_INT 以及以上版本）
	 */
	@Override
	public void onError(Exception arg0) {

	}

	/**
	 * SDK已经初始化成功
	 */
	@Override
	public void onInitialized() {
		if (mInitParams == null) {
			mInitParams = ECInitParams.createParams();
		}
		mInitParams.reset();
		// 如：VoIP账号/手机号码/..
		mInitParams.setUserid("aaf98f89529d327b0152a0c9a2f6043e");
		// appkey
		mInitParams.setAppKey("aaf98f8952b6f5730152dde1cdd8222a");
		// mInitParams.setAppKey(/*clientUser.getAppKey()*/"ff8080813d823ee6013d856001000029");
		// appToken
		mInitParams.setToken("9929d6e4ac609c4e73ed8c1120500e99");
		// mInitParams.setToken(/*clientUser.getAppToken()*/"d459711cd14b443487c03b8cc072966e");
		// ECInitParams.LoginMode.FORCE_LOGIN
		mInitParams.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
		// 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
		// 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
		// 3 LoginMode（强制上线：FORCE_LOGIN 默认登录：AUTO）
		mInitParams.setMode(ECInitParams.LoginMode.FORCE_LOGIN);

		ECDevice.setOnDeviceConnectListener(new ECDevice.OnECDeviceConnectListener() {
			public void onConnect() {
				// 兼容4.0，5.0可不必处理
			}

			@Override
			public void onDisconnect(ECError error) {
				// 兼容4.0，5.0可不必处理
			}

			@Override
			public void onConnectState(ECDevice.ECConnectState state,
					ECError error) {
				if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
					if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
						// 账号异地登陆
						showToastOnActivity("账号异地登陆");
					} else {
						// 连接状态失败
						showToastOnActivity("连接状态失败");
					}
					return;
				} else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
					// 登陆成功
					showToastOnActivity("登陆成功");
				}
			}
		});

		// 如果是v5.1.8r版本 ECDevice.setOnChatReceiveListener(new
		// OnChatReceiveListener())
		// 5.1.7r及以前版本设置SDK接收消息回调
		ECDevice.setOnChatReceiveListener(new OnChatReceiveListener() {
			@Override
			public void OnReceivedMessage(ECMessage msg) {
				// 收到新消息
			}

			@Override
			public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {
				// 收到群组通知消息（有人加入、退出...）
				// 可以根据ECGroupNoticeMessage.ECGroupMessageType类型区分不同消息类型
			}

			@Override
			public void onOfflineMessageCount(int count) {
				// 登陆成功之后SDK回调该接口通知账号离线消息数
			}

			@Override
			public void onReceiveOfflineMessage(List msgs) {
				// SDK根据应用设置的离线消息拉去规则通知应用离线消息
			}

			@Override
			public void onReceiveOfflineMessageCompletion() {
				// SDK通知应用离线消息拉取完成
			}

			@Override
			public void onServicePersonVersion(int version) {
				// SDK通知应用当前账号的个人信息版本号
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
			// 判断注册参数是否正确
			ECDevice.login(mInitParams);
		}

	}
	
	
	
	
	
	
	
	
	protected void showToastOnActivity(final String text){
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
