package com.lh.flux.model.api;
import com.lh.flux.model.entity.*;
import java.io.*;
import java.net.*;
import com.lh.flux.model.utils.*;

public class LoginApiImpl implements LoginApi
{
	@Override
	public String getCap(User u)
	{
		URL url=null;
		HttpURLConnection conn=null;
		String temp=null;
		try
		{
			url=new URL(ApiStore.getCapApi(u.getPhone()));
			conn=(HttpURLConnection) url.openConnection();
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
	public String login(User u,String cap)
	{
		URL url=null;
		HttpURLConnection conn=null;
		String temp=null;
		try
		{
			url=new URL(ApiStore.getLoginApi());
			conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			StreamUtils.writeToStream(conn.getOutputStream(),generatePostInfoWithCap(u,cap).getBytes());
			temp=StreamUtils.readFromStream(conn.getInputStream());
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return temp;
	}

	@Override
	public String login(User u)
	{
		URL url=null;
		HttpURLConnection conn=null;
		String temp=null;
		try
		{
			url=new URL(ApiStore.getLoginApi());
			conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setFollowRedirects(true);
			conn.connect();
			StreamUtils.writeToStream(conn.getOutputStream(),generatePostInfoWithToken(u).getBytes());
			temp=StreamUtils.readFromStream(conn.getInputStream());
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return temp;
	}
	
	private String generatePostInfoWithToken(User u)
	{
		String temp="{\"SDK\":"+u.getSdk()+",\"channel\":\"default\",\"data\":{},\"imsi\":\""+u.getImsi()+"\",\"isPreInstalled\":false,\"manufacturer\":\""+u.getManufacturer()+"\",\"mode\":\""+u.getMode()+"\",\"sessionId\":\"" + u.getSessionID() + "\",\"token\":\"" + u.getToken() + "\",\"userPhone\":\"" + u.getPhone() + "\"}";
		return temp;
	}
	
	private String generatePostInfoWithCap(User u,String cap)
	{
		String temp="{\"SDK\":"+u.getSdk()+",\"captcha\":\"" + cap + "\",\"channel\":\"default\",\"data\":{},\"imei\":\""+u.getImei()+"\",\"imsi\":\""+u.getImsi()+"\",\"isIOS\":\"false\",\"isPreInstalled\":false,\"manufacturer\":\""+u.getManufacturer()+"\",\"mode\":\""+u.getMode()+"\",\"userPhone\":\"" + u.getPhone()+ "\"}";
		return temp;
	}
}
