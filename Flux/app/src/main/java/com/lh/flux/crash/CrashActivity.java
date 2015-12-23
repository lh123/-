package com.lh.flux.crash;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import com.lh.flux.*;
import java.io.*;
import com.lh.flux.view.*;

public class CrashActivity extends AppCompatActivity implements View.OnClickListener
{
	private TextView tvCrash;
	private Button btnReboot,btnClose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crash_aty);
		Throwable e=(Throwable) getIntent().getSerializableExtra("error");
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		e.printStackTrace(pw);
		setFinishOnTouchOutside(false);
		tvCrash=(TextView) findViewById(R.id.tv_crash);
		btnReboot=(Button) findViewById(R.id.btn_reboot);
		btnClose=(Button) findViewById(R.id.btn_close);
		tvCrash.setText(sw.toString());
		btnClose.setOnClickListener(this);
		btnReboot.setOnClickListener(this);
	}

	@Override
	public void onClick(View p1)
	{
		switch(p1.getId())
		{
			case R.id.btn_close:
				System.exit(0);
				break;
			case R.id.btn_reboot:
				Intent i=new Intent();
				i.setClass(getApplicationContext(),FluxActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
		}
	}
}
