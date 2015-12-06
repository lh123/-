package com.lh.liuliang.presenter;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.lh.liuliang.model.*;
import com.lh.liuliang.preference.*;
import com.lh.liuliang.service.*;
import com.lh.liuliang.ui.*;
import com.lh.liuliang.user.*;
import java.text.*;
import java.util.*;
import org.json.*;

public class MainPresenter implements ServiceConnection
{
	private IMainActivity mMain;
	private UserInfo mUser;
	private MainModel mModel;
	private Handler mHandler;
	private WelfareService mService;

	public MainPresenter(IMainActivity mMain)
	{
		this.mMain = mMain;
		mUser = UserInfo.getUserInfo();
		mModel = new MainModel();
		mHandler = new Handler();
	}

	public UserInfo getUser()
	{
		return mUser;
	}

	public void checkStatus()
	{
		if (DataPre.getInstance().getSeasonsID() != null)
		{
			loginWithToken();
		}
		if (DataPre.getInstance().getFlux().length >= 2)
		{
			mMain.setFluxInfo(mUser.getTotalFlux(), mUser.getAvailableFlux());
		}
		if(DataPre.getInstance().getAutoGrabWelfareStatus()!=null)
		{
			mMain.setWelfareServiceStatus(DataPre.getInstance().getAutoGrabWelfareStatus());
		}
		if(WelfareService.isAlive())
		{
			startAndBindWelfareService();
		}
	}

	public void getFlux()
	{
		mMain.setPbRefreshFlux(View.VISIBLE);
		if (mUser.getSessionID() == null)
		{
			mMain.showToast("你还没有登录");
			mMain.setPbRefreshFlux(View.INVISIBLE);
			return;
		}
		mModel.getFlux(mUser, new MainModel.MessCallBack(){

				@Override
				public void info(String mess)
				{
					if (mess == null)
					{
						mHandler.post(new Runnable(){

								@Override
								public void run()
								{
									mMain.showToast("未知错误");
									mMain.setPbRefreshFlux(View.INVISIBLE);
								}
							});
						return;
					}
					try
					{
						JSONObject obj=new JSONObject(mess);
						if ("000".equals(obj.optString("returnCode")))
						{
							JSONObject temp=obj.getJSONObject("data").getJSONObject("sum");
							final int totalFlux=temp.optInt("total", 0);
							final int availableFlux=temp.optInt("available", 0);
							mUser.setTotalFlux(totalFlux);
							mUser.setAvailableFlux(availableFlux);
							DataPre.getInstance().saveFlux(availableFlux, totalFlux);
							mHandler.post(new Runnable(){

									@Override
									public void run()
									{
										mMain.setFluxInfo(totalFlux, availableFlux);
										mMain.showToast("刷新成功");
										mMain.setPbRefreshFlux(View.INVISIBLE);
									}
								});
						}
						else
						{
							mHandler.post(new Runnable(){

									@Override
									public void run()
									{
										mMain.showToast("刷新失败，请尝试重新登录");
										mMain.setPbRefreshFlux(View.INVISIBLE);
									}
								});
						}
					}
					catch (JSONException e)
					{}
				}
			});
	}


	public void getWelfareStatus()
	{
		mMain.setPbRefreshWelfare(View.VISIBLE);
		if (mUser.getSessionID() == null)
		{
			mMain.showToast("你还没有登录");
			mMain.setPbRefreshWelfare(View.INVISIBLE);
			return;
		}
		if (mUser.getCookie() == null)
		{
			mModel.getWelfareCookie(mUser, new MainModel.MessCallBack(){

					@Override
					public void info(final String mess)
					{
						if (mess == null)
						{
							mHandler.post(new Runnable(){

									@Override
									public void run()
									{
										mMain.showToast("未知错误");
										mMain.setPbRefreshWelfare(View.INVISIBLE);
									}
								});
							return;
						}
						DataPre.getInstance().saveCookie(mess);
						mUser.setCookie(mess);
						getWelfareInfo();
					}
				});
		}
		else
		{
			getWelfareInfo();
		}
	}

	private void getWelfareInfo()
	{
		mModel.getWelfareStatus(mUser, new MainModel.MessCallBack(){

				@Override
				public void info(String mess)
				{
					if (mess == null)
					{
						mHandler.post(new Runnable(){

								@Override
								public void run()
								{
									mMain.showToast("未知错误");
									mMain.setPbRefreshWelfare(View.INVISIBLE);
								}
							});
						return;
					}
					try
					{
						JSONObject obj=new JSONObject(mess);
						if ("000".equals(obj.optString("returnCode")))
						{
							final String msg=obj.optString("msg", null);
							JSONObject temp=obj.getJSONObject("data");
							JSONObject rule=temp.getJSONObject("rule");
							JSONObject prize=rule.getJSONArray("prize").getJSONObject(0).getJSONObject("id");
							final int remain=temp.optInt("remain", 0);
							final String time=temp.optString("startTime", null);
							final int total=rule.optInt("totalNum", 0);
							final String name=prize.optString("name","未知");
							mHandler.post(new Runnable(){

									@Override
									public void run()
									{
										mMain.setWelfareStatus(msg, time, remain, total,name);
										mMain.showToast("刷新成功");
										mMain.setPbRefreshWelfare(View.INVISIBLE);
									}
								});
						}
						else
						{
							mUser.setCookie(null);
							DataPre.getInstance().saveCookie(null);
							mHandler.post(new Runnable(){

									@Override
									public void run()
									{
										mMain.showToast("刷新失败，请重试");
										mMain.setPbRefreshWelfare(View.INVISIBLE);
									}
								});
						}
					}
					catch (JSONException e)
					{}
				}
			});
	}
	
