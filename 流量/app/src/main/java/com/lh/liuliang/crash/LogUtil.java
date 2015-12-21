package com.lh.liuliang.crash;
import android.content.*;
import java.io.*;
import java.text.*;
import java.util.*;
import android.util.*;

public class LogUtil
{
	private Context mContext;
	private File file;
	private SimpleDateFormat formate;
	private FileWriter write;
	
	private static LogUtil logUtil;
	
	public static LogUtil getInstance()
	{
		if(logUtil==null)
		{
			synchronized(LogUtil.class)
			{
				if(logUtil==null)
				{
					logUtil=new LogUtil();
				}
			}
		}
		return logUtil;
	}
	
	public void init(Context context)
	{
		mContext=context.getApplicationContext();
		File dir=mContext.getExternalFilesDir("log");
		dir=new File(dir.getAbsolutePath());
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!dir.exists()||!dir.canRead())
		{
			dir.mkdirs();
		}
		file=new File(dir,"log.txt");
		boolean isAppend=true;
		if(file.length()>1*1024*1024)
		{
			//System.out.println("clear");
			isAppend=false;
		}
		try
		{
			write=new FileWriter(file,isAppend);
			write.write("--------------\n");
			write.flush();
		}
		catch (IOException e)
		{}
	}
	public void logE(String tag,Exception ex)
	{
		ex.printStackTrace();
		//String es;
		StringWriter sw=new StringWriter();
		PrintWriter pw=new PrintWriter(sw);
		ex.printStackTrace(pw);
		Date date=new Date(System.currentTimeMillis());
		String time=formate.format(date);
		String temp=time+" "+tag+" :"+sw.toString();
		try
		{
			write.write(temp + "\n");
			write.flush();
		}
		catch (IOException e)
		{}
	}
	public void logE(String tag,String msg)
	{
		Log.i(tag,msg);
		Date date=new Date(System.currentTimeMillis());
		String time=formate.format(date);
		String temp=time+" "+tag+" :"+msg;
		try
		{
			write.write(temp + "\n");
			write.flush();
		}
		catch (IOException e)
		{}
	}
}
