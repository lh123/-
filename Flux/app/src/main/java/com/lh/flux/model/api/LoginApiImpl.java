package com.lh.flux.model.api;
import com.lh.flux.model.entity.*;
import java.io.*;
import java.net.*;

public class LoginApiImpl implements LoginApi
{
	@Override
	public String getCap(User u)
	{
		try
		{
			URL url=new URL(ApiStore.getCapApi(u.getPhone()));
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
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
	public String login(User u,String cap)
	{
		try
		{
			URL url=new URL(ApiStore.getLoginApi());
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.connect();
			OutputStream ou=conn.getOutputStream();
			ou.write(generatePostInfoWithCap(u,cap).getBytes());
			ou.flush();
			ou.close();
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
	public String login(User u)
	{
		try
		{
			URL url=new URL(ApiStore.getLoginApi());
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setFollowRedirects(true);
			conn.connect();
			OutputStream ou=conn.getOutputStream();
			ou.write(generatePostInfoWithToken(u).getBytes());
			ou.flush();
			ou.close();
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
