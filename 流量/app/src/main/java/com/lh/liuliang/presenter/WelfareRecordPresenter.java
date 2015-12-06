package com.lh.liuliang.presenter;
import com.lh.liuliang.model.*;
import com.lh.liuliang.ui.*;
import android.os.*;
import com.lh.liuliang.user.*;
import com.lh.liuliang.preference.*;
import java.util.*;

public class WelfareRecordPresenter
{
	private WelfareRecordModel mModel;
	private IWelfareActivity mAty;
	private Handler mHandler;
	
	private UserInfo mUser;

	public WelfareRecordPresenter(IWelfareActivity mAty)
	{
		this.mAty = mAty;
		mModel=new WelfareRecordModel();
		mHandler=new Handler();
		mUser=UserInfo.getUserInfo();
	}

	public void getWelfareRecord()
	{
		if(mUser.getCookie()==null)
		{
			mModel.getWelfareCookie(mUser, new WelfareRecordModel.MessCallBack(){

					@Override
					public void info(String mess)
					{
						mUser.setCookie(mess);
						DataPre.getInstance().saveCookie(mess);
						mHandler.post(new Runnable(){

								@Override
								public void run()
								{
									getWelfareRecordList();
								}
							});
					}
				});
		}
		else
		{
			getWelfareRecordList();
		}
	}
	
	private void getWelfareRecordList()
	{
		mModel.getWelfareRecord(mUser, new WelfareRecordModel.WelfareRecordCallBack(){

				@Override
				public void welfareRecord(final ArrayList<WelfareInfo> list)
				{
					mHandler.post(new Runnable(){

							@Override
							public void run()
							{
								mAty.setListData(list);
							}
						});
				}
			});
	}
	
	public void destory()
	{
		mHandler.removeCallbacksAndMessages(null);
	}
}
