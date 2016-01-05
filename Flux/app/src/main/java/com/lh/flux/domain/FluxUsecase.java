package com.lh.flux.domain;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.lh.flux.crash.LogUtil;
import com.lh.flux.domain.event.FluxEvent;
import com.lh.flux.model.api.FluxApi;
import com.lh.flux.model.api.FluxApiImpl;
import com.lh.flux.model.entity.FluxEntity;
import com.lh.flux.model.entity.User;

public class FluxUsecase
{
    private FluxApi mFluxApi;
    private Handler mHandler;

    public FluxUsecase(Handler handler)
    {
        mHandler = handler;
        mFluxApi = new FluxApiImpl();
        BusProvide.getBus().register(this);
    }

    public void getFluxInfo(final User u)
    {
        ThreadManager.getInstance().startThread(new Runnable()
        {

            @Override
            public void run()
            {
                String msg = mFluxApi.getFluxInfo(u);
                LogUtil.getInstance().logE("FluxUsecase", "获取流量");
                FluxEntity data = JSON.parseObject(msg, FluxEntity.class);
                final FluxEvent e = new FluxEvent(data);
                sendToPresenter(e);
            }
        });
    }

    private void sendToPresenter(final Object o)
    {
        if (Looper.myLooper() != Looper.getMainLooper())
        {
            mHandler.post(new Runnable()
            {

                @Override
                public void run()
                {
                    BusProvide.getBus().post(o);
                }
            });
        } else
        {
            BusProvide.getBus().post(o);
        }
    }

    public void onDestroy()
    {
        BusProvide.getBus().unregister(this);
    }
}
