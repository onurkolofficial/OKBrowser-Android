package com.onurkol.app.browser.data.settings.xml;

import android.content.Context;
import android.content.res.TypedArray;

import com.onurkol.app.browser.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DayNightXMLToList {
    /**
     * 'res/browser/values/day_night_modes.xml'
     */
    public static ArrayList<String> getDayNightNameList(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.day_night_mode_name)));
    }

    public static ArrayList<Integer> getDayNightValueList(Context context){
        int[] intList=context.getResources().getIntArray(R.array.day_night_mode_value);
        // Return
        ArrayList<Integer> intArrayList=new ArrayList<>();
        for (int index : intList)
            intArrayList.add(index);

        return intArrayList;
    }

    public static TypedArray getDayNightIconList(Context context){
        return context.getResources().obtainTypedArray(R.array.day_night_mode_preference_icons);
    }
}
