package com.lh.flux.view;
import android.os.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.widget.*;
import com.lh.flux.*;
import com.lh.flux.model.entity.*;
import com.lh.flux.mvp.presenter.*;
import com.lh.flux.mvp.view.*;
import com.lh.flux.view.adapter.*;
import java.util.*;

import android.support.v7.widget.Toolbar;
import com.umeng.analytics.*;
import com.lh.flux.domain.*;

public class WelfareRecordActivity extends AppCompatActivity implements IWelfareRecordActivity,SwipeRefreshLayout.OnRefreshListener
{
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private WelfareRecyclerAdapter mAdapter;
	private Toolbar toolbar;
	
	private WelfareRecordPresenter mPresenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		ThemeUtil.getInstance().setTheme(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welfare_record_aty);
		toolbar=(Toolbar) findViewById(R.id.toolbar);
		mRecyclerView=(RecyclerView) findViewById(R.id.welfare_record_recycler_view);
		mSwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.welfare_record_refresh);
		setSupportActionBar(toolbar);
		mAdapter=new WelfareRecyclerAdapter();
		LinearLayoutManager lm=new LinearLayoutManager(this);
		lm.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(lm);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(mAdapter);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mPresenter=new WelfareRecordPresenter(this);
		mPresenter.onCreate();
		mPresenter.startRefreshWelfareRecord();
	}
	
	@Override
	public void onRefresh()
	{
		mPresenter.startRefreshWelfareRecord();
	}

	@Override
	public void setData(ArrayList<WelfareRecordEntity.Data> data)
	{
		int preSize=mAdapter.getItemCount();
		mAdapter.setData(data);
		if(preSize<data.size())
		{
			mAdapter.notifyItemRangeInserted(preSize,data.size()-preSize);
		}
		else if(preSize>data.size())
		{
			mAdapter.notifyItemRangeRemoved(data.size(),preSize-data.size());
		}
		else
		{
			mAdapter.notifyItemRangeChanged(0,data.size());
		}
	}

	@Override
	public void setRefreshStatus(boolean status)
	{
		mSwipeRefreshLayout.setRefreshing(status);
	}

	@Override
	public void showToast(String msg)
	{
		Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
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
		mPresenter.onDestroy();
		super.onDestroy();
	}
}
