package com.lh.flux.domain;

import android.os.*;
import com.alibaba.fastjson.*;
import com.lh.flux.crash.*;
import com.lh.flux.domain.event.*;
import com.lh.flux.model.api.*;
import com.lh.flux.model.entity.*;

public class FluxUsecase
{
	private FluxApi mFluxApi;
	private Handler mHandler;

	public FluxUsecase(Handler handler)
	{
		mHandler = handler;
		mFluxApi=new FluxApiImpl();
		BusProvide.getBus().register(this);
	}
	
	public void getFluxInfo(final User u)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String msg=mFluxApi.getFluxInfo(u);
					LogUtil.getInstance().logE("FluxUsecase","获取流量");
					FluxEntity data=JSON.parseObject(msg,FluxEntity.class);
					final FluxEvent e=new FluxEvent(data);
					sendToPresenter(e);
				}
			});
	}
	
	private void sendToPresenter(final Object o)
	{
		if(Looper.myLooper()!=Looper.getMainLooper())
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
