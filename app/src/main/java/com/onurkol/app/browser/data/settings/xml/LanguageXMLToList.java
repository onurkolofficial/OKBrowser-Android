package com.onurkol.app.browser.data.settings.xml;

import android.content.Context;
import android.content.res.TypedArray;

import com.onurkol.app.browser.R;

import java.util.ArrayList;
import java.util.Arrays;

public class LanguageXMLToList {
    /**
     * 'res/browser/values/languages.xml'
     */
    public static ArrayList<String> getLanguageNameList(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.languages_name)));
    }

    public static ArrayList<Integer> getLanguageValueList(Context context){
        int[] intList=context.getResources().getIntArray(R.array.languages_value);
        // Return
        ArrayList<Integer> intArrayList=new ArrayList<>();
        for (int index : intList)
            intArrayList.add(index);

        return intArrayList;
    }

    public static TypedArray getLanguageIconList(Context context){
        return context.getResources().obtainTypedArray(R.array.languages_preference_icons);
    }
}
