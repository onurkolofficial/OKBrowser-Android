package com.onurkol.app.browser.libs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityActionAnimator {
    public static void startActivity(Context context, Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void finish(Context context){
        ((Activity)context).finish();
        ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void startAndFinishActivity(Context context, Intent startIntent){
        startActivity(context, startIntent);
        finish(context);
    }
    public static void finishAndStartActivity(Context context, Intent startIntent){
        finish(context);
        startActivity(context, startIntent);
    }
}