	public void grabWelfareEnvelope()
	{
		if(WelfareService.isAlive()==false||mService==null)
		{
			startAndBindWelfareService();
		}
		else if(mService.isGrabWelfare()==false)
		{
			mService.grabWelfare();
		}
		else
		{
			mMain.showToast("已经再抢红包啦");
		}
	}

	private void startAndBindWelfareService()
	{
		Intent i=new Intent();
		i.setClass(mMain.getApplicationContext(),WelfareService.class);
		i.putExtra("auto",false);
		i.putExtra("dur",180000l);
		if(WelfareService.isAlive()==false)
		{
			mMain.getApplicationContext().startService(i);
		}
		mMain.getApplicationContext().bindService(i,this,Context.BIND_AUTO_CREATE);
	}
	
	public void unbindAndStopService()
	{
		if(WelfareService.isAlive()==true)
		{
			Intent i=new Intent();
			i.setClass(mMain.getApplicationContext(),WelfareService.class);
			if(mService!=null)
			{
				mMain.getApplicationContext().unbindService(this);
			}
			mMain.getApplicationContext().stopService(i);
		}
	}

	public void startWelfareRecordAty()
	{
		Intent i=new Intent();
		i.setClass(mMain.getApplicationContext(), WelfareRecordActivity.class);
		if (mUser.getSessionID() == null)
		{
			mMain.showToast("请先登录");
			return;
		}
		mMain.getContext().startActivity(i);
	}
	
	public void setAutoGrabWelfare()
	{
		mMain.showToast("该功能的准确度因系统而异");
		final Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(System.currentTimeMillis());
		TimePickerDialog dialog=new TimePickerDialog(mMain.getContext(), new TimePickerDialog.OnTimeSetListener(){

				@Override
				public void onTimeSet(TimePicker p1, int p2, int p3)
				{
					AlarmManager am=(AlarmManager) mMain.getApplicationContext().getSystemService(mMain.getApplicationContext().ALARM_SERVICE);
					ca.set(Calendar.HOUR_OF_DAY,p2);
					ca.set(Calendar.MINUTE,p3);
					ca.set(Calendar.SECOND,0);
					ca.set(Calendar.MILLISECOND,0);
					if(ca.getTimeInMillis()<=System.currentTimeMillis())
					{
						mMain.showToast("时间不能小于当前时间");
						return;
					}
					Intent si=new Intent();
					si.putExtra("auto",true);
					si.putExtra("dur",(long)3*60*1000);
					si.putExtra("act",ca.getTimeInMillis());
					si.setClass(mMain.getApplicationContext(),WelfareService.class);
					PendingIntent pi=PendingIntent.getService(mMain.getApplicationContext(),0,si,PendingIntent.FLAG_UPDATE_CURRENT);
					am.cancel(pi);
					am.set(AlarmManager.RTC_WAKEUP,ca.getTimeInMillis()-3*60*1000,pi);
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					mMain.setWelfareServiceStatus("自动抢红包:"+df.format(ca.getTime()));
					DataPre.getInstance().saveAutoGrabWelfareStatus("自动抢红包:"+df.format(ca.getTime()));
				}
			}, ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE), true);
			dialog.show();
	}
	
	public void loginWithToken()
	{
		mMain.setPhoneNum(mUser.getPhone());
		mModel.loginWithToken(mUser, new MainModel.MessCallBack(){

				@Override
				public void info(String mess)
				{
					if(mess==null)
					{
						mHandler.post(new Runnable(){

								@Override
								public void run()
								{
									mMain.showToast("登录失败");
									mMain.setLoginStatus("未登录");
								}
							});
						return;
					}
					else
					{
						try
						{
							final JSONObject obj=new JSONObject(mess);
							if("000".equals(obj.optString("status","-1")))
							{
								String token = obj.optString("token", null);
								String id=obj.optString("sessionID",null);
								DataPre.getInstance().saveToken(token);
								DataPre.getInstance().saveSeasonsID(id);
								UserInfo.getUserInfo().setToken(token);
								UserInfo.getUserInfo().setSessionID(id);
								mHandler.post(new Runnable(){

										@Override
										public void run()
										{
											mMain.showToast("登录成功");
											mMain.setLoginStatus("已登录");
										}
									});
							}
							else
							{
								DataPre.getInstance().saveSeasonsID(null);
								DataPre.getInstance().saveToken(null);
								mUser.setSessionID(null);
								mUser.setToken(null);
								mHandler.post(new Runnable(){

										@Override
										public void run()
										{
											mMain.setLoginStatus(obj.optString("msg",""));
											mMain.startLogin();
										}
									});
							}
						}
						catch (JSONException e)
						{}
					}
				}
			});
	}
	
	public void destory()
	{
		mHandler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void onServiceConnected(ComponentName p1, IBinder p2)
	{
		mService=((WelfareService.WelfareBind)p2).getService();
		mService.setCallBack(new WelfareService.GrabWelfareStatus(){

				@Override
				public void onGrabWelfare(final String msg)
				{
					mHandler.post(new Runnable(){

							@Override
							public void run()
							{
								mMain.setWelfareServiceStatus(msg);
							}
						});
				}
			});
		grabWelfareEnvelope();
	}
	
	@Override
	public void onServiceDisconnected(ComponentName p1)
	{
		mService=null;
	}
	
}
