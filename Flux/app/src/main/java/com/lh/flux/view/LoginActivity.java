package com.lh.flux.view;

import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.lh.flux.*;
import com.lh.flux.mvp.presenter.*;
import com.lh.flux.mvp.view.*;
import android.support.v7.widget.Toolbar;
import com.umeng.analytics.*;
import com.lh.flux.domain.*;

public class LoginActivity extends AppCompatActivity implements ILoginActivity
{
	private LoginPresenter presenter;
	private EditText edPhone,edCap;
	private Button btnGetCap,btnLogin;
	private Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		ThemeUtil.getInstance().setTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_aty);
		toolbar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		edPhone=(EditText) findViewById(R.id.login_ed_phone);
		edCap=(EditText) findViewById(R.id.login_ed_cap);
		btnGetCap=(Button) findViewById(R.id.login_btn_get_cap);
		btnLogin=(Button) findViewById(R.id.login_btn_login);
		presenter=new LoginPresenter(this);
		presenter.onCreat();
		btnGetCap.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					presenter.getCap();
				}
			});
		btnLogin.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					presenter.login();
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

	@Override
	protected void onDestroy()
	{
		presenter.onDestroy();
		super.onDestroy();
	}
	
	@Override
	public void showToast(String msg)
	{
		Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
	}

	@Override
	public String getPhone()
	{
		return edPhone.getText().toString();
	}

	@Override
	public String getCap()
	{
		return edCap.getText().toString();
	}

	@Override
	public void setPhone(String phone)
	{
		edPhone.setText(phone);
	}
	
}
