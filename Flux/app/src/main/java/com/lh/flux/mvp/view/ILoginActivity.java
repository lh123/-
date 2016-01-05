package com.lh.flux.mvp.view;

public interface ILoginActivity
{
    void showToast(String msg);

    String getPhone();

    String getCap();

    void setPhone(String phone);

    void finish();
}
