package com.lh.liuliang;
import android.app.*;
import android.content.*;
import com.lh.liuliang.crash.*;
import com.lh.liuliang.preference.*;
import com.lh.liuliang.user.*;
import com.squareup.leakcanary.*;
import com.tencent.bugly.crashreport.*;
import com.umeng.analytics.*;
import com.umeng.update.*;

public class App extends Application
{
	public static RefWatcher getWacther(Context context)
	{
		return ((App)context.getApplicationContext()).watcher;
	}
	
	public RefWatcher watcher;
	@Override
	public void onCreate()
	{
		super.onCreate();
		watcher=LeakCanary.install(this);
		DataPre.init(this);
		UserInfo.getUserInfo().init(this);
		CrashHandler.getInstance().init(this);
		Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
		MobclickAgent.setCatchUncaughtExceptions(false);
		UmengUpdateAgent.update(this);
		CrashReport.initCrashReport(this, "900014048", false);
		LogUtil.getInstance().init(this);
	}
}
