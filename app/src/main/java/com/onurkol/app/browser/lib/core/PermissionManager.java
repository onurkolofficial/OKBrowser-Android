package com.onurkol.app.browser.lib.core;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.onurkol.app.browser.lib.ContextManager;

public class PermissionManager {
    private static PermissionManager instance=null;

    // Classes
    ContextManager contextManager=ContextManager.getManager();

    // Constructor
    private PermissionManager(){}

    public static synchronized PermissionManager getInstance(){
        if(instance==null)
            instance=new PermissionManager();
        return instance;
    }

    // Set Permissions
    public void setInternetPermission(){
        ActivityCompat.requestPermissions(contextManager.getContextActivity(),
                new String[]{Manifest.permission.INTERNET}, 1);
    }
    public void setStoragePermission(){
        ActivityCompat.requestPermissions(contextManager.getContextActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    // Get Permissions
    public boolean getInternetPermission(){
        int getPermission=contextManager.getContextActivity().checkSelfPermission(Manifest.permission.INTERNET);
        return getPermission==PackageManager.PERMISSION_GRANTED;
    }
    public boolean getStoragePermission(){
        int getPermissionRS=contextManager.getContextActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int getPermissionWS=contextManager.getContextActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int GRANTED=PackageManager.PERMISSION_GRANTED;
        return (getPermissionRS==GRANTED && getPermissionWS==GRANTED);
    }
}
