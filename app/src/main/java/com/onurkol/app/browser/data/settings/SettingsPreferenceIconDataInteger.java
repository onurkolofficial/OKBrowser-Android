package com.onurkol.app.browser.data.settings;

import android.graphics.drawable.Drawable;

public class SettingsPreferenceIconDataInteger {
    private String stringData;
    private Integer integerData;
    private Drawable dataIcon;
    private boolean isPreferenceData;
    private String preferenceKey;

    public SettingsPreferenceIconDataInteger(String getStringData, Integer getIntegerData, Drawable getDataIcon){
        stringData=getStringData;
        integerData=getIntegerData;
        dataIcon=getDataIcon;
        isPreferenceData=false;
        preferenceKey=null;
    }

    public SettingsPreferenceIconDataInteger(String getStringData, Integer getIntegerData, Drawable getDataIcon, boolean getIsPreferenceData, String getPreferenceKey){
        stringData=getStringData;
        integerData=getIntegerData;
        dataIcon=getDataIcon;
        isPreferenceData=getIsPreferenceData;
        preferenceKey=getPreferenceKey;
    }

    public String getStringData(){
        return stringData;
    }
    public Integer getIntegerData(){
        return integerData;
    }
    public Drawable getDataIcon(){
        return dataIcon;
    }
    public boolean getIsPreferenceData(){
        return isPreferenceData;
    }
    public String getPreferenceKey(){
        return preferenceKey;
    }
}
