package com.lh.flux.mvp.presenter;

import android.os.Handler;

import com.lh.flux.domain.BusProvide;
import com.lh.flux.domain.FluxUserManager;
import com.lh.flux.domain.WelfareRecordUsecase;
import com.lh.flux.domain.WelfareUsecase;
import com.lh.flux.domain.event.WelfareCookieEvent;
import com.lh.flux.domain.event.WelfareRecordEvent;
import com.lh.flux.model.entity.WelfareRecordEntity;
import com.lh.flux.mvp.view.IWelfareRecordActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class WelfareRecordPresenter
{
    private IWelfareRecordActivity mWelfareRecordActivity;
    private Handler mHandler;
    private WelfareRecordUsecase mWelfareRecordUsecase;
    private WelfareUsecase mWelfareUsecase;

    private boolean isNeedRefreshWelfareRecord = false;

    public WelfareRecordPresenter(IWelfareRecordActivity mWelfareRecordActivity)
    {
        this.mWelfareRecordActivity = mWelfareRecordActivity;
    }

    public void onCreate()
    {
        BusProvide.getBus().register(this);
        mHandler = new Handler();
        mWelfareRecordUsecase = new WelfareRecordUsecase(mHandler);
        mWelfareUsecase = new WelfareUsecase(mHandler);
    }

    public void startRefreshWelfareRecord()
    {
        mHandler.post(new Runnable()
        {

            @Override
            public void run()
            {
                mWelfareRecordActivity.setRefreshStatus(true);
            }
        });
        if (FluxUserManager.getInstance().getUser().getCookie() == null)
        {
            isNeedRefreshWelfareRecord = true;
            mWelfareUsecase.getWelfareCoookie(FluxUserManager.getInstance().getUser());
        } else
        {
            mWelfareRecordUsecase.getWelfareRecord(FluxUserManager.getInstance().getUser());
        }
    }

    @Subscribe
    public void onWelfareCookieReceive(WelfareCookieEvent event)
    {
        if (event.isSuccess())
        {
            FluxUserManager.getInstance().getUser().setCookie(event.getCookie());
            if (isNeedRefreshWelfareRecord)
            {
                isNeedRefreshWelfareRecord = false;
                startRefreshWelfareRecord();
            }
        }
    }

    @Subscribe
    public void onWelfareRecordEventReceive(WelfareRecordEvent event)
    {
        mWelfareRecordActivity.setRefreshStatus(false);
        if (event.isSuccess() && event.getData().isSuccess())
        {
            mWelfareRecordActivity.setData(event.getData().getData());
        } else
        {
            mWelfareRecordActivity.setData(new ArrayList<WelfareRecordEntity.Data>());
            mWelfareRecordActivity.showToast("获取失败");
        }
    }

    public void onDestroy()
    {
        mHandler.removeCallbacksAndMessages(null);
        BusProvide.getBus().unregister(this);
        mWelfareRecordUsecase.onDestroy();
        mWelfareUsecase.onDestroy();
    }
}
