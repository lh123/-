package com.lh.flux.model.api;

public class ApiStore
{
	public static final String HOST="http://game.hb189.mobi";
	public static final String CAP="/getCaptcha/";
	public static final String LOGIN="/login";
	public static final String FLUX="/user/checkUserFlux";
	public static final String WELFARE_STATUS="/redEnvelopes/envelopesInfo";
	public static final String WELFARE_COOKIE="/welfare_android?";
	public static final String GRAB_WELFARE="/redEnvelopes/grabEnvelopes";
	public static final String GRAB_WELFARE_RECORD="/redEnvelopes/grabEnvelopesRecord";
	
	public static String getLoginApi()
	{
		return HOST+LOGIN;
	}
	
	public static String getCapApi(String phone)
	{
		return HOST+CAP+phone;
	}
	
	public static String getFluxApi()
	{
		return HOST+FLUX;
	}
	
	public static String getWelfareStatusApi()
	{
		return HOST+WELFARE_STATUS;
	}
	
	public static String getWelfareCookieApi(String phone,String sessionID)
	{
		return HOST+WELFARE_COOKIE+"phone=" + phone + "&sessionId=" + sessionID;
	}
	
	public static String getGrabWelfareApi()
	{
		return HOST+GRAB_WELFARE;
	}
	
	public static String getGrabWelfareRecordApi()
	{
		return HOST+GRAB_WELFARE_RECORD;
	}
}
