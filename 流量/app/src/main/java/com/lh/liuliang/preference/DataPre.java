package com.lh.liuliang.preference;
import android.content.*;

public class DataPre
{
	private static Context mContext;
	private static DataPre mDataPre;
	
	public static void init(Context context)
	{
		mContext=context;
	}
	
	public static DataPre getInstance()
	{
		if(mDataPre==null)
		{
			synchronized(DataPre.class)
			{
				if(mDataPre==null)
				{
					mDataPre=new DataPre();
					return mDataPre;
				}
			}
		}
		return mDataPre;
	}
	
	public void savePhoneNum(String phone)
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		sp.edit().putString("phone",phone).commit();
	}
	
	public String getPhoneNum()
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		return sp.getString("phone","未知");
	}
	
	public void saveSeasonsID(String id)
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		sp.edit().putString("id",id).commit();
	}

	public String getSeasonsID()
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		return sp.getString("id",null);
	}
	
	public void saveToken(String token)
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		sp.edit().putString("token",token).commit();
	}

	public String getToken()
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		return sp.getString("token",null);
	}
	
	public void saveFlux(int avaliable,int total)
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		sp.edit().putInt("available",avaliable).commit();
		sp.edit().putInt("total",total).commit();
	}

	public int[] getFlux()
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		int avaliable=sp.getInt("available",0);
		int total=sp.getInt("total",0);
		return new int[]{avaliable,total};
	}
	
	public void saveCookie(String cookie)
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		sp.edit().putString("cookie",cookie).commit();
	}

	public String getCookie()
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		return sp.getString("cookie",null);
	}
	
	public void saveAutoGrabWelfareStatus(String msg)
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		sp.edit().putString("auto_grab",msg).commit();
	}
	
	public String getAutoGrabWelfareStatus()
	{
		SharedPreferences sp=mContext.getSharedPreferences("data",mContext.MODE_PRIVATE);
		return sp.getString("auto_grab",null);
	}
}
