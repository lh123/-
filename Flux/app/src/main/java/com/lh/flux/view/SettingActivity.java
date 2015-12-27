package com.lh.flux.view;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import com.lh.flux.*;
import com.lh.flux.domain.*;
import com.lh.flux.view.fragment.*;
import com.umeng.analytics.*;

public class SettingActivity extends AppCompatActivity
{
	private Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		ThemeUtil.getInstance().setTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_aty);
		toolbar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getFragmentManager().beginTransaction().replace(R.id.setting_layout,new SettingFragment()).commit();
		getFragmentManager().beginTransaction().addToBackStack(null).commit();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
}
