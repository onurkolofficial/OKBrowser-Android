package com.onurkol.app.browser.data.settings;

public class SettingsPreferenceDataInteger {
    private String stringData;
    private Integer integerData;
    private boolean isPreferenceData;
    private String preferenceKey;

    public SettingsPreferenceDataInteger(String getStringData, Integer getIntegerData){
        stringData=getStringData;
        integerData=getIntegerData;
        isPreferenceData=false;
        preferenceKey=null;
    }

    public SettingsPreferenceDataInteger(String getStringData, Integer getIntegerData, boolean getIsPreferenceData, String getPreferenceKey){
        stringData=getStringData;
        integerData=getIntegerData;
        isPreferenceData=getIsPreferenceData;
        preferenceKey=getPreferenceKey;
    }

    public String getStringData(){
        return stringData;
    }
    public Integer getIntegerData(){
        return integerData;
    }
    public boolean getIsPreferenceData(){
        return isPreferenceData;
    }
    public String getPreferenceKey(){
        return preferenceKey;
    }
}
