package com.lh.flux.domain.event;
import com.lh.flux.service.*;
import java.lang.ref.*;

public class WelfareServiceCreatEvent
{
	private WeakReference<WelfareService> mService;

	public WelfareServiceCreatEvent(WeakReference<WelfareService> mService)
	{
		this.mService = mService;
	}


	public void setService(WeakReference<WelfareService> service)
	{
		this.mService = service;
	}

	public WeakReference<WelfareService> getService()
	{
		return mService;
	}
}
