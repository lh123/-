package com.lh.liuliang.model;

import com.lh.liuliang.threadpool.*;
import com.lh.liuliang.user.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

public class WelfareRecordModel
{
	public void getWelfareRecord(final UserInfo info,final WelfareRecordCallBack back)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/redEnvelopes/grabEnvelopesRecord");
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						conn.setRequestMethod("POST");
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.setConnectTimeout(3000);
						conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
						conn.setRequestProperty("Referer","http://game.hb189.mobi/redEnvelopes/myPrize_android?phone="+info.getPhone()+"&sessionId="+info.getSessionID());
						conn.setRequestProperty("Cookie",info.getCookie());
						conn.connect();
						OutputStream ou=conn.getOutputStream();
						String post="{\"phone\":\""+info.getPhone()+"\"}";
						ou.write(post.getBytes());
						ou.flush();
						ou.close();
						InputStream is=conn.getInputStream();
						InputStreamReader isr=new InputStreamReader(is);
						BufferedReader br=new BufferedReader(isr);
						StringBuilder sb=new StringBuilder();
						String temp;
						while((temp=br.readLine())!=null)
						{
							sb.append(temp);
						}
						br.close();
						isr.close();
						is.close();
						JSONObject obj=new JSONObject(sb.toString());
						JSONArray data=obj.getJSONArray("data");
						ArrayList<WelfareInfo> list=new ArrayList<WelfareInfo>();
						for(int i=0;i<data.length();i++)
						{
							WelfareInfo welfareObj=new WelfareInfo();
							JSONObject prize=data.getJSONObject(i).getJSONObject("prize");
							welfareObj.setDes(prize.optString("description",null));
							welfareObj.setTime(data.getJSONObject(i).optString("addtime",null));
							welfareObj.setStatus(data.getJSONObject(i).optInt("status",0));
							list.add(welfareObj);
						}
						back.welfareRecord(list);
						return;
					}
					catch (JSONException e)
					{}
					catch (MalformedURLException e)
					{}
					catch (IOException e)
					{}
					back.welfareRecord(null);
				}
			});
	}
	
	public void getWelfareCookie(final UserInfo info,final MessCallBack back)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/welfare_android?phone="+info.getPhone()+"&sessionId="+info.getSessionID());
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(3000);
						conn.setRequestProperty("Accept-Encoding","gzip,deflate");
						conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
						conn.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 5.0.2; MI 2 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36");
						conn.setRequestProperty("X-Requested-With","com.hoyotech.lucky_draw");
						conn.connect();
						String cookie=conn.getHeaderField("Set-Cookie");
						cookie=cookie.split(";")[0];
						back.info(cookie);
						return;
					}
					catch (MalformedURLException e)
					{}
					catch (IOException e)
					{}
					back.info(null);
				}
			});
	}
	
	public interface MessCallBack
	{
		public void info(String mess)
	}
	
	public interface WelfareRecordCallBack
	{
		public void welfareRecord(ArrayList<WelfareInfo> list);
	}
}
