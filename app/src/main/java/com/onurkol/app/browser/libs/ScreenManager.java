package com.onurkol.app.browser.libs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;

public class ScreenManager {
    static DisplayMetrics displayMetrics = new DisplayMetrics();

    public static int getScreenHeight(Context context){
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context){
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static Bitmap getScreenshot(Context context, View view){
        // Get Screen Width/Height
        int height = getScreenHeight(context);
        int width = getScreenWidth(context);
        // Create Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, (height-220), Bitmap.Config.ARGB_8888);
        // Get ScreenShot
        Canvas c = new Canvas(bitmap);
        if(view!=null)
            view.draw(c);
        return bitmap;
    }
}
