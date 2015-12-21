package com.lh.flux.domain.event;
import com.lh.flux.model.entity.*;

public class WelfareRecordEvent
{
	private WelfareRecordEntity data;

	public WelfareRecordEvent(WelfareRecordEntity data)
	{
		this.data = data;
	}

	public void setData(WelfareRecordEntity data)
	{
		this.data = data;
	}

	public WelfareRecordEntity getData()
	{
		return data;
	}
	
	public boolean isSuccess()
	{
		return data!=null;
	}
}
