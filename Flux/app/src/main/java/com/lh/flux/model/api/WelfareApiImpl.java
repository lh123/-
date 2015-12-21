package com.lh.flux.model.api;
import com.lh.flux.model.entity.*;
import java.io.*;
import java.net.*;

public class WelfareApiImpl implements WelfareApi
{
	@Override
	public String getWelfareStatus(User u)
	{
		try
		{
			URL url=new URL(ApiStore.getWelfareStatusApi());
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			//conn.setRequestProperty("Origin","http://game.hb189.mobi");
			conn.setRequestProperty("Accept", "application/json, text/plain, */*");
			conn.setRequestProperty("Referer", "http://game.hb189.mobi/welfare_android?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
			conn.setRequestProperty("Cookie", u.getCookie());
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.connect();
			InputStream is=conn.getInputStream();
			InputStreamReader isr=new InputStreamReader(is);
			BufferedReader br=new BufferedReader(isr);
			StringBuilder sb=new StringBuilder();
			String temp;
			while ((temp = br.readLine()) != null)
			{
				sb.append(temp);
			}
			br.close();
			isr.close();
			is.close();
			return sb.toString();
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return null;
	}

	@Override
	public String getWelfareCookie(User u)
	{
		try
		{
			URL url=new URL(ApiStore.getWelfareCookieApi(u.getPhone(),u.getSessionID()));
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.2; MI 2 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36");
			conn.setRequestProperty("X-Requested-With", "com.hoyotech.lucky_draw");
			conn.connect();
			String cookie=conn.getHeaderField("Set-Cookie");
			if(cookie!=null)
			{
				cookie = cookie.split(";")[0];
				return cookie;
			}
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return null;
	}

	@Override
	public String grabWelfare(User u)
	{
		try
		{
			URL url=new URL(ApiStore.getGrabWelfareApi());
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			//conn.setRequestProperty("Accept","application/json, text/plain, */*");
			conn.setRequestProperty("Referer", "http://game.hb189.mobi/welfare_android?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
			conn.setRequestProperty("Cookie", u.getCookie());
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(2000);
			conn.connect();
			InputStream is=conn.getInputStream();
			InputStreamReader isr=new InputStreamReader(is);
			BufferedReader br=new BufferedReader(isr);
			StringBuilder sb=new StringBuilder();
			String temp;
			while ((temp = br.readLine()) != null)
			{
				sb.append(temp);
			}
			br.close();
			isr.close();
			is.close();
			return sb.toString();
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return null;
	}
	
}
