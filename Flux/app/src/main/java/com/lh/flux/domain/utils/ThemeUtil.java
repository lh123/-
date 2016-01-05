package com.lh.flux.domain.utils;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

import com.lh.flux.R;

public class ThemeUtil
{
    private static final ThemeUtil mThemeUtil = new ThemeUtil();
    private String mode = "0";
    private Context mContext;

    private ThemeUtil()
    {}

    public void init(Context context)
    {
        mContext = context;
    }

    public static ThemeUtil getInstance()
    {
        return mThemeUtil;
    }

    public void setDialogTheme(Activity a)
    {
        mode = PreferenceManager.getDefaultSharedPreferences(mContext).getString("theme", "0");
        switch (mode)
        {
            case "0":
                a.setTheme(R.style.DialogBlue);
                break;
            case "1":
                a.setTheme(R.style.DialogRed);
                break;
            default:
                a.setTheme(R.style.DialogBlue);
                break;
        }
    }

    public void setTheme(Activity a)
    {
        mode = PreferenceManager.getDefaultSharedPreferences(mContext).getString("theme", "0");
        switch (mode)
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
