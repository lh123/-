package com.lh.flux.mvp.view;
import android.content.*;

public interface IFluxActivity
{
	public static final int LOGINING=0;
	public static final int LOGIN_SUCCESS=1;
	public static final int LOGIN_FAIL=2;
	
	public void showToast(String msg)
	public void setPhoneNum(String phone)
	public void setLoginStatus(int status)
	public void setFluxProgressStatus(boolean status)
	public void setFlux(String msg,float rate)
	public void setWelfareProgressStatus(boolean status)
	public void setWelfareInfo(String num,String time,String type)
	public void setWelfareServiceStatus(String msg,boolean isGrabing)
	public Context getContext()
}
