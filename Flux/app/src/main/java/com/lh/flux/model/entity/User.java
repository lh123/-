package com.lh.flux.model.entity;

public class User
{
	private String phone;
	private String sessionID;
	private String token;
	private int totalFlux=-1;
	private int availableFlux=-1;
	private String cookie;
	private String imei;
	private String imsi;
	private String manufacturer;
	private String mode;
	private int sdk;
	
	private boolean isLogin=false;

	public void setIsLogin(boolean isLogin)
	{
		this.isLogin = isLogin;
	}

	public boolean isLogin()
	{
		return isLogin;
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

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getCookie()
	{
		return cookie;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public String getImei()
	{
		return imei;
	}

	public void setImsi(String imsi)
	{
		this.imsi = imsi;
	}

	public String getImsi()
	{
		return imsi;
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

	public void setSdk(int sdk)
	{
		this.sdk = sdk;
	}

	public int getSdk()
	{
		return sdk;
	}
}
