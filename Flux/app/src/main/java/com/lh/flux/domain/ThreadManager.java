package com.lh.flux.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager
{
    private static ThreadManager manager;

    private ExecutorService exec;

    private ThreadManager()
    {
        exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public static ThreadManager getInstance()
    {
        if (manager == null)
        {
            synchronized (ThreadManager.class)
            {
                if (manager == null)
                {
                    manager = new ThreadManager();
                }
            }
        }
        return manager;
    }

    public void startThread(Runnable run)
    {
        exec.execute(run);
    }
}
