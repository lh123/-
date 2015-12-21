package com.lh.flux.model.api;

import com.lh.flux.model.entity.*;
import java.io.*;
import java.net.*;

public class FluxApiImpl implements FluxApi
{
	@Override
	public String getFluxInfo(User u)
	{
		try
		{
			URL url=new URL(ApiStore.getFluxApi());
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestProperty("Origin", "http://game.hb189.mobi");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Referer", "http://game.hb189.mobi/android-personal-center?phone=" + u.getPhone() + "&sessionId=" + u.getSessionID());
			conn.connect();
			OutputStream ou=conn.getOutputStream();
			String postInfo="{\"phone\":\"" + u.getPhone() + "\"}";
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
			return sb.toString();
		}
		catch (MalformedURLException e)
		{}
		catch (IOException e)
		{}
		return null;
	}
}
