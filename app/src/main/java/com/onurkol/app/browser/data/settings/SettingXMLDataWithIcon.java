package com.onurkol.app.browser.data.settings;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class SettingXMLDataWithIcon {
    /**
     * WARNING!
     * Look at 'res/browser/values/*.xml'
     * Only <String> and <Integer> tags.
     * ! If set preference data to isPreferenceData is true;
     **/
    private final String stringData;
    private final Integer integerData;
    private final Drawable iconData;
    private final String preferenceKey;

    public SettingXMLDataWithIcon(String StringData, Integer IntegerData, Drawable IconDrawable, @Nullable String PreferenceKey){
        stringData=StringData;
        integerData=IntegerData;
        iconData=IconDrawable;
        preferenceKey=PreferenceKey;
    }

    public String getStringData(){
        return stringData;
    }
    public Integer getIntegerData(){
        return integerData;
    }
    public Drawable getIconData() { return iconData; }
    public String getPreferenceKey(){
        return preferenceKey;
    }
}
