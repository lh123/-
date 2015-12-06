package com.lh.liuliang.service;
import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import com.lh.liuliang.*;
import com.lh.liuliang.model.*;
import com.lh.liuliang.preference.*;
import com.lh.liuliang.ui.*;
import com.lh.liuliang.user.*;
import java.util.*;
import org.json.*;
import android.net.wifi.*;
import android.net.wifi.WifiManager.*;
import android.app.Notification.*;
import com.lh.liuliang.presenter.*;
import android.util.*;

public class WelfareService extends Service
{
	private UserInfo info;
	private MainModel mModel;
	private static Handler mHandler;
	private long startTime,durTime;
	private GrabWelfareStatus back;
	private long intervalTime=2000,actTime;
	private int loginTimes=3;

	private static boolean isAlive=false;
	private boolean isGrabWelfare=false;
	private boolean isBind=false;
	private PowerManager.WakeLock lock;
	private WifiManager.WifiLock wlock;
	private NotificationManager nm;

	public static final int FLAG_SERVICE_RUNNING=0;
	public static final int FLAG_SERVICE_RUNNING_AND_GRABING=1;
	public static final int FLAG_WELFARE_RESULT=2;
	public static final String LOCK_TAG="auto_grab_welfare";

	@Override
	public void onCreate()
	{
		Log("service creat");
		PowerManager pw=(PowerManager) getSystemService(POWER_SERVICE);
		WifiManager wm=(WifiManager) getSystemService(WIFI_SERVICE);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		lock = pw.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_TAG);
		wlock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, LOCK_TAG);
		lock.setReferenceCounted(false);
		wlock.setReferenceCounted(false);
		lock.acquire();
		wlock.acquire();
		showNotification(null, FLAG_SERVICE_RUNNING);
		Log("lock cpu wifi");
		super.onCreate();
		isAlive = true;
		info = UserInfo.getUserInfo();
		mModel = new MainModel();
		mHandler = new Handler();
	}

	@Override
	public void onDestroy()
	{
		isAlive = false;
		stopForeground(true);
		if (lock != null && lock.isHeld())
		{
			Log("unlock cpu");
			lock.release();
			lock = null;
		}
		if (wlock != null && wlock.isHeld())
		{
			Log("unlock wifi");
			wlock.release();
			wlock = null;
		}
		mHandler.removeCallbacksAndMessages(null);
		Log("service destory");
		super.onDestroy();
	}

	public static boolean isAlive()
	{
		return isAlive;
	}

	public boolean isGrabWelfare()
	{
		return isGrabWelfare;
	}

	public void setCallBack(GrabWelfareStatus back)
	{
		this.back = back;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log("service start");
		durTime = intent.getLongExtra("dur", 3 * 60 * 1000l);
		startTime = System.currentTimeMillis();
		actTime = intent.getLongExtra("act", startTime);
		Log("startTime=" + startTime);
		Log("actTime=" + actTime);
		if (intent.getBooleanExtra("auto", false))
		{
			startAutoGrab();
		}
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent p1)
	{
		Log("bind service");
		isBind = true;
		return new WelfareBind(this);
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		Log("unbind service");
		isBind = false;
		return true;
	}

	@Override
	public void onRebind(Intent intent)
	{
		super.onRebind(intent);
		isBind = true;
		Log("rebind service");
	}

	public void startAutoGrab()
	{
		DataPre.getInstance().saveAutoGrabWelfareStatus(null);
		if (actTime > startTime)
		{
			Log("delay=" + (actTime - startTime));
			mHandler.postDelayed(new Runnable(){

					@Override
					public void run()
					{
						Log("afterDelay=" + System.currentTimeMillis());
						GrabWelfareWithLogin();
					}
				}, actTime - startTime);
		}
		else
		{
			Log("noDelay");
			GrabWelfareWithLogin();
		}
	}


	public void grabWelfare()
	{
		isGrabWelfare = true;
		startTime = System.currentTimeMillis();
		if (info.getSessionID() == null)
		{
			Intent i=new Intent();
			i.setClass(this, LoginActivity.class);
			startActivity(i);
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			isGrabWelfare = false;
			return;
		}
		showNotification(null, FLAG_SERVICE_RUNNING_AND_GRABING);
		if (info.getCookie() == null)
		{
			mModel.getWelfareCookie(info, new MainModel.MessCallBack(){

					@Override
					public void info(String mess)
					{
						info.setCookie(mess);
						DataPre.getInstance().saveCookie(mess);
						innerGrabWelfare();
					}
				});
		}
		else
		{
			innerGrabWelfare();
		}
	}

	private void GrabWelfareWithLogin()
	{
		if (loginTimes <= 0)
		{
			Log("登录失败");
			showNotification("登录失败", FLAG_WELFARE_RESULT);
			releaseLock();
			return;
		}
		Log("开始登录");
		DataPre.getInstance().saveAutoGrabWelfareStatus(null);
		mModel.loginWithToken(info, new MainModel.MessCallBack(){

				@Override
				public void info(String mess)
				{
					if (mess == null)
					{
						Log("登录失败");
						showNotification("登录失败,尝试重新登录", FLAG_WELFARE_RESULT);
						loginTimes--;
						GrabWelfareWithLogin();
						return;
					}
					else
					{
						try
						{
							JSONObject obj=new JSONObject(mess);
							if ("000".equals(obj.optString("status", "-1")))
							{
								String token = obj.optString("token", null);
								String id=obj.optString("sessionID", null);
								DataPre.getInstance().saveToken(token);
								DataPre.getInstance().saveSeasonsID(id);
								UserInfo.getUserInfo().setToken(token);
								UserInfo.getUserInfo().setSessionID(id);
								Log("登录成功");
								mHandler.post(new Runnable(){

										@Override
										public void run()
										{
											grabWelfare();
										}
									});
							}
							else
							{
								DataPre.getInstance().saveSeasonsID(null);
								DataPre.getInstance().saveToken(null);
								info.setSessionID(null);
								info.setToken(null);
								mHandler.post(new Runnable(){

										@Override
										public void run()
										{
											Log("登录失败");
											showNotification("登录失败", FLAG_WELFARE_RESULT);
											releaseLock();
										}
									});
							}
						}
						catch (JSONException e)
						{}
					}
				}
			});
	}

	private void releaseLock()
	{
		if (lock != null && lock.isHeld())
		{
			lock.release();
			Log("unlock cpu");
		}
		if (wlock != null && wlock.isHeld())
		{
			wlock.release();
			Log("unlock wifi");
		}
		stopWelfareService();
	}

	private void innerGrabWelfare()
	{
		intervalTime = 500;
		updateTvText("抢红包中");
		isGrabWelfare = true;
		mModel.grabWelfare(info, new MainModel.MessCallBack(){

				@Override
				public void info(final String mess)
				{
					Log("msg=" + mess);
					if (mess == null)
					{
						if (isAlive)
						{
							mHandler.postDelayed(new Runnable(){

									@Override
									public void run()
									{
										innerGrabWelfare();
									}
								}, intervalTime);
						}
						return;
					}
					try
					{
						JSONObject json=new JSONObject(mess);
						String code = json.optString("returnCode");
						if ("000".equals(code))
						{
							isGrabWelfare = false;
							Log("抢到红包");
							showNotification(json.optString("msg"), FLAG_WELFARE_RESULT);
							releaseLock();
						}
						else if ("001".equals(code))
						{
							isGrabWelfare = false;
							UserInfo.getUserInfo().setCookie(null);
							DataPre.getInstance().saveCookie(null);
							showNotification("请重新登录", FLAG_WELFARE_RESULT);
							releaseLock();
						}
						else if ("003".equals(code))
						{
							isGrabWelfare = false;
							showNotification("每天只能抢一次红包～(￣▽￣～)~?", FLAG_WELFARE_RESULT);
							releaseLock();
						}
						else if ("004".equals(code))
						{
							isGrabWelfare = false;
							showNotification("没抢到红包", FLAG_WELFARE_RESULT);
							releaseLock();
						}
						else
						{
							if (System.currentTimeMillis() < startTime + durTime)
							{
								if ("002".equals(code))
								{
									intervalTime = 3000;
								}
								else
								{
									intervalTime = 2000;
								}
								if (isAlive)
								{
									mHandler.postDelayed(new Runnable(){

											@Override
											public void run()
											{
												innerGrabWelfare();
											}
										}, intervalTime);
								}
							}
							else
							{
								isGrabWelfare = false;
								showNotification("没抢到红包", FLAG_WELFARE_RESULT);
								releaseLock();
							}
						}
					}
					catch (JSONException e)
					{}
				}
			});
	}

	private void showNotification(String msg, int mode)
	{
		Notification.Builder builder=new Notification.Builder(this);
		builder.setContentTitle("服务运行中");
		builder.setSmallIcon(R.drawable.ic_launcher);
		if (mode == FLAG_SERVICE_RUNNING)
		{
			builder.setContentText("等待中");
			Notification no=builder.build();
			no.flags = Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
			startForeground(android.os.Process.myPid(), no);
		}
		else if (mode == FLAG_SERVICE_RUNNING_AND_GRABING)
		{
			updateTvText("抢红包中");
			builder.setContentText("抢红包中");
			Notification no=builder.build();
			no.flags = Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
			nm.notify(android.os.Process.myPid(), no);
		}
		else
		{
			builder.setContentTitle("服务已关闭");
			updateTvText(msg);
			builder.setContentText(msg);
			Intent i=new Intent(this, MainActivity.class);
			PendingIntent pi=PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(pi);
			Notification no=builder.build();
			no.flags = Notification.FLAG_AUTO_CANCEL;
			nm.notify(FLAG_WELFARE_RESULT, no);
		}
	}

	private void Log(String msg)
	{
		Log.i("WelfareService", msg);
	}

	private void stopWelfareService()
	{
		if (!isBind)
		{
			stopSelf();
		}
	}

	private void updateTvText(String msg)
	{
		if (back != null)
		{
			back.onGrabWelfare(msg);
		}
	}

	public class WelfareBind extends Binder
	{
		private WelfareService mService;

		public WelfareBind(WelfareService mService)
		{
			this.mService = mService;
		}

		public WelfareService getService()
		{
			return mService;
		}
	}

	public interface GrabWelfareStatus
	{
		public void onGrabWelfare(String msg)
	}
}
