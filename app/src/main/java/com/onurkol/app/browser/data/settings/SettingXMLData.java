package com.onurkol.app.browser.data.settings;

import androidx.annotation.Nullable;

public class SettingXMLData {
    /**
     * WARNING!
     * Look at 'res/browser/values/*.xml'
     * Only <String> and <Integer> tags.
     * ! If set preference data to isPreferenceData is true;
     **/
    private final String stringData;
    private final Integer integerData;
    private final String preferenceKey;

    public SettingXMLData(String StringData, Integer IntegerData, @Nullable String PreferenceKey){
        stringData=StringData;
        integerData=IntegerData;
        preferenceKey=PreferenceKey;
    }

    public String getStringData(){
        return stringData;
    }
    public Integer getIntegerData(){
        return integerData;
    }
    public String getPreferenceKey(){
        return preferenceKey;
    }
}
