package com.lh.liuliang.ui;
import android.content.*;

public interface IMainActivity
{
	public void setPhoneNum(String num)
	public void setLoginStatus(String status);
	public void setFluxInfo(int total,int avaliable)
	public void setWelfareStatus(String msg,String time,int remain,int total,String name)
	public void setWelfareServiceStatus(String msg)
	public void showToast(String mess)
	public void setPbRefreshFlux(int status)
	public void setPbRefreshWelfare(int status);
	public Context getContext()
	public Context getApplicationContext();
	public void startLogin()
}
