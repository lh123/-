package com.lh.flux.domain.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class PermissionUtil
{
    private static final PermissionUtil mPermissionUtil=new PermissionUtil();

    private ArrayList<String> permissionList;

    private PermissionUtil()
    {
        permissionList=new ArrayList<>();
        addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        addPermission(Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * @return PermissionUtil instance
     */
    public static PermissionUtil getInstance()
    {
        return mPermissionUtil;
    }


    public void addPermission(String permission)
    {
        if(!permissionList.contains(permission))
        {
            permissionList.add(permission);
        }
    }

    public void removePermission(String permission)
    {
        permissionList.remove(permission);
    }

    /**
     * @param context current context
     * @return permissionStatus
     */
    public boolean checkAllPermission(Context context)
    {
        boolean status=true;
        for(int i=0;i<permissionList.size();i++)
        {
            if(ContextCompat.checkSelfPermission(context,permissionList.get(i))== PackageManager.PERMISSION_DENIED)
            {
                status=false;
            }
        }
        return status;
    }

    /**
     * @param activity current
     */
    public void requestAllPermission(Activity activity)
    {
        SharedPreferences preferences = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        if(preferences.contains("imei")&&preferences.contains("imsi"))
        {
            permissionList.remove(Manifest.permission.READ_PHONE_STATE);
        }
        for(int i=0;i<permissionList.size();i++)
        {
            if(ContextCompat.checkSelfPermission(activity,permissionList.get(i))== PackageManager.PERMISSION_GRANTED)
            {
                permissionList.remove(i);
            }
        }
        if(permissionList.size()>0)
        {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]),0);
        }
    }

    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        boolean canRun=true;
        if(requestCode==0)
        {
            for(int i=0;i<grantResults.length;i++)
            {
                if(grantResults[i]==PackageManager.PERMISSION_DENIED)
                {
                    canRun=false;
                }
            }
        }
        return canRun;
    }
}
