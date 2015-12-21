package com.lh.flux.model.api;
import com.lh.flux.model.entity.*;
import java.io.*;
import java.net.*;

public class WelfareRecordImpl implements WelfareRecordApi
{
	@Override
	public String getWelfareRecord(User u)
	{
		try
		{
			URL url=new URL(ApiStore.getGrabWelfareRecordApi());
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(3000);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Referer", "http://game.hb189.mobi/redEnvelopes/myPrize_android?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
			conn.setRequestProperty("Cookie", u.getCookie());
			conn.connect();
			OutputStream ou=conn.getOutputStream();
			String post="{\"phone\":\"" + u.getPhone() + "\"}";
			ou.write(post.getBytes());
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

}
