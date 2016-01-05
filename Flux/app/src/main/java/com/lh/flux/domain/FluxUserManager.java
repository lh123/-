package com.lh.flux.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.lh.flux.model.entity.User;

public class FluxUserManager
{
    private User user;
    private SharedPreferences mPreference;
    private TelephonyManager tm;
    private Context mContext;

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
        mContext=context;
        mPreference = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public void refreshUser()
    {
        if(mPreference.contains("imei")&&mPreference.contains("imsi"))
        {
            user.setImei(mPreference.getString("imei",null));
            user.setImsi(mPreference.getString("imsi",null));
        }
        else
        {
            tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            user.setImei(tm.getDeviceId());
            user.setImsi(tm.getSubscriberId());
        }
        user.setManufacturer(android.os.Build.MANUFACTURER);
        user.setMode(android.os.Build.MODEL);
        user.setSdk(android.os.Build.VERSION.SDK_INT);
        user.setPhone(mPreference.getString("phone", null));
        user.setToken(mPreference.getString("token", null));
        user.setSessionID(mPreference.getString("sessionID", null));
        user.setAvailableFlux(mPreference.getInt("availableFlux", 0));
        user.setTotalFlux(mPreference.getInt("totalFlux", 0));
    }

    public void saveUser()
    {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString("imei",user.getImei());
        edit.putString("imsi",user.getImsi());
        edit.putString("phone", user.getPhone());
        edit.putString("token", user.getToken());
        edit.putString("sessionID", user.getSessionID());
        edit.putInt("availableFlux", user.getAvailableFlux());
        edit.putInt("totalFlux", user.getTotalFlux());
        edit.apply();
    }

    public User getUser()
    {
        return user;
    }
}
