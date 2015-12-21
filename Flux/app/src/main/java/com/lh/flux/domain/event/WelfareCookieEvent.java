package com.lh.flux.domain.event;

public class WelfareCookieEvent
{
	private String cookie;

	public WelfareCookieEvent(String cookie)
	{
		this.cookie = cookie;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getCookie()
	{
		return cookie;
	}
	
	public boolean isSuccess()
	{
		return cookie!=null;
	}
}
