package com.lh.flux.view;
import android.preference.*;
import android.os.*;
import com.lh.flux.*;

public class SettingActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_aty);
	}
}
