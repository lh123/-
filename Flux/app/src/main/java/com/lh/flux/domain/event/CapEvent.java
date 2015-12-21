package com.lh.flux.domain.event;
import com.lh.flux.model.entity.*;

public class CapEvent
{
	private CapEntity data;

	public CapEvent(CapEntity data)
	{
		this.data = data;
	}

	public void setData(CapEntity data)
	{
		this.data = data;
	}

	public CapEntity getData()
	{
		return data;
	}
	
	public boolean isSuccess()
	{
		return data!=null;
	}
}
