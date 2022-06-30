package com.onurkol.app.browser.interfaces.tabs;

import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.TabFragment;

import java.util.ArrayList;

public interface TabDataInterface {
    String KEY_TAB_LIST="BROWSER_TAB_LIST",
            KEY_CURRENT_TAB_INDEX="BROWSER_TAB_CURRENT_INDEX",
            KEY_NEW_TAB_URL="BROWSER_TAB_URL";

    String TAB_NO_DATA="NO_TAB";
    int TAB_DEFAULT_INDEX=0;

    String NEW_TAB_URL="ok:new",
            NEW_INCOGNITO_TAB_URL="ok:incognito";

    // Tab List
    ArrayList<TabData> TAB_DATA_LIST=new ArrayList<>();
    ArrayList<TabData> TAB_DATA_LIST_RESTORE=new ArrayList<>();

    ArrayList<TabData> INCOGNITO_TAB_DATA_LIST=new ArrayList<>();
    ArrayList<TabData> INCOGNITO_TAB_DATA_LIST_RESTORE=new ArrayList<>();
}
