package com.lh.liuliang.ui;
import android.app.*;
import android.os.*;
import com.lh.liuliang.*;
import android.widget.*;
import com.lh.liuliang.presenter.*;
import android.view.View.*;
import android.view.*;
import android.content.*;
import com.umeng.analytics.*;

public class LoginActivity extends Activity implements ILoginActivity
{
	private EditText phoneNum,capNum;
	private Button login,getCap;
	
	private LoginPresenter mPresenter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mPresenter=new LoginPresenter(this);
		phoneNum=(EditText) findViewById(R.id.phone);
		capNum=(EditText) findViewById(R.id.cap);
		login=(Button) findViewById(R.id.login);
		getCap=(Button) findViewById(R.id.get_cap);
		mPresenter.getSaveInfo();
		login.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mPresenter.login();
				}
			});
		getCap.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					mPresenter.getCap();
				}
			});
	}

	@Override
	public String getPhoneNum()
	{
		return phoneNum.getText().toString();
	}

	@Override
	public void setPhoneNum(String num)
	{
		phoneNum.setText(num);
	}

	@Override
	public String getCapNum()
	{
		return capNum.getText().toString();
	}

	@Override
	public void setResult(String token, String seasonID)
	{
		Intent i=new Intent();
		i.putExtra("token",token);
		i.putExtra("id",seasonID);
		i.putExtra("phone",getPhoneNum());
		setResult(0,i);
		finish();
	}

	@Override
	public void onBackPressed()
	{
		setResult(-1);
		super.onBackPressed();
	}
	
	@Override
	public void showToast(String message)
	{
		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
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
		mPresenter.destroy();
		super.onDestroy();
	}
}
