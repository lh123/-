package com.lh.liuliang;
import android.app.*;
import com.lh.liuliang.crash.*;
import com.lh.liuliang.preference.*;
import com.lh.liuliang.user.*;
import com.tencent.bugly.crashreport.*;
import com.umeng.analytics.*;
import com.umeng.update.*;

public class App extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		DataPre.init(this);
		UserInfo.getUserInfo().init(this);
		CrashHandler.getInstance().init(this);
		Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
		MobclickAgent.setCatchUncaughtExceptions(false);
		UmengUpdateAgent.update(this);
		CrashReport.initCrashReport(this, "900014048", false);
	}
}
