package com.lh.flux.crash;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogUtil
{
    private SimpleDateFormat formate;
    private FileWriter write;
    private static LogUtil logUtil;

    private boolean isInit = false;

    public static LogUtil getInstance()
    {
        if (logUtil == null)
        {
            synchronized (LogUtil.class)
            {
                if (logUtil == null)
                {
                    logUtil = new LogUtil();
                }
            }
        }
        return logUtil;
    }


    public void init(Context context)
    {
        isInit = true;
        Context mContext = context.getApplicationContext();
        File dir = mContext.getExternalFilesDir("log");
        assert dir != null;
        dir = new File(dir.getAbsolutePath());
        formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        if (!dir.exists() || !dir.canRead())
        {
            boolean isCreat = dir.mkdirs();
            System.out.print(isCreat);
        }
        File file = new File(dir, "log.txt");
        boolean isAppend = true;
        if (file.length() > 50 * 1024)
        {
            //System.out.println("clear");
            isAppend = false;
        }
        try
        {
            write = new FileWriter(file, isAppend);
            write.write("----------------\n");
            write.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

//    public void logE(String tag, Exception ex)
//    {
//        if(isInit)
//        {
//            ex.printStackTrace();
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            ex.printStackTrace(pw);
//            Date date = new Date(System.currentTimeMillis());
//            String time = formate.format(date);
//            String temp = time + " " + tag + " :" + sw.toString();
//            try
//            {
//                write.write(temp + "\n");
//                write.flush();
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }

    public void logE(String tag, String msg)
    {
        if (isInit)
        {
            Log.i(tag, msg == null ? "null" : msg);
            Date date = new Date(System.currentTimeMillis());
            String time = formate.format(date);
            String temp = time + " " + tag + " :" + msg;
            try
            {
                write.write(temp + "\n");
                write.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}