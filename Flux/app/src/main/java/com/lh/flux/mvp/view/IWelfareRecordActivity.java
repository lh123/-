package com.lh.flux.mvp.view;
import java.util.*;
import com.lh.flux.model.entity.*;

public interface IWelfareRecordActivity
{
	public void setData(ArrayList<WelfareRecordEntity.Data> data)
	public void setRefreshStatus(boolean status)
	public void showToast(String msg)
}
