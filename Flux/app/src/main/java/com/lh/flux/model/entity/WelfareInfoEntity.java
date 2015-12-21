package com.lh.flux.model.entity;
import com.alibaba.fastjson.annotation.*;
import java.util.*;

public class WelfareInfoEntity
{
	private String returnCode;
	private String msg;
	private Data data;

	public void setReturnCode(String returnCode)
	{
		this.returnCode = returnCode;
	}

	public String getReturnCode()
	{
		return returnCode;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setData(Data data)
	{
		this.data = data;
	}

	public Data getData()
	{
		return data;
	}
	
	public boolean isSuccess()
	{
		return "000".equals(returnCode);
	}
	
	public boolean isGrabing()
	{
		return "grabing".equals(data.getFlag());
	}
	
	public static class Data
	{
		private String flag;
		private int remain;
		private String startTime;
		private Rule rule;


		public void setFlag(String flag)
		{
			this.flag = flag;
		}

		public String getFlag()
		{
			return flag;
		}

		public void setRemain(int remain)
		{
			this.remain = remain;
		}

		public int getRemain()
		{
			return remain;
		}

		public void setStartTime(String startTime)
		{
			this.startTime = startTime;
		}

		public String getStartTime()
		{
			return startTime;
		}

		public void setRule(Rule rule)
		{
			this.rule = rule;
		}

		public Rule getRule()
		{
			return rule;
		}
	}
	
	public static class Rule
	{
		@JSONField(name="_id")
		private String id;
		private int period;
		private int sustain;
		private int totalNum;
		private int status;
		@JSONField(name="__v")
		private int v;
		private ArrayList<Prize> prize;

		public void setId(String id)
		{
			this.id = id;
		}

		public String getId()
		{
			return id;
		}

		public void setPeriod(int period)
		{
			this.period = period;
		}

		public int getPeriod()
		{
			return period;
		}

		public void setSustain(int sustain)
		{
			this.sustain = sustain;
		}

		public int getSustain()
		{
			return sustain;
		}

		public void setTotalNum(int totalNum)
		{
			this.totalNum = totalNum;
		}

		public int getTotalNum()
		{
			return totalNum;
		}

		public void setStatus(int status)
		{
			this.status = status;
		}

		public int getStatus()
		{
			return status;
		}

		public void setV(int v)
		{
			this.v = v;
		}

		public int getV()
		{
			return v;
		}

		public void setPrize(ArrayList<Prize> prize)
		{
			this.prize = prize;
		}

		public ArrayList<Prize> getPrize()
		{
			return prize;
		}
	}
	
	public static class Prize
	{
		private int number;
		@JSONField(name="_id")
		private String id;
		@JSONField(name="id")
		private WelfareEnvelopEntity data;

		public void setNumber(int number)
		{
			this.number = number;
		}

		public int getNumber()
		{
			return number;
		}

		public void setId(String id)
		{
			this.id = id;
		}

		public String getId()
		{
			return id;
		}

		public void setData(WelfareEnvelopEntity data)
		{
			this.data = data;
		}

		public WelfareEnvelopEntity getData()
		{
			return data;
		}
	}
}
