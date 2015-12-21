package com.lh.flux.domain.event;
import java.util.*;
import com.lh.flux.model.entity.*;

public class FluxEvent
{
	private FluxEntity data;

	public FluxEvent(FluxEntity data)
	{
		this.data = data;
	}

	public void setData(FluxEntity data)
	{
		this.data = data;
	}

	public FluxEntity getData()
	{
		return data;
	}
	
	public boolean isSuccess()
	{
		return data!=null;
	}
}
