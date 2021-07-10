package com.onurkol.app.browser.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.onurkol.app.browser.lib.ContextManager;

public class StringToDrawable {
    public static Drawable convertDrawable(String DrawableName) {
        // Get Context
        Context context=ContextManager.getManager().getContext();
        // Return Drawable
        return ContextCompat.getDrawable(context,context.getResources().getIdentifier(DrawableName,"drawable", context.getPackageName()));
    }

    public static Drawable convertDrawable(Context context, String DrawableName) {
        // Return Drawable
        return ContextCompat.getDrawable(context,context.getResources().getIdentifier(DrawableName,"drawable", context.getPackageName()));
    }
}
