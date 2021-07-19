package com.onurkol.app.browser.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.View;

import com.onurkol.app.browser.lib.ContextManager;

public class ScreenManager {
    // Get Classes
    static DisplayMetrics displayMetrics = new DisplayMetrics();

    public static int getScreenHeight(){
        ContextManager.getManager().getContextActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(){
        ContextManager.getManager().getContextActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static Bitmap getScreenshot(View view){
        // Get Screen Width/Height
        int height = getScreenHeight();
        int width = getScreenWidth();
        // Create Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, (height-220), Bitmap.Config.ARGB_8888);
        // Get ScreenShot
        Canvas c = new Canvas(bitmap);
        if(view!=null)
            view.draw(c);
        return bitmap;
    }
}
