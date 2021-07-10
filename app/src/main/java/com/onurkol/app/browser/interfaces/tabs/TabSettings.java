package com.onurkol.app.browser.interfaces.tabs;

import com.onurkol.app.browser.data.tabs.ClassesTabData;
import com.onurkol.app.browser.data.tabs.IncognitoTabData;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.IncognitoTabFragment;
import com.onurkol.app.browser.fragments.tabs.TabFragment;

import java.util.ArrayList;

public interface TabSettings {
    // Preference Keys
    String KEY_TAB_PREFERENCE="BROWSER_TAB_LIST_PREFERENCE",
            KEY_TAB_URL_SENDER="TAB_DATA_URL";

    // Variables
    String NEW_TAB_URL="";

    // Tab Data List
    ArrayList<TabData> BROWSER_TAB_LIST=new ArrayList<>();
    ArrayList<ClassesTabData> BROWSER_CLASSES_TAB_LIST=new ArrayList<>();
    ArrayList<IncognitoTabData> BROWSER_INCOGNITO_LIST=new ArrayList<>();
    // Tab Fragment List
    ArrayList<TabFragment> BROWSER_TAB_FRAGMENT_LIST=new ArrayList<>();
    ArrayList<IncognitoTabFragment> BROWSER_INCOGNITO_FRAGMENT_LIST=new ArrayList<>();

}
