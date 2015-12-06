package com.lh.liuliang.crash;
import android.content.*;
import android.app.*;

public class CrashHandler implements Thread.UncaughtExceptionHandler
{
	private static CrashHandler handler=new CrashHandler();
	
	private Context mContext;
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private CrashHandler(){}
	
	public static CrashHandler getInstance()
	{
		return handler;
	}
	
	public void init(Context context)
	{
		mDefaultHandler=Thread.getDefaultUncaughtExceptionHandler();
		mContext=context;
	}
	
	@Override
	public void uncaughtException(Thread p1, Throwable p2)
	{
		Intent i=new Intent();
		i.setClass(mContext,CrashActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("error",p2);
		PendingIntent pi=PendingIntent.getActivity(mContext,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am=(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC,System.currentTimeMillis()+1000,pi);
		//android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
