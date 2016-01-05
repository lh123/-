package com.lh.flux.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.lh.flux.R;
import com.lh.flux.view.FluxActivity;

public class SettingFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_aty);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findPreference("theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {

            @Override
            public boolean onPreferenceChange(Preference p1, Object p2)
            {
                Intent i = new Intent();
                i.setClass(getActivity().getApplication(), FluxActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            }
        });
    }

}
