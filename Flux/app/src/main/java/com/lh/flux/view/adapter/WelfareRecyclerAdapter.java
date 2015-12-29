package com.lh.flux.view.adapter;

import android.graphics.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.lh.flux.*;
import com.lh.flux.model.entity.*;
import java.util.*;

public class WelfareRecyclerAdapter extends RecyclerView.Adapter
{
	private ArrayList<WelfareRecordEntity.Data> data;

	public void setData(ArrayList<WelfareRecordEntity.Data> data)
	{
		this.data = data;
	}

	public ArrayList<WelfareRecordEntity.Data> getData()
	{
		return data;
	}
	
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int p2)
	{
		View view=LayoutInflater.from(p1.getContext()).inflate(R.layout.welfare_record_item,p1,false);
		return new Holder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder p1, int p2)
	{
		Holder holder=(WelfareRecyclerAdapter.Holder) p1;
		holder.tvWelfareDes.setText(data.get(p2).getPrize().getDescription());
		holder.tvWelfareTime.setText(data.get(p2).getAddtime());
		holder.tvWelfareStatus.setText(data.get(p2).getStatus()==0?"未充值":"已充值");
		holder.tvWelfareStatus.setTextColor(data.get(p2).getStatus()==0?Color.RED:Color.GREEN);
	}

	@Override
	public int getItemCount()
	{
		return data==null?0:data.size();
	}
	
	public static class Holder extends RecyclerView.ViewHolder
	{
		private TextView tvWelfareDes;
		private TextView tvWelfareTime;
		private TextView tvWelfareStatus;
		
		public Holder(View view)
		{
			super(view);
			tvWelfareDes=(TextView) itemView.findViewById(R.id.tv_welfare_des);
			tvWelfareTime=(TextView) itemView.findViewById(R.id.tv_welfare_time);
			tvWelfareStatus=(TextView) itemView.findViewById(R.id.tv_welfare_status);
		}
	}
	
}
