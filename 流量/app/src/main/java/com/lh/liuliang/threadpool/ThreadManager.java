package com.lh.liuliang.threadpool;
import java.util.concurrent.*;

public class ThreadManager
{
	private static ThreadManager threadManager;
	private ExecutorService exec;;
	
	private ThreadManager()
	{
		exec=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	public static ThreadManager getInstance()
	{
		if(threadManager==null)
		{
			synchronized(ThreadManager.class)
			{
				if(threadManager==null)
				{
					threadManager=new ThreadManager();
				}
			}
		}
		return threadManager;
	}
	
	public void startThread(Runnable run)
	{
		exec.execute(run);
		
	}
	
	public void destory()
	{
		exec.shutdown();
	}
}
