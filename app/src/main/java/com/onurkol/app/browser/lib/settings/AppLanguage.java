package com.onurkol.app.browser.lib.settings;

import android.content.Context;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;

import java.util.ArrayList;
import java.util.Arrays;

public class AppLanguage {
    private static AppLanguage instance=null;

    Context context;

    private AppLanguage(){
        context=ContextManager.getManager().getContext();
    }

    public static synchronized AppLanguage getInstance() {
        if(instance==null)
            instance=new AppLanguage();
        return instance;
    }

    public ArrayList<String> getLanguageNameList() {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.app_languages_name)));
    }

    public ArrayList<Integer> getLanguageValueList() {
        return returnIntegerListHandler(R.array.app_languages_value);
    }

    private ArrayList<Integer> returnIntegerListHandler(int arrayName){
        int[] list=context.getResources().getIntArray(arrayName);
        // Return
        ArrayList<Integer> intValue=new ArrayList<>();
        for (int index : list)
            intValue.add(index);

        return intValue;
    };
}
