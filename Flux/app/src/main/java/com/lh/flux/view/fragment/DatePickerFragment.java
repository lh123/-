package com.lh.flux.view.fragment;
import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.support.v4.app.*;
import android.widget.*;
import com.lh.flux.crash.*;
import com.lh.flux.service.*;
import com.lh.flux.view.*;
import java.text.*;
import java.util.*;
import u.aly.*;

import android.support.v4.app.DialogFragment;
import com.lh.flux.domain.*;
import com.lh.flux.*;

public class DatePickerFragment extends DialogFragment
{
	public static final String TAG="DatePickerFragment";
	
	//private DatePickerDialog mDialog;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		final SharedPreferences sp = getActivity().getSharedPreferences("auto_grab", Context.MODE_PRIVATE);
		final long advanceTime=Long.parseLong(PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("advance_time", "3")) * 60 * 1000;
		final Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		int theme=ThemeUtil.getInstance().getCurrentTheme()==0?R.style.DialogBlue:R.style.DialogRed;
		TimePickerDialog dialog=new TimePickerDialog(getActivity(),theme ,new TimePickerDialog.OnTimeSetListener(){

				@Override
				public void onTimeSet(TimePicker p1, int p2, int p3)
				{
					AlarmManager am=(AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
					ca.set(Calendar.HOUR_OF_DAY, p2);
					ca.set(Calendar.MINUTE, p3);
					ca.set(Calendar.SECOND, 0);
					ca.set(Calendar.MILLISECOND, 0);
					if (ca.getTimeInMillis() <= System.currentTimeMillis())
					{
						((FluxActivity)getActivity()).showToast("时间设定为明天");
						ca.add(Calendar.DAY_OF_MONTH, 1);
					}
					Intent si=new Intent();
					si.putExtra("mode", WelfareService.START_GRAB_DELY);
					si.putExtra("act", ca.getTimeInMillis());
					si.setClass(getActivity().getApplicationContext(), WelfareService.class);
					PendingIntent pi=PendingIntent.getService(getActivity().getApplicationContext(), 0, si, PendingIntent.FLAG_UPDATE_CURRENT);
					am.cancel(pi);
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.MNC)
					{
						am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis() - advanceTime, pi);
					}
					else
					{
						am.setExact(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis() - advanceTime, pi);
					}
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					((FluxActivity)getActivity()).setWelfareServiceStatus("自动抢红包:" + df.format(ca.getTime()), false);
					LogUtil.getInstance().logE("FluxPresenter", "auto grab " + df.format(ca.getTime()));
					sp.edit().putString("time", "自动抢红包:" + df.format(ca.getTime())).commit();
				}
			}, ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE), true);
		return dialog;
	}
	
}
