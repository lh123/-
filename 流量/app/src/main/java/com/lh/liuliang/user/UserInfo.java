package com.lh.liuliang.user;
import android.content.*;
import com.lh.liuliang.preference.*;
import android.telephony.*;

public class UserInfo
{

	private static UserInfo info;

	private String phone;
	private String sessionID;
	private String token;
	private int totalFlux;
	private int availableFlux;
	private String cookie;
	private String imei;
	private String imsi;
	private String manufacturer;
	private String mode;
	private int sdk;

	private UserInfo()
	{}
	
	public static UserInfo getUserInfo()
	{
		if (info == null)
		{
			synchronized (UserInfo.class)
			{
				if (info == null)
				{
					info = new UserInfo();
				}
			}
		}
		return info;
	}

	public void init(Context context)
	{
		DataPre instance = DataPre.getInstance();
		phone = instance.getPhoneNum();
		sessionID = instance.getSeasonsID();
		token = instance.getToken();
		totalFlux = instance.getFlux()[1];
		availableFlux = instance.getFlux()[0];
		cookie = instance.getCookie();
		TelephonyManager te=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		imei=te.getDeviceId();
		imsi=te.getSubscriberId();
		manufacturer=android.os.Build.MANUFACTURER;
		mode=android.os.Build.MODEL;
		sdk=android.os.Build.VERSION.SDK_INT;
	}
	
	public void setSdk(int sdk)
	{
		this.sdk = sdk;
	}

	public int getSdk()
	{
		return sdk;
	}
	
	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}

	public String getImsi()
	{
		return imsi;
	}
	
	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public String getImei()
	{
		return imei;
	}

	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	public String getManufacturer()
	{
		return manufacturer;
	}

	public void setMode(String mode)
	{
		this.mode = mode;
	}

	public String getMode()
	{
		return mode;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getCookie()
	{
		return cookie;
	}

	public void setTotalFlux(int totalFlux)
	{
		this.totalFlux = totalFlux;
	}

	public int getTotalFlux()
	{
		return totalFlux;
	}

	public void setAvailableFlux(int availableFlux)
	{
		this.availableFlux = availableFlux;
	}

	public int getAvailableFlux()
	{
		return availableFlux;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setSessionID(String sessionID)
	{
		this.sessionID = sessionID;
	}

	public String getSessionID()
	{
		return sessionID;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getToken()
	{
		return token;
	}

	public String generatePostInfoWithCap(String cap)
	{
		String temp="{\"SDK\":"+sdk+",\"captcha\":\"" + cap + "\",\"channel\":\"default\",\"data\":{},\"imei\":\""+imei+"\",\"imsi\":\""+imsi+"\",\"isIOS\":\"false\",\"isPreInstalled\":false,\"manufacturer\":\""+manufacturer+"\",\"mode\":\""+mode+"\",\"userPhone\":\"" + phone + "\"}";
		return temp;
	}

	public String generatePostInfoWithToken()
	{
		String temp="{\"SDK\":"+sdk+",\"channel\":\"default\",\"data\":{},\"imsi\":\""+imsi+"\",\"isPreInstalled\":false,\"manufacturer\":\""+manufacturer+"\",\"mode\":\""+mode+"\",\"sessionId\":\"" + sessionID + "\",\"token\":\"" + token + "\",\"userPhone\":\"" + phone + "\"}";
		return temp;
	}
}
