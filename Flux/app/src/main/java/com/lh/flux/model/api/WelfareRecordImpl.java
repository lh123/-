package com.lh.flux.model.api;
import com.lh.flux.model.entity.*;
import java.io.*;
import java.net.*;
import com.lh.flux.model.utils.*;

public class WelfareRecordImpl implements WelfareRecordApi
{
	@Override
	public String getWelfareRecord(User u)
	{
		URL url=null;
		HttpURLConnection conn=null;
		String temp=null;
		try
		{
			url=new URL(ApiStore.getGrabWelfareRecordApi());
			conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(3000);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Referer", "http://game.hb189.mobi/redEnvelopes/myPrize_android?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
			conn.setRequestProperty("Cookie", u.getCookie());
			conn.connect();
			String post="{\"phone\":\"" + u.getPhone() + "\"}";
			StreamUtils.writeToStream(conn.getOutputStream(),post.getBytes());
			temp=StreamUtils.readFromStream(conn.getInputStream());
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return temp;
	}

}
