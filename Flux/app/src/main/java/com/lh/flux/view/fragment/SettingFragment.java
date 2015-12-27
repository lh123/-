package com.lh.flux.view.fragment;
import android.os.*;
import android.preference.*;
import com.lh.flux.*;
import android.content.*;
import com.lh.flux.view.*;
import android.support.v7.app.*;

public class SettingFragment extends PreferenceFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_aty);
		((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		findPreference("theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){

				@Override
				public boolean onPreferenceChange(Preference p1, Object p2)
				{
					Intent i=new Intent();
					i.setClass(getActivity().getApplication(),FluxActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
					return true;
				}
			});
	}
	
}
