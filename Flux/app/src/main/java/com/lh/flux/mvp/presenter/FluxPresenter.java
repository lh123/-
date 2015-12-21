package com.lh.flux.mvp.presenter;
import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.widget.*;
import com.lh.flux.domain.*;
import com.lh.flux.domain.event.*;
import com.lh.flux.mvp.view.*;
import com.lh.flux.service.*;
import com.lh.flux.view.*;
import com.squareup.otto.*;
import java.lang.ref.*;
import java.text.*;
import java.util.*;
import com.lh.flux.crash.*;

public class FluxPresenter
{
	private IFluxActivity mFluxActivity;
	private LoginUsecase mLoginUsecase;
	private WelfareUsecase mWelfareUsecase;
	private FluxUsecase mFluxUsecase;
	private Handler mHandler;
	private WeakReference<WelfareService> mService;
	private SharedPreferences sp;

	private boolean isNeedRefreshWelfareInfo=false;
	private boolean isNeedGrabWelfare=false;

	public FluxPresenter(IFluxActivity mFluxActivity)
	{
		this.mFluxActivity = mFluxActivity;
	}

	public void onCreate()
	{
		BusProvide.getBus().register(this);
		mHandler = new Handler();
		mLoginUsecase = new LoginUsecase(mHandler);
		mWelfareUsecase = new WelfareUsecase(mHandler);
		mFluxUsecase = new FluxUsecase(mHandler);
		sp = mFluxActivity.getContext().getSharedPreferences("auto_grab", Context.MODE_PRIVATE);
		mFluxActivity.setPhoneNum(FluxUserManager.getInstance().getUser().getPhone());
		if (sp.contains("time"))
		{
			mFluxActivity.setWelfareServiceStatus(sp.getString("time", ""), false);
		}
		if (!FluxUserManager.getInstance().getUser().isLogin())
		{
			startLogin();
		}
		else
		{
			mFluxActivity.setLoginStatus(mFluxActivity.LOGIN_SUCCESS);
		}
	}

	public void startLoginActivity()
	{
		Intent i=new Intent();
		i.setClass(mFluxActivity.getContext(), LoginActivity.class);
		((FluxActivity)mFluxActivity).startActivity(i);
	}

	public void startRefreshFlux()
	{
		mFluxActivity.setFluxProgressStatus(true);
		mFluxUsecase.getFluxInfo(FluxUserManager.getInstance().getUser());
	}

	public void startRefreshWelfareInfo()
	{
		if (FluxUserManager.getInstance().getUser().getCookie() == null)
		{
			isNeedRefreshWelfareInfo = true;
			mFluxActivity.setWelfareProgressStatus(true);
			mWelfareUsecase.getWelfareCoookie(FluxUserManager.getInstance().getUser());
		}
		else
		{
			mFluxActivity.setWelfareProgressStatus(true);
			mWelfareUsecase.getWelfareInfo(FluxUserManager.getInstance().getUser());
		}
	}

	public void startGrabWelfare()
	{
		if (!WelfareService.isAlive || mService == null || mService.get() == null)
		{
			Intent i=new Intent();
			i.setClass(mFluxActivity.getContext().getApplicationContext(), WelfareService.class);
			mFluxActivity.getContext().getApplicationContext().startService(i);
			isNeedGrabWelfare = true;
		}
		else if (!mService.get().isGrabWelfare())
		{
			mService.get().startAutoGrab();
		}
		else
		{
			mFluxActivity.showToast("已经再抢红包啦");
		}
	}

	public void startLogin()
	{
		if (FluxUserManager.getInstance().getUser().getSessionID() != null)
		{
			mFluxActivity.setLoginStatus(mFluxActivity.LOGINING);
			mLoginUsecase.login(FluxUserManager.getInstance().getUser());
			if (FluxUserManager.getInstance().getUser().getTotalFlux() != -1)
			{
				int ava=FluxUserManager.getInstance().getUser().getAvailableFlux();
				int total=FluxUserManager.getInstance().getUser().getTotalFlux();
				String msg="流量:总共" + total + "M 可用" + ava + "M";
				mFluxActivity.setFlux(msg, (float)(total - ava) * 100 / total);
			}
		}
		else
		{
			mFluxActivity.showToast("未登录");
		}
	}

	public void startWelfareRecordActivity()
	{
		Intent i=new Intent();
		i.setClass(mFluxActivity.getContext(), WelfareRecordActivity.class);
		mFluxActivity.getContext().startActivity(i);
	}

