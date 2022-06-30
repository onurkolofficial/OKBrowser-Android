package com.onurkol.app.browser.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.onurkol.app.browser.BuildConfig;

import java.lang.ref.WeakReference;

public class PermissionController {
    private static WeakReference<PermissionController> instance=null;

    private PermissionController(){}

    public static synchronized PermissionController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new PermissionController());
        return instance.get();
    }

    // Set Permissions
    public void setInternetPermission(Context context){
        if(!getInternetPermission(context))
            ActivityCompat.requestPermissions(((Activity)context),
                    new String[]{Manifest.permission.INTERNET}, 1);
    }
    public void setStoragePermission(Context context){
        if(!getStoragePermission(context))
            ActivityCompat.requestPermissions(((Activity)context),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
    // Get Permissions
    public boolean getInternetPermission(Context context){
        int getPermission=context.checkSelfPermission(Manifest.permission.INTERNET);
        return getPermission==PackageManager.PERMISSION_GRANTED;
    }
    public boolean getStoragePermission(Context context){
        int getPermissionRS = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int getPermissionWS = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int GRANTED = PackageManager.PERMISSION_GRANTED;
        return (getPermissionRS == GRANTED && getPermissionWS == GRANTED);
    }
}
