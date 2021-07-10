package com.onurkol.app.browser.lib;

import android.app.Activity;
import android.content.Context;

public class ContextManager {
    private static ContextManager instance=null;
    private Context mContext;

    private ContextManager(Context context){
        mContext=context;
    }

    public static synchronized void Build(Context context){
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
}
