package com.lh.flux.crash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lh.flux.R;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.view.FluxActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashActivity extends AppCompatActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ThemeUtil.getInstance().setDialogTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crash_aty);
        Throwable e = (Throwable) getIntent().getSerializableExtra("error");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        setFinishOnTouchOutside(false);
        TextView tvCrash = (TextView) findViewById(R.id.tv_crash);
        Button btnReboot = (Button) findViewById(R.id.btn_reboot);
        Button btnClose = (Button) findViewById(R.id.btn_close);
        tvCrash.setText(sw.toString());
        btnClose.setOnClickListener(this);
        btnReboot.setOnClickListener(this);
    }

    @Override
    public void onClick(View p1)
    {
        switch (p1.getId())
        {
            case R.id.btn_close:
                System.exit(0);
                break;
            case R.id.btn_reboot:
                Intent i = new Intent();
                i.setClass(getApplicationContext(), FluxActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
        }
    }
}
