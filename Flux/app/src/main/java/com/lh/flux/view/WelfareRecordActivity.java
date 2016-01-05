package com.lh.flux.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.lh.flux.R;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.model.entity.WelfareRecordEntity;
import com.lh.flux.mvp.presenter.WelfareRecordPresenter;
import com.lh.flux.mvp.view.IWelfareRecordActivity;
import com.lh.flux.view.adapter.WelfareRecyclerAdapter;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class WelfareRecordActivity extends AppCompatActivity implements IWelfareRecordActivity, SwipeRefreshLayout.OnRefreshListener
{
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WelfareRecyclerAdapter mAdapter;

    private WelfareRecordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        ThemeUtil.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welfare_record_aty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.welfare_record_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.welfare_record_refresh);
        setSupportActionBar(toolbar);
        mAdapter = new WelfareRecyclerAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mPresenter = new WelfareRecordPresenter(this);
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
        int preSize = mAdapter.getItemCount();
        mAdapter.setData(data);
        if (preSize < data.size())
        {
            mAdapter.notifyItemRangeInserted(preSize, data.size() - preSize);
        }
		else if (preSize > data.size())
        {
            mAdapter.notifyItemRangeRemoved(data.size(), preSize - data.size());
        }
		else
        {
            mAdapter.notifyItemRangeChanged(0, data.size());
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
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
