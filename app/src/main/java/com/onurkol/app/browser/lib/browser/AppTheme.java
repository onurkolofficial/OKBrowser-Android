package com.onurkol.app.browser.lib.browser;

import android.content.Context;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppTheme {
    private static AppTheme instance=null;

    Context context;

    private AppTheme(){
        context= ContextManager.getManager().getContext();
    }

    public static synchronized AppTheme getInstance() {
        if(instance==null)
            instance=new AppTheme();
        return instance;
    }

    public ArrayList<String> getThemeNameList() {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.app_themes_name)));
    }

    public ArrayList<Integer> getThemeValueList() {
        return returnIntegerListHandler(R.array.app_themes_value);
    }

    private ArrayList<Integer> returnIntegerListHandler(int arrayName){
        int[] list=context.getResources().getIntArray(arrayName);
        // Return
        ArrayList<Integer> intValue=new ArrayList<>();
        for (int index : list)
            intValue.add(index);

        return intValue;
    }
}
