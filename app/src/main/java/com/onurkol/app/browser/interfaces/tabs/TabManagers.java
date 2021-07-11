package com.onurkol.app.browser.interfaces.tabs;

import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.data.tabs.ClassesTabData;
import com.onurkol.app.browser.data.tabs.IncognitoTabData;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.IncognitoTabFragment;
import com.onurkol.app.browser.fragments.tabs.TabFragment;
import com.onurkol.app.browser.webview.OKWebView;

import java.util.ArrayList;

public interface TabManagers {
    // Normal Tabs
    void createNewTab();
    void createNewTab(String Url);
    void changeTab(int tabIndex);
    // Data
    void setActiveTabFragment(TabFragment TabFragment);
    void setActiveTabWebView(OKWebView TabWebView);
    void setActiveTabIndex(int Index);
    TabFragment getActiveTabFragment();
    OKWebView getActiveTabWebView();
    int getActiveTabIndex();
    @Deprecated // Because tab count update issue.
    int getTabCount();
    // Datas
    ArrayList<TabFragment> getTabFragmentList();
    ArrayList<TabData> getTabDataList();
    ArrayList<ClassesTabData> getClassesTabDataList();
    // Saved Tabs
    //void saveTabPreference(TabData tabData);
    void saveTabListPreference(ArrayList<TabData> tabDataList);
    ArrayList<TabData> getSavedTabList();
    void syncSavedTabs();
    void updateSyncTabData(int position, TabData updateData, ClassesTabData updateClassesData, OKWebView webView);

    // Incognito Tabs
    void createNewIncognitoTab();
    void createNewIncognitoTab(String Url);
    void changeIncognitoTab(int tabIndex);
    // Data
    void setActiveIncognitoFragment(IncognitoTabFragment IncognitoFragment);
    void setActiveIncognitoWebView(OKWebView IncognitoWebView);
    IncognitoTabFragment getActiveIncognitoFragment();
    OKWebView getActiveIncognitoWebView();
    void setActiveIncognitoIndex(int Index);
    int getActiveIncognitoTabIndex();
    @Deprecated
    int getIncognitoTabCount();
    // Datas
    ArrayList<IncognitoTabFragment> getIncognitoTabFragmentList();
    ArrayList<IncognitoTabData> getIncognitoTabDataList();

    // Fragment Views
    void addFragmentView(int viewId, Fragment fragment);
    void removeFragment(Fragment fragment);
    void showFragment(Fragment fragment);
    void hideFragment(Fragment fragment);
    void attachFragment(Fragment fragment);
    void detachFragment(Fragment fragment);
    boolean isShowingFragment(Fragment fragment);
    boolean isHiddenFragment(Fragment fragment);
    boolean isAttachedFragment(Fragment fragment);
    boolean isDetachedFragment(Fragment fragment);
}
