package com.lh.flux.domain;
import android.os.*;
import com.alibaba.fastjson.*;
import com.lh.flux.domain.*;
import com.lh.flux.model.api.*;
import com.lh.flux.model.entity.*;
import com.lh.flux.domain.event.*;
import com.lh.flux.crash.*;

public class WelfareUsecase
{
	private WelfareApi mWelfareApi;
	private Handler mHandler;

	public WelfareUsecase(Handler handler)
	{
		BusProvide.getBus().register(this);
		mWelfareApi = new WelfareApiImpl();
		mHandler = handler;
	}

	public void getWelfareInfo(final User u)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String msg=mWelfareApi.getWelfareStatus(u);
					LogUtil.getInstance().logE("WelfareUsecase",msg);
					WelfareInfoEntity data=JSON.parseObject(msg, WelfareInfoEntity.class);
					final WelfareInfoEvent e=new WelfareInfoEvent(data);
					sendToPresenter(e);
				}
			});
	}

	public void getWelfareCoookie(final User u)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String cookie=mWelfareApi.getWelfareCookie(u);
					LogUtil.getInstance().logE("WelfareUsecase","Cookie="+cookie);
					final WelfareCookieEvent e=new WelfareCookieEvent(cookie);
					sendToPresenter(e);
				}
			});
	}

	public void grabWelfare(final User u)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String msg=mWelfareApi.grabWelfare(u);
					LogUtil.getInstance().logE("WelfareUsecase",msg);
					GrabInfoEntity data=JSON.parseObject(msg, GrabInfoEntity.class);
					final GrabEvent e=new GrabEvent(data);
					sendToPresenter(e);
				}
			});
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

	public void onDestroy()
	{
		BusProvide.getBus().unregister(this);
	}
}
