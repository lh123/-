package com.lh.liuliang.ui;

import android.app.*;
import android.os.*;
import android.widget.*;
import com.lh.liuliang.*;
import com.lh.liuliang.presenter.*;
import com.lh.liuliang.ui.adapter.*;
import com.lh.liuliang.user.*;
import com.umeng.analytics.*;
import java.util.*;

public class WelfareRecordActivity extends Activity implements IWelfareActivity
{
	private ListView listView;
	private WelfareAdapter adapter;
	private WelfareRecordPresenter mPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welfare_record);
		listView=(ListView) findViewById(R.id.welfare_record_list);
		mPresenter=new WelfareRecordPresenter(this);
		adapter=new WelfareAdapter();
		listView.setAdapter(adapter);
		mPresenter.getWelfareRecord();
	}

	@Override
	public void setListData(ArrayList<WelfareInfo> data)
	{
		adapter.setList(data);
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
		mPresenter.destory();
		super.onDestroy();
	}
}
