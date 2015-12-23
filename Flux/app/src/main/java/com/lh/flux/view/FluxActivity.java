package com.lh.flux.view;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.lh.flux.*;
import com.lh.flux.mvp.presenter.*;
import com.lh.flux.mvp.view.*;

import android.support.v7.widget.Toolbar;
import com.umeng.analytics.*;

public class FluxActivity extends AppCompatActivity implements View.OnClickListener,IFluxActivity
{
	private Button btnLogin,btnRefreshFlux,btnRefreshWelfare,btnGrabWelfare,btnWelfareRecord,btnAutoGrabWelfare,btnLoginRety;
	private TextView tvPhone,tvLoginStatus,tvFlux,tvFluxDetail,tvWelfareStatus,tvNextTime,tvWelfareServiceStatus,tvWelfareType;
	private ProgressBar pbFlux,pbWelfareServiceStatus,pbRefreshFlux,pbRefreshWelfare,pbLoginStatus;
	private Toolbar toolbar;
	
	private FluxPresenter mPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flux_aty);
		toolbar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		btnLoginRety=(Button) findViewById(R.id.btn_login_retry);
		btnLogin=(Button) findViewById(R.id.btn_login);
		btnRefreshFlux=(Button) findViewById(R.id.btn_refresh_flux);
		btnRefreshWelfare=(Button) findViewById(R.id.btn_refresh_welfare);
		btnGrabWelfare=(Button) findViewById(R.id.btn_grab);
		btnWelfareRecord=(Button) findViewById(R.id.btn_welfare_record);
		btnAutoGrabWelfare=(Button) findViewById(R.id.btn_auto_grab_welfare);
		tvPhone=(TextView) findViewById(R.id.tv_phone);
		tvLoginStatus=(TextView) findViewById(R.id.tv_login_status);
		tvFlux=(TextView) findViewById(R.id.tv_flux);
		tvFluxDetail=(TextView) findViewById(R.id.tv_flux_detail);
		tvWelfareStatus=(TextView) findViewById(R.id.tv_welfare_status);
		tvNextTime=(TextView) findViewById(R.id.tv_next_time);
		tvWelfareServiceStatus=(TextView) findViewById(R.id.tv_welfare_service_status);
		tvWelfareType=(TextView) findViewById(R.id.tv_welfare_type);
		pbFlux=(ProgressBar) findViewById(R.id.pb_flux);
		pbWelfareServiceStatus=(ProgressBar) findViewById(R.id.pb_welfare_service_status);
		pbRefreshFlux=(ProgressBar) findViewById(R.id.pb_refresh_flux);
		pbRefreshWelfare=(ProgressBar) findViewById(R.id.pb_refresh_welfare);
		pbLoginStatus=(ProgressBar) findViewById(R.id.pb_login_status);
		mPresenter=new FluxPresenter(this);
		mPresenter.onCreate();
		btnLogin.setOnClickListener(this);
		btnRefreshFlux.setOnClickListener(this);
		btnRefreshWelfare.setOnClickListener(this);
		btnGrabWelfare.setOnClickListener(this);
		btnWelfareRecord.setOnClickListener(this);
		btnAutoGrabWelfare.setOnClickListener(this);
		btnLoginRety.setOnClickListener(this);
	}

	@Override
	public void onClick(View p1)
	{
		switch(p1.getId())
		{
			case R.id.btn_login:
				mPresenter.startLoginActivity();
				break;
			case R.id.btn_refresh_flux:
				mPresenter.startRefreshFlux();
				break;
			case R.id.btn_refresh_welfare:
				mPresenter.startRefreshWelfareInfo();
				break;
			case R.id.btn_grab:
				mPresenter.startGrabWelfare();
				break;
			case R.id.btn_welfare_record:
				mPresenter.startWelfareRecordActivity();
				break;
			case R.id.btn_auto_grab_welfare:
				mPresenter.startGrabWelfareAtTime();
				break;
			case R.id.btn_login_retry:
				mPresenter.startLogin();
				break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.setting:
				Intent i=new Intent();
				i.setClass(FluxActivity.this,SettingActivity.class);
				startActivity(i);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void showToast(String msg)
	{
		Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	}

	@Override
	public void setPhoneNum(String phone)
	{
		tvPhone.setText("电话:"+phone);
	}

	@Override
	public void setLoginStatus(int status)
	{
		boolean isLogin=false;
		if(status==LOGINING)
		{
			isLogin=false;
			tvLoginStatus.setText("状态:正在登录");
			pbLoginStatus.setVisibility(View.VISIBLE);
			btnLoginRety.setVisibility(View.INVISIBLE);
		}
		else if(status==LOGIN_SUCCESS)
		{
			isLogin=true;
			tvLoginStatus.setText("状态:已登录");
			pbLoginStatus.setVisibility(View.INVISIBLE);
			btnLoginRety.setVisibility(View.INVISIBLE);
		}
		else if(status==LOGIN_FAIL)
		{
			pbLoginStatus.setVisibility(View.INVISIBLE);
			tvLoginStatus.setText("状态:登录失败");
			btnLoginRety.setVisibility(View.VISIBLE);
		}
		btnRefreshFlux.setEnabled(isLogin);
		btnRefreshWelfare.setEnabled(isLogin);
		btnGrabWelfare.setEnabled(isLogin);
		btnWelfareRecord.setEnabled(isLogin);
		//btnAutoGrabWelfare.setEnabled(status);
	}

	@Override
	public void setFluxProgressStatus(boolean status)
	{
		pbRefreshFlux.setVisibility(status?View.VISIBLE:View.INVISIBLE);
		btnRefreshFlux.setVisibility(status?View.INVISIBLE:View.VISIBLE);
	}

	@Override
	public void setFlux(String msg, float rate)
	{
		tvFluxDetail.setText(msg);
		tvFlux.setText(String.format("%.1f%%",rate));
		pbFlux.setProgress((int)rate);
	}

	@Override
	public void setWelfareProgressStatus(boolean status)
	{
		pbRefreshWelfare.setVisibility(status?View.VISIBLE:View.INVISIBLE);
		btnRefreshWelfare.setVisibility(status?View.INVISIBLE:View.VISIBLE);
	}

	@Override
	public void setWelfareInfo(String num, String time, String type)
	{
		tvWelfareStatus.setText(num);
		tvNextTime.setText(time);
		tvWelfareType.setText(type);
	}

	@Override
	public void setWelfareServiceStatus(String msg, boolean isGrabing)
	{
		tvWelfareServiceStatus.setText("当前状态:"+msg);
		pbWelfareServiceStatus.setVisibility(isGrabing?View.VISIBLE:View.INVISIBLE);
	}

	@Override
	public Context getContext()
	{
		return this;
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
	
	@Override
	protected void onDestroy()
	{
		mPresenter.onDestroy();
		super.onDestroy();
	}

	long backFirst=-1;
	@Override
	public void onBackPressed()
	{
		long backSec=-1;
		if(backFirst<0)
		{
			backFirst=System.currentTimeMillis();
			showToast("退出将会终止抢红包,再按一次退出");
			return;
		}
		else
		{
			backSec=System.currentTimeMillis();
			if(backSec-backFirst<1000)
			{
				finish();
			}
			else
			{
				backFirst=System.currentTimeMillis();
				showToast("退出将会终止抢红包,再按一次退出");
			}
		}
	}
}
