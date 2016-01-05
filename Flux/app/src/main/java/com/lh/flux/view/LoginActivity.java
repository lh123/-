package com.lh.flux.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lh.flux.R;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.mvp.presenter.LoginPresenter;
import com.lh.flux.mvp.view.ILoginActivity;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends AppCompatActivity implements ILoginActivity
{
    private LoginPresenter presenter;
    private EditText edPhone;
    private EditText edCap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ThemeUtil.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_aty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edPhone = (EditText) findViewById(R.id.login_ed_phone);
        edCap = (EditText) findViewById(R.id.login_ed_cap);
        Button btnGetCap = (Button) findViewById(R.id.login_btn_get_cap);
        Button btnLogin = (Button) findViewById(R.id.login_btn_login);
        presenter = new LoginPresenter(this);
        presenter.onCreat();
        btnGetCap.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View p1)
            {
                presenter.getCap();
            }
        });
        btnLogin.setOnClickListener(new OnClickListener()
        {

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
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
