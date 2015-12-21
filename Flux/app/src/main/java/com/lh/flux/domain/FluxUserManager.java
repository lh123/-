package com.lh.flux.domain;
import android.content.*;
import android.telephony.*;
import com.lh.flux.model.entity.*;

public class FluxUserManager
{
	private User user;
	private SharedPreferences mPreference;
	private static FluxUserManager userManager;

	private FluxUserManager()
	{
		user = new User();
	}
	
	public static FluxUserManager getInstance()
	{
		if (userManager == null)
		{
			synchronized (FluxUserManager.class)
			{
				if (userManager == null)
				{
					userManager = new FluxUserManager();
				}
			}
		}
		return userManager;
	}

	public void init(Context context)
	{
		mPreference=context.getSharedPreferences("user",context.MODE_PRIVATE);
		TelephonyManager tm=(TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		user.setImei(tm.getDeviceId());
		user.setImsi(tm.getSubscriberId());
		user.setManufacturer(android.os.Build.MANUFACTURER);
		user.setMode(android.os.Build.MODEL);
		user.setSdk(android.os.Build.VERSION.SDK_INT);
		refreshUser();
	}
	
	public void refreshUser()
	{
		user.setPhone(mPreference.getString("phone",null));
		user.setToken(mPreference.getString("token",null));
		user.setSessionID(mPreference.getString("sessionID",null));
		user.setAvailableFlux(mPreference.getInt("availableFlux",0));
		user.setTotalFlux(mPreference.getInt("totalFlux",0));
	}
	
	public void saveUser()
	{
		SharedPreferences.Editor edit=mPreference.edit();
		edit.putString("phone",user.getPhone());
		edit.putString("token",user.getToken());
		edit.putString("sessionID",user.getSessionID());
		edit.putInt("availableFlux",user.getAvailableFlux());
		edit.putInt("totalFlux",user.getTotalFlux());
		edit.commit();
	}
	
	public User getUser()
	{
		return user;
	}
}
