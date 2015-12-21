package com.lh.flux;
import android.app.*;
import android.content.*;
import com.lh.flux.crash.*;
import com.lh.flux.domain.*;
import com.squareup.leakcanary.*;
import java.lang.reflect.*;
import android.os.*;

public class FluxApp extends Application
{
	public static RefWatcher getRefWatcher(Context context) {
        FluxApp application = (FluxApp) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		trySolveLeak();
		refWatcher = LeakCanary.install(this);
		FluxUserManager.getInstance().init(this);
		LogUtil.getInstance().init(this);
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
