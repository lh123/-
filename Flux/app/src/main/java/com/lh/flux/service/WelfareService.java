package com.lh.flux.service;
import android.app.*;
import android.content.*;
import android.net.wifi.*;
import android.os.*;
import android.preference.*;
import com.lh.flux.*;
import com.lh.flux.crash.*;
import com.lh.flux.domain.*;
import com.lh.flux.domain.event.*;
import com.lh.flux.view.*;
import com.squareup.otto.*;
import java.lang.ref.*;

public class WelfareService extends Service
{
	private Handler mHandler;
	private WelfareUsecase mWelfareUsecase;
	private LoginUsecase mLoginUsecase;

	private long startTime,durTime;
	private long intervalTime=2000,actTime;
	private int loginTimes=3;

	public static boolean isAlive=false;

	private boolean isGrabWelfare=false;
	private PowerManager.WakeLock lock;
	private WifiManager.WifiLock wlock;
	private NotificationManager nm;
	private boolean isNeedGrab=false;

	public static final int START_GRAB=0;
	public static final int STOP_SERVICE=1;
	public static final int FLAG_SERVICE_RUNNING=0;
	public static final int FLAG_WELFARE_RESULT=1;
	public static final String LOCK_TAG="auto_grab_welfare";

	public boolean isGrabWelfare()
	{
		return isGrabWelfare;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log("service creat");
		BusProvide.getBus().register(this);
		mHandler = new Handler();
		mWelfareUsecase = new WelfareUsecase(mHandler);
		mLoginUsecase = new LoginUsecase(mHandler);
		PowerManager pw=(PowerManager) getSystemService(POWER_SERVICE);
		WifiManager wm=(WifiManager) getSystemService(WIFI_SERVICE);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
		lock = pw.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_TAG);
		wlock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, LOCK_TAG);
		lock.setReferenceCounted(false);
		wlock.setReferenceCounted(false);
		lock.acquire();
		wlock.acquire();
		intervalTime = Long.parseLong(sp.getString("interval_time", "2")) * 1000l;
		loginTimes = Integer.parseInt(sp.getString("retry_times", "3"));
		durTime = Long.parseLong(sp.getString("time_out", "3")) * 60 * 1000l;
		showNotification("正在等待", FLAG_SERVICE_RUNNING);
		Log("lock cpu wifi");
		startTime = System.currentTimeMillis();
		isAlive = true;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (!isGrabWelfare)
		{
			Log("service start");
			actTime = intent.getLongExtra("act", startTime);
			Log("startTime=" + startTime);
			Log("actTime=" + actTime);
			if (intent.getBooleanExtra("auto", false) && !isNeedGrab)
			{
				startAutoGrabWithDely();
			}
		}
		sendToPresenter(new WelfareServiceCreatEvent(new WeakReference<WelfareService>(this)));
		return START_NOT_STICKY;
	}

	public void startAutoGrabWithDely()
	{
		SharedPreferences sp=getSharedPreferences("auto_grab", Context.MODE_PRIVATE);
		sp.edit().putString("time", null).commit();
		long delyTime=actTime - startTime;
		Log("dely=" + delyTime);
		if (delyTime > 0 && !isGrabWelfare)
		{
			mHandler.postDelayed(new Runnable(){

					@Override
					public void run()
					{
						Log("after delay");
						startAutoGrab();
					}
				}, delyTime);
		}
		else
		{
			startAutoGrab();
		}
	}

	public void startAutoGrab()
	{
		if (FluxUserManager.getInstance().getUser().isLogin() == false)
		{
			isNeedGrab = true;
			showNotification("正在尝试登录", FLAG_SERVICE_RUNNING);
			mLoginUsecase.login(FluxUserManager.getInstance().getUser());
		}
		else if (FluxUserManager.getInstance().getUser().getCookie() == null)
		{
			isNeedGrab = true;
			showNotification("正在获取Cookie", FLAG_SERVICE_RUNNING);
			mWelfareUsecase.getWelfareCoookie(FluxUserManager.getInstance().getUser());
		}
		else
		{
			isGrabWelfare = true;
			mWelfareUsecase.grabWelfare(FluxUserManager.getInstance().getUser());
		}
	}

	@Override
	public IBinder onBind(Intent p1)
	{
		return null;
	}

	private void showNotification(String msg, int mode)
	{
		updateTvText(msg);
		Notification.Builder builder=new Notification.Builder(this);
		builder.setContentTitle("服务运行中");
		builder.setSmallIcon(R.drawable.ic_launcher);
		if (mode == FLAG_SERVICE_RUNNING)
		{
			builder.setContentText(msg);
			Notification no=builder.build();
			no.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
			nm.notify(FLAG_SERVICE_RUNNING,no);
		}
		else
		{
			builder.setContentTitle("服务已关闭");
			builder.setContentText(msg);
			Intent i=new Intent(this, FluxActivity.class);
			PendingIntent pi=PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(pi);
			Notification no=builder.build();
			no.flags = Notification.FLAG_AUTO_CANCEL;
			//nm.cancel(FLAG_SERVICE_RUNNING);
			nm.notify(FLAG_WELFARE_RESULT,no);
		}
	}

	@Subscribe
	public void onGrabEventReceive(GrabEvent event)
	{
		if (System.currentTimeMillis() - actTime > durTime)
		{
			isGrabWelfare = false;
			Log("超时");
			showNotification("超时", FLAG_WELFARE_RESULT);
			releaseLockAndStop();
		}
		else
		{
			if (event.isSuccess())
			{
				//Log(event.getData().getMsg());
				if ("000".equals(event.getData().getReturnCode()))
				{
					isGrabWelfare = false;
					showNotification("抢到红包", FLAG_WELFARE_RESULT);
					releaseLockAndStop();
				}
				else if ("001".equals(event.getData().getReturnCode()))
				{
					showNotification("正在重新获取Cookie", FLAG_SERVICE_RUNNING);
					FluxUserManager.getInstance().getUser().setCookie(null);
					grabWelfareDelay();
				}
				else if ("002".equals(event.getData().getReturnCode()))
				{
					showNotification("正在抢红包", FLAG_SERVICE_RUNNING);
					grabWelfareDelay();
				}
				else if ("003".equals(event.getData().getReturnCode()))
				{
					isGrabWelfare = false;
					showNotification("每天只能抢一次红包", FLAG_WELFARE_RESULT);
					releaseLockAndStop();
				}
				else if ("004".equals(event.getData().getReturnCode()))
				{
					isGrabWelfare = false;
					showNotification("没抢到红包", FLAG_WELFARE_RESULT);
					releaseLockAndStop();
				}
			}
			else
			{
				grabWelfareDelay();
			}
		}
	}

	@Subscribe
	public void onLoginEventReceive(LoginEvent event)
	{
		if (event.isSuccess())
		{
			FluxUserManager.getInstance().getUser().setIsLogin(event.getData().isSuccess());
			if (event.getData().isSuccess())
			{
				FluxUserManager.getInstance().getUser().setToken(event.getData().getToken());
				FluxUserManager.getInstance().getUser().setSessionID(event.getData().getSessionID());
				FluxUserManager.getInstance().saveUser();
				if (isNeedGrab)
				{
					isNeedGrab = false;
					startAutoGrab();
				}
				return;
			}
		}
		showNotification("登录失败", FLAG_SERVICE_RUNNING);
		loginTimes--;
		if (loginTimes > 0)
		{
			startAutoGrab();
		}
		else
		{
			showNotification("获取Cookie失败", FLAG_WELFARE_RESULT);
			releaseLockAndStop();
		}
	}

	@Subscribe
	public void onWelfareCookieReceive(WelfareCookieEvent event)
	{
		if (event.isSuccess())
		{
			showNotification("成功获取Cookie", FLAG_SERVICE_RUNNING);
			FluxUserManager.getInstance().getUser().setCookie(event.getCookie());
			FluxUserManager.getInstance().saveUser();
			if (isNeedGrab)
			{
				isNeedGrab = false;
				startAutoGrab();
			}
		}
		else
		{
			//showNotification("获取Cookie失败",FLAG_SERVICE_RUNNING);
			loginTimes--;
			if (loginTimes > 0)
			{
				startAutoGrab();
			}
			else
			{
				showNotification("获取Cookie失败", FLAG_WELFARE_RESULT);
				releaseLockAndStop();
			}
		}
	}

	private void updateTvText(String msg)
	{
		WelfareServiceEvent e=new WelfareServiceEvent(msg, isGrabWelfare);
		sendToPresenter(e);
	}

	private void grabWelfareDelay()
	{
		mHandler.postDelayed(new Runnable(){

				@Override
				public void run()
				{
					if (isAlive)
					{
						startAutoGrab();
					}
				}
			}, intervalTime);
	}

	private void Log(String msg)
	{
		LogUtil.getInstance().logE("WelfareService", msg);

	}

	private void releaseLockAndStop()
	{
		isAlive = false;
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
		stopSelf();
	}


	private void sendToPresenter(final Object o)
	{
		if (Looper.myLooper() != Looper.getMainLooper())
		{
			mHandler.post(new Runnable(){

					@Override
					public void run()
					{
						BusProvide.getBus().post(o);
					}
				});
		}
		else
		{
			BusProvide.getBus().post(o);
		}
	}

	@Override
	public void onDestroy()
	{
		isAlive = false;
		nm.cancel(FLAG_SERVICE_RUNNING);
		BusProvide.getBus().unregister(this);
		mWelfareUsecase.onDestroy();
		mLoginUsecase.onDestroy();
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
		mHandler.removeCallbacksAndMessages(null);
		Log("service destroy");
		super.onDestroy();
		FluxApp.getRefWatcher(this).watch(this, "service");
		FluxApp.getRefWatcher(this).watch(mWelfareUsecase);
		FluxApp.getRefWatcher(this).watch(mLoginUsecase);
	}

}
