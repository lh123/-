package com.lh.liuliang.crash;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.lh.liuliang.*;
import android.view.View.*;
import android.view.*;
import android.content.*;
import com.lh.liuliang.ui.*;
import com.umeng.analytics.*;
import java.io.*;

public class CrashActivity extends Activity
{
	private TextView tvCrashDetail;
	private Button btnReboot,btnClose;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crash);
		tvCrashDetail=(TextView) findViewById(R.id.tv_crash_detail);
		btnReboot=(Button) findViewById(R.id.btn_crash_reboot);
		btnClose=(Button) findViewById(R.id.btn_crash_close);
		//String error = getIntent().getStringExtra("detail");
		Throwable th=(Throwable) getIntent().getSerializableExtra("error");
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		th.printStackTrace(pw);
		String error=sw.toString();
		tvCrashDetail.setText(error);
		MobclickAgent.reportError(this,th);
		btnClose.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					finish();
				}
			});
		btnReboot.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Intent i=new Intent();
					i.setClass(getApplication(),MainActivity.class);
					startActivity(i);
					finish();
				}
			});
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
