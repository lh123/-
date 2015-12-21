package com.lh.flux.domain.event;
import com.lh.flux.model.entity.*;

public class LoginEvent
{
	private LoginEntity data;

	public LoginEvent(LoginEntity data)
	{
		this.data = data;
	}

	public void setData(LoginEntity data)
	{
		this.data = data;
	}

	public LoginEntity getData()
	{
		return data;
	}
	
	public boolean isSuccess()
	{
		return data!=null;
	}
}