	public void startGrabWelfareAtTime()
	{
		final long advanceTime=Long.parseLong(PreferenceManager.getDefaultSharedPreferences(mFluxActivity.getContext().getApplicationContext()).getString("advance_time", "3")) * 60 * 1000;
		final Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		TimePickerDialog dialog=new TimePickerDialog(mFluxActivity.getContext(), new TimePickerDialog.OnTimeSetListener(){

				@Override
				public void onTimeSet(TimePicker p1, int p2, int p3)
				{
					AlarmManager am=(AlarmManager) mFluxActivity.getContext().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
					ca.set(Calendar.HOUR_OF_DAY, p2);
					ca.set(Calendar.MINUTE, p3);
					ca.set(Calendar.SECOND, 0);
					ca.set(Calendar.MILLISECOND, 0);
					if (ca.getTimeInMillis() <= System.currentTimeMillis())
					{
						mFluxActivity.showToast("时间设定为明天");
						ca.add(Calendar.DAY_OF_MONTH, 1);
					}
					Intent si=new Intent();
					si.putExtra("auto", true);
					si.putExtra("act", ca.getTimeInMillis());
					si.setClass(mFluxActivity.getContext().getApplicationContext(), WelfareService.class);
					PendingIntent pi=PendingIntent.getService(mFluxActivity.getContext().getApplicationContext(), 0, si, PendingIntent.FLAG_UPDATE_CURRENT);
					am.cancel(pi);
					am.set(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis() - advanceTime, pi);
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//System.out.println(new Date(ca.getTimeInMillis() - advanceTime));
					mFluxActivity.setWelfareServiceStatus("自动抢红包:" + df.format(ca.getTime()), false);
					LogUtil.getInstance().logE("FluxPresenter","auto grab " + df.format(ca.getTime()));
					sp.edit().putString("time", "自动抢红包:" + df.format(ca.getTime())).commit();
				}
			}, ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE), true);
		dialog.show();
	}

	@Subscribe
	public void onFluxEventEventReceive(FluxEvent event)
	{
		mFluxActivity.setFluxProgressStatus(false);
		if (event.isSuccess())
		{
			if (event.getData().isSuccess())
			{
				int ava=event.getData().getData().getSum().getAvailable();
				int total=event.getData().getData().getSum().getTotal();
				String msg="流量:总共" + total + "M 可用" + ava + "M";
				mFluxActivity.setFlux(msg, (float)(total - ava) * 100 / total);
				FluxUserManager.getInstance().getUser().setAvailableFlux(ava);
				FluxUserManager.getInstance().getUser().setTotalFlux(total);
				FluxUserManager.getInstance().saveUser();
			}
			else
			{
				mFluxActivity.showToast(event.getData().getMsg());
			}
		}
		else
		{
			mFluxActivity.showToast("未知错误");
		}
	}

	@Subscribe
	public void onWelfareCookieReceive(WelfareCookieEvent event)
	{
		if (event.isSuccess())
		{
			FluxUserManager.getInstance().getUser().setCookie(event.getCookie());
			if (isNeedRefreshWelfareInfo)
			{
				isNeedRefreshWelfareInfo = false;
				startRefreshWelfareInfo();
			}
		}
	}

	@Subscribe
	public void onLoginEventReceive(LoginEvent event)
	{
		if (event.isSuccess())
		{
			mFluxActivity.setLoginStatus(event.getData().isSuccess()?mFluxActivity.LOGIN_SUCCESS:mFluxActivity.LOGIN_FAIL);
			FluxUserManager.getInstance().getUser().setIsLogin(event.getData().isSuccess());
			if (event.getData().isSuccess())
			{
				mFluxActivity.showToast(event.getData().isSuccess() ?"登录成功": "登录失败");
				mFluxActivity.setPhoneNum(FluxUserManager.getInstance().getUser().getPhone());
				FluxUserManager.getInstance().getUser().setToken(event.getData().getToken());
				FluxUserManager.getInstance().getUser().setSessionID(event.getData().getSessionID());
				FluxUserManager.getInstance().saveUser();
			}
			else
			{
				mFluxActivity.showToast(event.getData().getMsg());
			}
		}
		else
		{
			mFluxActivity.setLoginStatus(event.isSuccess()?mFluxActivity.LOGIN_SUCCESS:mFluxActivity.LOGIN_FAIL);
			mFluxActivity.showToast("未知错误");
		}
	}

	@Subscribe
	public void onWelfareInfoEventReceive(WelfareInfoEvent event)
	{
		mFluxActivity.setWelfareProgressStatus(false);
		if (event.isSuccess())
		{
			if (event.getData().isSuccess())
			{
				mFluxActivity.showToast("刷新成功");
				int total=event.getData().getData().getRule().getTotalNum();
				int remain=event.getData().getData().getRemain();
				String time=event.getData().isGrabing() ?"结束时间:" + event.getData().getData().getStartTime(): "开始时间:" + event.getData().getData().getStartTime();
				String type="类型:" + event.getData().getData().getRule().getPrize().get(0).getData().getName();
				String num="红包状态:剩余" + remain + "个 总共:" + total + "个";
				mFluxActivity.setWelfareInfo(num, time, type);
			}
			else
			{
				mFluxActivity.showToast(event.getData().getMsg());
			}
		}
		else
		{
			mFluxActivity.showToast("未知错误");
		}
	}

	@Subscribe
	public void onWelfareServiceEventReceive(WelfareServiceEvent event)
	{
		mFluxActivity.setWelfareServiceStatus(event.getMsg(), event.isGrabing());
	}

	@Subscribe
	public void onWelfareServiceCreatEventReceive(WelfareServiceCreatEvent event)
	{
		this.mService = event.getService();
		if (isNeedGrabWelfare)
		{
			isNeedGrabWelfare = false;
			mService.get().startAutoGrab();
		}
	}

	public void onDestroy()
	{
		mHandler.removeCallbacksAndMessages(null);
		BusProvide.getBus().unregister(this);
		mLoginUsecase.onDestroy();
		mWelfareUsecase.onDestroy();
		mFluxUsecase.onDestroy();
		if (mService != null && mService.get() != null)
		{
			mService.get().stopSelf();
		}
	}
}
