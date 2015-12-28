package com.lh.flux.domain;

import android.app.*;
import android.content.*;
import android.preference.*;
import com.lh.flux.*;

public class ThemeUtil
{
	private static final ThemeUtil mThemeUtil=new ThemeUtil();
	private String mode="0";
	private Context mContext;
	private ThemeUtil()
	{}
	
	public void init(Context context)
	{
		mContext=context;
	}
	
	public static ThemeUtil getInstance()
	{
		return mThemeUtil;
	}
	
	public void setTheme(Activity a)
	{
		mode=PreferenceManager.getDefaultSharedPreferences(mContext).getString("theme","0");
		switch(mode)
		{
			case "0":
				a.setTheme(R.style.ThemeBlue);
				break;
			case "1":
				a.setTheme(R.style.ThemeRed);
				break;
			default:
				a.setTheme(R.style.ThemeBlue);
				break;
		}
	}
	
	public int getCurrentTheme()
	{
		return Integer.parseInt(mode);
	}
}
