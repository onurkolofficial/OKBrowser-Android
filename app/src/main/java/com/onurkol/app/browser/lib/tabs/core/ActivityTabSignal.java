package com.onurkol.app.browser.lib.tabs.core;

public class ActivityTabSignal {
    public static final int TAB_ON_CHANGE=0x100,
            TAB_ON_CREATE=0x101,
            INCOGNITO_ON_CHANGE=0x200,
            INCOGNITO_ON_CREATE=0x201;

    private int CURRENT_STATUS;

    private boolean TAB_TYPE_INCOGNITO;

    private TabSignalData SIGNAL_DATA;

    // Set Signal Status
    public void setSignalData(TabSignalData NEW_DATA){
        SIGNAL_DATA=NEW_DATA;
    }
    // Get Signal Data
    public TabSignalData getSignalData(){
        return SIGNAL_DATA;
    }
    // Set Signal Status
    public void setSignalStatus(int STATUS_CODE){
        CURRENT_STATUS=STATUS_CODE;
    }
    // Get Signal Status
    public int getSignalStatus(){
        return CURRENT_STATUS;
    }
    // Set Incognito Tab
    public void setTabIsIncognito(boolean isIncognito){ TAB_TYPE_INCOGNITO=isIncognito; }
    // Get Incognito Tab
    public boolean getTabIsIncognito(){ return TAB_TYPE_INCOGNITO; }

    // Signal Data Object
    public static class TabSignalData {
        public int tab_position=0;
    }

}
