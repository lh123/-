package com.lh.flux.domain;
import com.squareup.otto.*;

public class BusProvide
{
	private static Bus mBus;
	
	public static Bus getBus()
	{
		if(mBus==null)
		{
			synchronized(BusProvide.class)
			{
				if(mBus==null)
				{
					mBus=new Bus();
				}
			}
		}
		return mBus;
	}
}
