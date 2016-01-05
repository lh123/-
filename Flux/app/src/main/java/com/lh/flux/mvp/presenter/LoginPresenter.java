package com.lh.flux.mvp.presenter;

import android.os.Handler;

import com.lh.flux.domain.BusProvide;
import com.lh.flux.domain.FluxUserManager;
import com.lh.flux.domain.LoginUsecase;
import com.lh.flux.domain.event.CapEvent;
import com.lh.flux.domain.event.LoginEvent;
import com.lh.flux.model.entity.User;
import com.lh.flux.mvp.view.ILoginActivity;
import com.squareup.otto.Subscribe;

public class LoginPresenter
{
    private LoginUsecase loginUsecase;
    private ILoginActivity loginAty;
    private User u;

    public LoginPresenter(ILoginActivity loginAty)
    {
        this.loginAty = loginAty;
        u = FluxUserManager.getInstance().getUser();
    }

    public void onCreat()
    {
        BusProvide.getBus().register(this);
        Handler mHandler = new Handler();
        loginUsecase = new LoginUsecase(mHandler);
        loginAty.setPhone(u.getPhone());
    }

    public void getCap()
    {
        u.setPhone(loginAty.getPhone());
        loginUsecase.getCap(u);
    }

    public void login()
    {
        loginUsecase.loginWithCap(u, loginAty.getCap());
    }

    @Subscribe
    public void onCapEventReceive(CapEvent event)
    {
        loginAty.showToast(event.getData().getMsg());
    }

    @Subscribe
    public void onLoginEventReceive(LoginEvent event)
    {
        if (event.isSuccess())
        {
            if (event.getData().isSuccess())
            {
                u.setToken(event.getData().getToken());
                u.setSessionID(event.getData().getSessionID());
                loginAty.finish();
            } else
            {
                loginAty.showToast(event.getData().getMsg());
            }
        } else
        {
            loginAty.showToast("未知错误");
        }
    }

    public void onDestroy()
    {
        BusProvide.getBus().unregister(this);
        loginUsecase.onDestroy();
    }
}
