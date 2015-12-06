package com.lh.liuliang.model;
import java.net.*;
import java.io.*;
import org.json.*;
import android.widget.*;
import android.content.*;
import com.lh.liuliang.user.*;
import com.lh.liuliang.threadpool.*;

public class LoginModel
{
	public void getCap(final String phone,final MessCallBack callBack)
	{
		ThreadManager.getInstance().startThread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						URL url=new URL("http://game.hb189.mobi/getCaptcha/" + phone);
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(3000);
						conn.setReadTimeout(3000);
						conn.connect();
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
						callBack.info(sb.toString());
						return;
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					callBack.info(null);
				}
			});
	}
	
	public void loginWithCap(final String phone,final String cap,final MessCallBack callBack)
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
						conn.setRequestProperty("Content-type","application/json; charset=utf-8");
						conn.setDoInput(true);
						conn.setDoOutput(true);
						conn.connect();
						OutputStream ou=conn.getOutputStream();
						ou.write(UserInfo.getUserInfo().generatePostInfoWithCap(cap).getBytes());
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
						callBack.info(sb.toString());
						return;
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					callBack.info(null);
				}
			});
	}
	
	public interface MessCallBack
	{
		public void info(String info)
	}
}
