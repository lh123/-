package com.lh.flux.mvp.presenter;
import android.app.*;
import android.content.*;
import android.os.*;
import com.lh.flux.domain.*;
import com.lh.flux.domain.event.*;
import com.lh.flux.mvp.view.*;
import com.lh.flux.service.*;
import com.lh.flux.view.*;
import com.lh.flux.view.fragment.*;
import com.squareup.otto.*;
import com.umeng.update.*;
import java.util.*;

public class FluxPresenter
{
	private IFluxActivity mFluxActivity;
	private LoginUsecase mLoginUsecase;
	private WelfareUsecase mWelfareUsecase;
	private FluxUsecase mFluxUsecase;
	private Handler mHandler;
	private SharedPreferences sp;

	private boolean isNeedRefreshWelfareInfo=false;

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
		UmengUpdateAgent.update(mFluxActivity.getContext());
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
		Intent i=new Intent();
		i.setClass(mFluxActivity.getContext().getApplicationContext(), WelfareService.class);
		i.putExtra("mode", WelfareService.START_GRAB);
		mFluxActivity.getContext().getApplicationContext().startService(i);
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
		DatePickerFragment f=new DatePickerFragment();
		f.show(((FluxActivity)mFluxActivity).getSupportFragmentManager(),DatePickerFragment.TAG);
	}

	public void stopWelfareService()
	{
		if (isWelfareServiceRunning())
		{
			Intent i=new Intent();
			i.setClass(mFluxActivity.getContext().getApplicationContext(), WelfareService.class);
			i.putExtra("mode", WelfareService.STOP_SERVICE);
			mFluxActivity.getContext().getApplicationContext().startService(i);
		}
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
			mFluxActivity.showToast("请检查网络连接");
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
		else
		{
			if(isNeedRefreshWelfareInfo)
			{
				isNeedRefreshWelfareInfo=false;
				mFluxActivity.setWelfareProgressStatus(false);
				mFluxActivity.showToast("请检查网络连接");
			}
		}
	}

	@Subscribe
	public void onLoginEventReceive(LoginEvent event)
	{
		if (event.isSuccess())
		{
			mFluxActivity.setLoginStatus(event.getData().isSuccess() ?mFluxActivity.LOGIN_SUCCESS: mFluxActivity.LOGIN_FAIL);
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
			mFluxActivity.setLoginStatus(event.isSuccess() ?mFluxActivity.LOGIN_SUCCESS: mFluxActivity.LOGIN_FAIL);
			mFluxActivity.showToast("请检查网络连接");
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
			mFluxActivity.showToast("请检查网络连接");
		}
	}

	@Subscribe
	public void onWelfareServiceEventReceive(WelfareServiceEvent event)
	{
		mFluxActivity.setWelfareServiceStatus(event.getMsg(), event.isGrabing());
	}

	private boolean isWelfareServiceRunning()
	{
		boolean isRunning=false;
		ActivityManager am=(ActivityManager) mFluxActivity.getContext().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> list=am.getRunningServices(Integer.MAX_VALUE);
		for (int i=0;i < list.size();i++)
		{
			if (WelfareService.class.getName().equals(list.get(i).service.getClassName()))
			{
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	public void onDestroy()
	{
		mHandler.removeCallbacksAndMessages(null);
		BusProvide.getBus().unregister(this);
		mLoginUsecase.onDestroy();
		mWelfareUsecase.onDestroy();
		mFluxUsecase.onDestroy();
		stopWelfareService();
	}
}
