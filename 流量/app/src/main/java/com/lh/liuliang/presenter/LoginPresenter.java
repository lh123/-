package com.lh.liuliang.presenter;
import com.lh.liuliang.ui.*;
import com.lh.liuliang.model.*;
import org.json.*;
import android.os.*;
import com.lh.liuliang.user.*;
import com.lh.liuliang.preference.*;

public class LoginPresenter
{
	private ILoginActivity mLogin;
	private LoginModel mModel;
	private Handler mHandler;

	public LoginPresenter(ILoginActivity mMain)
	{
		this.mLogin = mMain;
		mModel = new LoginModel();
		mHandler = new Handler();
	}

	public void getSaveInfo()
	{
		mLogin.setPhoneNum(DataPre.getInstance().getPhoneNum());
	}
	
	public void getCap()
	{
		mModel.getCap(mLogin.getPhoneNum(), new LoginModel.MessCallBack(){

				@Override
				public void info(String info)
				{
					if(info==null)
					{
						mLogin.showToast("未知错误");
						return;
					}
					try
					{
						final JSONObject obj=new JSONObject(info);
						mHandler.post(new Runnable(){

								@Override
								public void run()
								{
									mLogin.showToast(obj.optString("msg"));
								}
							});

					}
					catch (JSONException e)
					{}
				}
			});
	}
	
	public void login()
	{
		mModel.loginWithCap(mLogin.getPhoneNum(), mLogin.getCapNum(), new LoginModel.MessCallBack(){

				@Override
				public void info(String info)
				{
					if(info==null)
					{
						mHandler.post(new Runnable(){

								@Override
								public void run()
								{
									mLogin.showToast("未知错误");
								}
							});
						return;
					}
					try
					{
						final JSONObject obj=new JSONObject(info);
						if("000".equals(obj.optString("status")))
						{
							mHandler.post(new Runnable(){

									@Override
									public void run()
									{
										mLogin.showToast("登录成功");
										mLogin.setResult(obj.optString("token",null),obj.optString("sessionID",null));
									}
								});
						}
						else
						{
							mHandler.post(new Runnable(){

									@Override
									public void run()
									{
										mLogin.showToast(obj.optString("msg",""));
									}
								});
						}
					}
					catch (JSONException e)
					{}
				}
			});
	}
	
	public void destroy()
	{
		mHandler.removeCallbacksAndMessages(null);
	}
}
