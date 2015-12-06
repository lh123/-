package com.lh.liuliang.model;
import com.lh.liuliang.user.*;
import java.net.*;
import java.io.*;
import java.util.*;
import org.json.*;
import com.tencent.bugly.crashreport.*;
import com.lh.liuliang.threadpool.*;

public class MainModel
{
	public void getFlux(final UserInfo info, final MessCallBack back)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/user/checkUserFlux");
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						conn.setRequestMethod("POST");
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.setConnectTimeout(3000);
						conn.setReadTimeout(3000);
						conn.setRequestProperty("Origin", "http://game.hb189.mobi");
						conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
						conn.setRequestProperty("Referer", "http://game.hb189.mobi/android-personal-center?phone=" + info.getPhone() + "&sessionId=" + info.getSessionID());
						conn.connect();
						OutputStream ou=conn.getOutputStream();
						String postInfo="{\"phone\":\"" + info.getPhone() + "\"}";
						ou.write(postInfo.getBytes());
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
						if (back != null)
						{
							back.info(sb.toString());
						}
						return;
					}
					catch (MalformedURLException e)
					{}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (back != null)
					{
						back.info(null);
					}
				}
			});
	}

	public void getWelfareStatus(final UserInfo info, final MessCallBack back)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/redEnvelopes/envelopesInfo");
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						//conn.setRequestProperty("Origin","http://game.hb189.mobi");
						conn.setRequestProperty("Accept", "application/json, text/plain, */*");
						conn.setRequestProperty("Referer", "http://game.hb189.mobi/welfare_android?phone=" + info.getPhone() + "&sessionId=" + info.getSessionID());
						conn.setRequestProperty("Cookie", info.getCookie());
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
						if (back != null)
						{
							back.info(sb.toString());
						}
						return;
					}
					catch (MalformedURLException e)
					{}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (back != null)
					{
						back.info(null);
					}
				}
			});
	}

	public void getWelfareCookie(final UserInfo info, final MessCallBack back)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/welfare_android?phone=" + info.getPhone() + "&sessionId=" + info.getSessionID());
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(3000);
						conn.setReadTimeout(3000);
						conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
						conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
						conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.2; MI 2 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36");
						conn.setRequestProperty("X-Requested-With", "com.hoyotech.lucky_draw");
						conn.connect();
						String cookie=conn.getHeaderField("Set-Cookie");
						cookie = cookie.split(";")[0];
						if (back != null)
						{
							back.info(cookie);
						}
						return;
					}
					catch (MalformedURLException e)
					{}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (back != null)
					{
						back.info(null);
					}
				}
			});
	}

	public void grabWelfare(final UserInfo info, final MessCallBack back)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/redEnvelopes/grabEnvelopes");
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						//conn.setRequestProperty("Accept","application/json, text/plain, */*");
						conn.setRequestProperty("Referer", "http://game.hb189.mobi/welfare_android?phone=" + info.getPhone() + "&sessionId=" + info.getSessionID());
						conn.setRequestProperty("Cookie", info.getCookie());
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
						if (back != null)
						{
							back.info(sb.toString());
						}
						return;
					}
					catch (MalformedURLException e)
					{}
					catch (IOException e)
					{
						e.printStackTrace();
						//CrashReport.postCatchedException(e);
					}
					if (back != null)
					{
						back.info(null);
					}
				}
			});
	}

	public void loginWithToken(UserInfo info, final MessCallBack callBack)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/login");
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						conn.setRequestMethod("POST");
						conn.setConnectTimeout(3000);
						conn.setReadTimeout(3000);
						conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.connect();
						OutputStream ou=conn.getOutputStream();
						ou.write(UserInfo.getUserInfo().generatePostInfoWithToken().getBytes());
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
						if (callBack != null)
						{
							callBack.info(sb.toString());
						}
						return;
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					if (callBack != null)
					{
						callBack.info(null);
					}
				}
			});
	}

	public interface MessCallBack
	{
		public void info(String mess)
	}
}
