package com.onurkol.app.browser.data.settings.xml;

import android.content.Context;
import android.content.res.TypedArray;

import com.onurkol.app.browser.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchEngineXMLToList {
    /**
     * 'res/browser/values/search_engines.xml'
     */
    public static ArrayList<String> getSearchEngineNameList(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.search_engine_name)));
    }

    public static ArrayList<String> getSearchEngineQueryList(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.search_engine_query)));
    }

    public static ArrayList<Integer> getSearchEngineValueList(Context context){
        int[] intList=context.getResources().getIntArray(R.array.search_engine_value);
        // Return
        ArrayList<Integer> intArrayList=new ArrayList<>();
        for (int index : intList)
            intArrayList.add(index);

        return intArrayList;
    }

    public static TypedArray getSearchEngineIconList(Context context){
        return context.getResources().obtainTypedArray(R.array.search_engine_preference_icons);
    }
}
