package com.onurkol.app.browser.controller.tabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.onurkol.app.browser.R;

public class TabCounterController {
    public static void setMenuTabCounterButton(Context context, int tabCount){
        ImageButton menuTabCountButton=((Activity)context).findViewById(R.id.browserToolbarTabCountButton);
        menuTabCountButton.setImageDrawable(getDrawableHandler(context, tabCount));
    }

    private static Drawable getDrawableHandler(Context context, int Count){
        String drawableName;
        if(Count>9)
            drawableName="ic_tab_num_9_more";
        else
            drawableName="ic_tab_num_"+Count;

        return ContextCompat.getDrawable(context,context.getResources()
                .getIdentifier(drawableName,"drawable", context.getPackageName()));
    }
}
