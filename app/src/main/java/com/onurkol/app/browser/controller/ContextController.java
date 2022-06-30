package com.onurkol.app.browser.controller;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.lang.ref.WeakReference;

public class ContextController {
    private static WeakReference<ContextController> instance=null;
    private final Context mContext;
    private static WeakReference<Context> baseContextStatic;
    private static boolean base;

    private ContextController(Context context){
        if(base)
            baseContextStatic=new WeakReference<>(context);
        mContext=context;
    }

    public static synchronized void setContext(Context context){
        base=false;
        instance=new WeakReference<>(new ContextController(context));
    }

    public static synchronized void setBaseContext(Context context){
        base=true;
        instance=new WeakReference<>(new ContextController(context));
    }

    public static synchronized ContextController getController(){
        return instance.get();
    }

    public Context getContext(){
        return mContext;
    }
    public Activity getContextActivity(){
        return ((Activity)mContext);
    }

    public Context getBaseContext(){
        return (baseContextStatic!=null ? baseContextStatic.get() : null);
    }
    public Activity getBaseContextActivity(){
        return (baseContextStatic!=null ? (Activity)baseContextStatic.get() : null);
    }
    public FragmentManager getBaseFragmentManager(){ return (baseContextStatic!=null ? ((FragmentActivity)baseContextStatic.get()).getSupportFragmentManager() : null); }
}
