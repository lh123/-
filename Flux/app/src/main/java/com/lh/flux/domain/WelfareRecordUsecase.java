package com.lh.flux.domain;

import android.os.*;
import com.lh.flux.model.api.*;
import com.lh.flux.model.entity.*;
import com.alibaba.fastjson.*;
import com.lh.flux.domain.event.*;

public class WelfareRecordUsecase
{
	private Handler mHandler;
	private WelfareRecordApi mWelfareRecordApi;

	public WelfareRecordUsecase(Handler mHandler)
	{
		this.mHandler = mHandler;
		mWelfareRecordApi=new WelfareRecordImpl();
		BusProvide.getBus().register(this);
	}
	
	public void getWelfareRecord(final User u)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String js=mWelfareRecordApi.getWelfareRecord(u);
					WelfareRecordEntity data=JSON.parseObject(js,WelfareRecordEntity.class);
					final WelfareRecordEvent e=new WelfareRecordEvent(data);
					sendToPresenter(e);
				}
			});
	}
	
	public void onDestroy()
	{
		BusProvide.getBus().unregister(this);
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
}
