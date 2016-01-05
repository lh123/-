package com.lh.flux.mvp.view;

import com.lh.flux.model.entity.WelfareRecordEntity;

import java.util.ArrayList;

public interface IWelfareRecordActivity
{
    void setData(ArrayList<WelfareRecordEntity.Data> data);

    void setRefreshStatus(boolean status);

    void showToast(String msg);
}
