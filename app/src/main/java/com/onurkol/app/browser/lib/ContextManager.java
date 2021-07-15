package com.onurkol.app.browser.lib;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class ContextManager {
    private static ContextManager instance=null;
    private Context mContext,baseContext;
    private static boolean base=false;

    private ContextManager(Context context){
        if(base)
            baseContext=context;
        mContext=context;
    }

    public static synchronized void Build(Context context){
        instance=new ContextManager(context);
    }

    public static synchronized void BuildBase(Context context){
        base=true;
        instance=new ContextManager(context);
    }

    public static synchronized ContextManager getManager(){
        return instance;
    }

    public Context getContext(){
        return mContext;
    }
    public Activity getContextActivity(){
        return ((Activity)mContext);
    }

    public Context getBaseContext(){
        return baseContext;
    }
    public Activity getBaseContextActivity(){
        return ((Activity)baseContext);
    }
    public FragmentManager getBaseFragmentManager(){ return ((FragmentActivity)baseContext).getSupportFragmentManager(); }
}
