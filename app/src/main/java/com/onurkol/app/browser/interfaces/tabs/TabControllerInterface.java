package com.onurkol.app.browser.interfaces.tabs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.TabFragment;

import java.util.ArrayList;

public interface TabControllerInterface {
    void onStart();
    void onDestroy();

    void newTab();
    void newTab(@NonNull String Url);

    void newIncognitoTab();
    void newIncognitoTab(@NonNull String Url);

    void changeTab(int tabIndex, boolean isIncognitoTab);

    void closeTab(int tabIndex, boolean isIncognito);
    void closeAllTabs();
    void onRestoreTabData(boolean isIncognito);
    void onBackupPointTabData(boolean isIncognito);

    int getTabCount();
    int getIncognitoTabCount();
    int getAllTabCount();

    void recreateTabIndex(boolean isIncognito);
    void saveCurrentTabIndexPreference(int tabIndex);
    void saveTabDataPreference();
    ArrayList<TabData> getTabDataListPreference();

    void hideAllTabs();
    void removeAllTabs();

    void replaceTabData(int tabDataIndex, TabData newTabData);
    void replaceIncognitoTabData(int tabDataIndex, TabData newIncognitoTabData);

    @Nullable TabFragment getTab(int tabIndex);
    @Nullable TabFragment getIncognitoTab(int tabIndex);
    @Nullable TabFragment getCurrentTab();
    TabData getTabData(int tabDataIndex);
    TabData getIncognitoTabData(int tabDataIndex);
    TabData getCurrentTabData();

    ArrayList<TabData> getTabList();
    ArrayList<TabData> getIncognitoTabList();
}
