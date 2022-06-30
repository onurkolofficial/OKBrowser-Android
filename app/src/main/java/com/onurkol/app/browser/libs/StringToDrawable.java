package com.onurkol.app.browser.libs;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

public class StringToDrawable {
    public static Drawable convertDrawable(Context context, String DrawableName) {
        return ContextCompat.getDrawable(context,context.getResources().getIdentifier(DrawableName,"drawable", context.getPackageName()));
    }
}
