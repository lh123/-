package com.lh.flux.domain;
import android.os.*;
import com.alibaba.fastjson.*;
import com.lh.flux.crash.*;
import com.lh.flux.domain.event.*;
import com.lh.flux.model.api.*;
import com.lh.flux.model.entity.*;

public class LoginUsecase
{
	private LoginApi loginApi;
	private Handler mHandler;
	
	public LoginUsecase(Handler handler)
	{
		loginApi=new LoginApiImpl();
		mHandler=handler;
		BusProvide.getBus().register(this);
	}
	
	public void getCap(final User u)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String js=loginApi.getCap(u);
					LogUtil.getInstance().logE("LoginUsecase","获取验证码:"+js);
					CapEntity data = JSON.parseObject(js, CapEntity.class);
					final CapEvent e=new CapEvent(data);
					sendToPresenter(e);
				}
			});
	}
	
	public void loginWithCap(final User u,final String cap)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String js=loginApi.login(u,cap);
					LogUtil.getInstance().logE("LoginUsecase","登录:"+js);
					LoginEntity data=JSON.parseObject(js,LoginEntity.class);
					final LoginEvent e=new LoginEvent(data);
					sendToPresenter(e);
				}
			});
	}
	
	public void login(final User u)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					String js=loginApi.login(u);
					LogUtil.getInstance().logE("LoginUsecase","登录:"+js);
					LoginEntity data=JSON.parseObject(js,LoginEntity.class);
					final LoginEvent e=new LoginEvent(data);
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
