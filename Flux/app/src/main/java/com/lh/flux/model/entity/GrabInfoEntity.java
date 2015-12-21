package com.lh.flux.model.entity;

public class GrabInfoEntity
{
	private String returnCode;
	private String msg;
	private WelfareEnvelopEntity data;

	public void setReturnCode(String returnCode)
	{
		this.returnCode = returnCode;
	}

	public String getReturnCode()
	{
		return returnCode;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setData(WelfareEnvelopEntity data)
	{
		this.data = data;
	}

	public WelfareEnvelopEntity getData()
	{
		return data;
	}
}
