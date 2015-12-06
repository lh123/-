package com.lh.liuliang.crash;

import com.tencent.bugly.crashreport.*;
import java.util.*;
import android.content.*;
import android.app.*;

public class CrashCallBack extends CrashReport.CrashHandleCallback
{
	private Context mContext;

	public CrashCallBack(Context context)
	{
		mContext = context;
	}
	@Override
	public Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack)
	{
		Intent i=new Intent();
		i.setClass(mContext,CrashActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("detail",errorStack);
		PendingIntent pi=PendingIntent.getActivity(mContext,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC,System.currentTimeMillis()+1000,pi);
		return null;
	}
}
