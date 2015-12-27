package com.lh.flux.model.api;
import com.lh.flux.model.entity.*;
import java.io.*;
import java.net.*;
import com.lh.flux.model.utils.*;

public class WelfareApiImpl implements WelfareApi
{
	@Override
	public String getWelfareStatus(User u)
	{
		URL url=null;
		HttpURLConnection conn=null;
		String temp=null;
		try
		{
			url=new URL(ApiStore.getWelfareStatusApi());
			conn=(HttpURLConnection) url.openConnection();
			//conn.setRequestProperty("Origin","http://game.hb189.mobi");
			conn.setRequestProperty("Accept", "application/json, text/plain, */*");
			conn.setRequestProperty("Referer", "http://game.hb189.mobi/welfare_android?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
			conn.setRequestProperty("Cookie", u.getCookie());
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.connect();
			temp=StreamUtils.readFromStream(conn.getInputStream());
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return temp;
	}

	@Override
	public String getWelfareCookie(User u)
	{
		URL url=null;
		HttpURLConnection conn=null;
		String cookie=null;
		try
		{
			url=new URL(ApiStore.getWelfareCookieApi(u.getPhone(),u.getSessionID()));
			conn=(HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.2; MI 2 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36");
			conn.setRequestProperty("X-Requested-With", "com.hoyotech.lucky_draw");
			conn.connect();
			cookie=conn.getHeaderField("Set-Cookie");
			if(cookie!=null)
			{
				cookie = cookie.split(";")[0];
			}
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return cookie;
	}

	@Override
	public String grabWelfare(User u)
	{
		URL url=null;
		HttpURLConnection conn=null;
		String temp=null;
		try
		{
			url=new URL(ApiStore.getGrabWelfareApi());
			conn=(HttpURLConnection) url.openConnection();
			//conn.setRequestProperty("Accept","application/json, text/plain, */*");
			conn.setRequestProperty("Referer", "http://game.hb189.mobi/welfare_android?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
			conn.setRequestProperty("Cookie", u.getCookie());
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			conn.connect();
			temp=StreamUtils.readFromStream(conn.getInputStream());
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return temp;
	}
	
}
