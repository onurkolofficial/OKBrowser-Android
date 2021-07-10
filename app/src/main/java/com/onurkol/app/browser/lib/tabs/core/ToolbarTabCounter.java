package com.onurkol.app.browser.lib.tabs.core;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.tabs.TabBuilder;

public class ToolbarTabCounter {
    // Classes
    TabBuilder tabBuilder;
    Context context;

    public Drawable getTabCountDrawable(){
        // Get Classes
        tabBuilder=TabBuilder.Build();
        context=ContextManager.getManager().getContext();

        // Get Tab Count
        return tabCountHandler(tabBuilder.getSavedTabList().size());
    }

    public Drawable getIncognitoTabCountDrawable(){
        // Get Classes
        tabBuilder=TabBuilder.Build();
        context=ContextManager.getManager().getContext();

        // Get Tab Count
        return tabCountHandler(tabBuilder.getIncognitoTabDataList().size());
    }

    public Drawable tabCountHandler(int Count){
        String drawableName;
        if(Count>9)
            drawableName="ic_tab_num_9_more";
        else
            drawableName="ic_tab_num_"+Count;

        return ContextCompat.getDrawable(context,context.getResources()
                .getIdentifier(drawableName,"drawable", context.getPackageName()));
    }
}
