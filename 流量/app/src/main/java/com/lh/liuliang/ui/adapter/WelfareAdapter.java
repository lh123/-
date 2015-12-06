package com.lh.liuliang.ui.adapter;
import android.widget.*;
import android.view.*;
import java.util.*;
import com.lh.liuliang.user.*;
import com.lh.liuliang.*;
import android.graphics.*;

public class WelfareAdapter extends BaseAdapter
{
	private ArrayList<WelfareInfo> list;

	public void setList(ArrayList<WelfareInfo> list)
	{
		this.list = list;
		notifyDataSetChanged();
	}

	public ArrayList<WelfareInfo> getList()
	{
		return list;
	}
	
	@Override
	public int getCount()
	{
		if(list!=null)
		{
			return list.size();
		}
		return 0;
	}

	@Override
	public WelfareInfo getItem(int p1)
	{
		return list.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group)
	{
		if(convertView==null)
		{
			convertView=LayoutInflater.from(group.getContext()).inflate(R.layout.welfare_record_item,group,false);
		}
		TextView tvWelfareDes=(TextView) convertView.findViewById(R.id.tv_welfare_des);
		TextView tvWelfareTime=(TextView) convertView.findViewById(R.id.tv_welfare_time);
		TextView tvWelfareStatus=(TextView) convertView.findViewById(R.id.tv_welfare_status);
		tvWelfareDes.setText(getItem(position).getDes());
		tvWelfareTime.setText(getItem(position).getTime());
		//tvWelfareStatus.setText(getItem(position).getStatus()==0?"未充值":"已充值");
		if(getItem(position).getStatus()==2)
		{
			tvWelfareStatus.setTextColor(Color.GREEN);
			tvWelfareStatus.setText("已充值");
		}
		else
		{
			tvWelfareStatus.setTextColor(Color.RED);
			tvWelfareStatus.setText("未充值");
		}
		return convertView;
	}
}
