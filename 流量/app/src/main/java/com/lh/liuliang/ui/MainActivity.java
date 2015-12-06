package com.lh.liuliang.ui;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.lh.liuliang.*;
import com.lh.liuliang.preference.*;
import com.lh.liuliang.presenter.*;
import com.tencent.bugly.crashreport.*;

import com.lh.liuliang.R;
import com.umeng.analytics.*;
import com.lh.liuliang.service.*;
import android.os.PowerManager.*;

public class MainActivity extends Activity implements IMainActivity
{
	private Button btnLogin,btnRefreshFlux,btnRefreshWelfare,btnGrabWelfare,btnWelfareRecord,btnAutoGrabWelfare;
	private TextView tvPhone,tvLoginStatus,tvFlux,tvFluxDetail,tvWelfareStatus,tvNextTime,tvWelfareServiceStatus,tvWelfareType;
	private ProgressBar pbFlux,pbWelfareServiceStatus,pbRefreshFlux,pbRefreshWelfare;
	
	private MainPresenter mPresenter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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
		mPresenter=new MainPresenter(this);
		mPresenter.checkStatus();
		btnLogin.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					startLogin();
				}
			});
		btnRefreshFlux.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mPresenter.getFlux();
				}
			});
		btnRefreshWelfare.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mPresenter.getWelfareStatus();
				}
			});
		btnGrabWelfare.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mPresenter.grabWelfareEnvelope();
				}
			});
		btnWelfareRecord.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mPresenter.startWelfareRecordAty();
				}
			});
		btnAutoGrabWelfare.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mPresenter.setAutoGrabWelfare();
				}
			});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode==0&&resultCode==0)
		{
			String token,id,phone;
			token=data.getStringExtra("token");
			id=data.getStringExtra("id");
			phone=data.getStringExtra("phone");
			System.out.println(id);
			mPresenter.getUser().setPhone(phone);
			mPresenter.getUser().setToken(token);
			mPresenter.getUser().setSessionID(id);
			DataPre.getInstance().savePhoneNum(phone);
			DataPre.getInstance().saveToken(token);
			DataPre.getInstance().saveSeasonsID(id);
			setPhoneNum(phone);
			setLoginStatus("已登录");
			mPresenter.getFlux();
		}
	}

	@Override
	public void setPbRefreshWelfare(int status)
	{
		pbRefreshWelfare.setVisibility(status);
		btnRefreshWelfare.setVisibility(status==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
	}

	@Override
	public void setPbRefreshFlux(int status)
	{
		pbRefreshFlux.setVisibility(status);
		btnRefreshFlux.setVisibility(status==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
	}
	
	@Override
	public void setPhoneNum(String num)
	{
		tvPhone.setText("号码:"+num);
	}

	@Override
	public void setLoginStatus(String status)
	{
		tvLoginStatus.setText("状态:"+status);
	}

	@Override
	public void setFluxInfo(int total, int avaliable)
	{
		//showToast("刷新成功");
		pbFlux.setMax(total);
		pbFlux.setProgress(total-avaliable);
		float rate=1-(float)avaliable/total;
		rate=rate*100;
		tvFlux.setText(String.format("%.1f%%",rate));
		tvFluxDetail.setText("流量情况:可用:"+avaliable+"MB   总共:"+total+"MB");
	}

	@Override
	public void setWelfareStatus(String msg,String time, int remain, int total,String name)
	{
		//showToast("刷新成功");
		String txt="红包状态:"+msg+" 还剩:"+remain+"个 总共:"+total+"个";
		if("抢红包进行中".equals(msg))
		{
			tvNextTime.setText("本轮结束时间:"+time);
		}
		else
		{
			tvNextTime.setText("下轮时间:"+time);
		}
		tvWelfareStatus.setText(txt);
		tvWelfareType.setText("类型:"+name);
	}
	
	@Override
	public void setWelfareServiceStatus(String msg)
	{
		tvWelfareServiceStatus.setText("状态:"+msg);
		if("抢红包中".equals(msg))
		{
			pbWelfareServiceStatus.setVisibility(View.VISIBLE);
		}
		else
		{
			pbWelfareServiceStatus.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void showToast(String mess)
	{
		Toast.makeText(this,mess,Toast.LENGTH_SHORT).show();
	}

	@Override
	public Context getContext()
	{
		return this;
	}

	@Override
	public void startLogin()
	{
		Intent i=new Intent();
		i.setClass(MainActivity.this,LoginActivity.class);
		startActivityForResult(i,0);
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
		mPresenter.unbindAndStopService();
		mPresenter.destory();
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
