package com.lh.flux;
import android.app.*;
import android.content.*;
import android.os.*;
import com.lh.flux.crash.*;
import com.lh.flux.domain.*;
import com.squareup.leakcanary.*;
import com.tencent.bugly.crashreport.*;
import com.umeng.analytics.*;
import java.lang.reflect.*;

public class FluxApp extends Application
{
    private RefWatcher refWatcher;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		trySolveLeak();
		refWatcher = LeakCanary.install(this);
		FluxUserManager.getInstance().init(this);
		LogUtil.getInstance().init(this);
		MyCrashHandler.getInstance().init(this);
		Thread.setDefaultUncaughtExceptionHandler(MyCrashHandler.getInstance());
		MobclickAgent.setCatchUncaughtExceptions(false);
		CrashReport.initCrashReport(this,"900014048",false);
		ThemeUtil.getInstance().init(this);
	}

	public static RefWatcher getRefWatcher(Context context)
	{
        FluxApp application = (FluxApp) context.getApplicationContext();
        return application.refWatcher;
    }
	
	private void trySolveLeak()
	{
		try
		{
			UserManager um=(UserManager) getSystemService(USER_SERVICE);
			Class clss=um.getClass();
			Method m=clss.getDeclaredMethod("get", Context.class);
			m.invoke(um, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
