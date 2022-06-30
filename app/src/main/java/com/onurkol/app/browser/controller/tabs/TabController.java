package com.onurkol.app.browser.controller.tabs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.FragmentController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.data.tabs.TabDataPreference;
import com.onurkol.app.browser.fragments.tabs.TabFragment;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.tabs.TabControllerInterface;
import com.onurkol.app.browser.libs.ListToJson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TabController implements TabControllerInterface, BrowserDataInterface {
    private static WeakReference<TabController> instance=null;
    PreferenceController preferenceController;
    FragmentController fragmentController;

    Context mContext;

    final Gson gson=new Gson();

    TabFragment CURRENT_TAB_FRAGMENT;
    TabData CURRENT_TAB_DATA;

    private TabController(Context context){
        preferenceController=PreferenceController.getController();
        fragmentController=FragmentController.getController();
        mContext=context;
    }

    public static synchronized TabController getController(Context context){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new TabController(context));
        return instance.get();
    }

    @Override
    public void onStart() {
        // First Get Preference Saved Tabs
        String tabPreferenceList=preferenceController.getString(KEY_TAB_LIST);
        int currentTabIndex=preferenceController.getInt(KEY_CURRENT_TAB_INDEX);
        // Check Tab Exists
        if(tabPreferenceList==null || tabPreferenceList.equals(TAB_NO_DATA))
            newTab();
        else{
            // Sync Open Tabs
            TAB_DATA_LIST.clear();
            // String to List
            TAB_DATA_LIST.addAll(getTabDataListPreference());
            // Check Fragment And Start/Change last tab.
            TabData getLastTab=TAB_DATA_LIST.get(currentTabIndex);

            if(getLastTab.getTabFragment()==null){
                // Create Fragment Bundle
                Bundle bundle = new Bundle();
                bundle.putString(KEY_NEW_TAB_URL, getLastTab.getUrl());
                // Create Tab Fragment
                TabFragment tabFragment=new TabFragment();
                tabFragment.setTabIndex(currentTabIndex);
                tabFragment.setArguments(bundle);
                // Set Tab Fragment
                getLastTab.setTabFragment(tabFragment);
                // Set Current Tab
                CURRENT_TAB_FRAGMENT=tabFragment;
                CURRENT_TAB_DATA=getLastTab;
                // Check 'activity_main_simple.xml' and 'activity_main_dense.xml' in 'res/layouts/activity/'
                fragmentController.addFragment(R.id.browserFragmentViewForWeb, tabFragment);
            }
        }

        // Update Menu Tab Counter
        TabCounterController.setMenuTabCounterButton(mContext, TAB_DATA_LIST.size());
    }

    @Override
    public void onDestroy() {
        // Called 'finish();' App and destroy all data.
        removeAllTabs();
    }

    @Override
    public void newTab() {
        newTab(NEW_TAB_URL);
    }

    @Override
    public void newTab(@NonNull String Url) {
        hideAllTabs();
        // Get Tab Index
        int tabIndex=TAB_DATA_LIST.size();
        // Create Fragment Bundle
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NEW_TAB_URL, Url);
        // Create Fragment
        TabFragment newTabFragment=new TabFragment();
        newTabFragment.setTabIndex(tabIndex);
        newTabFragment.setIncognito(false);
        newTabFragment.setArguments(bundle);
        // Default Title
        String tabTitle=mContext.getString(R.string.new_tab_text);
        // Create New Data & Add List
        TabData data=new TabData(tabIndex, tabTitle, Url, null, newTabFragment);
        TAB_DATA_LIST.add(data);
        // Set Current Tab
        CURRENT_TAB_FRAGMENT=newTabFragment;
        CURRENT_TAB_DATA=data;
        // Check 'activity_main_simple.xml' and 'activity_main_dense.xml' in 'res/layouts/activity/'
        fragmentController.addFragment(R.id.browserFragmentViewForWeb, newTabFragment);
        fragmentController.showFragment(newTabFragment);
        fragmentController.attachFragment(newTabFragment);
        // Update Menu Tab Counter
        TabCounterController.setMenuTabCounterButton(mContext, TAB_DATA_LIST.size());
        saveTabDataPreference();
        saveCurrentTabIndexPreference(tabIndex);
    }

    @Override
    public void newIncognitoTab() {
        newIncognitoTab(NEW_INCOGNITO_TAB_URL);
    }

    @Override
    public void newIncognitoTab(@NonNull String Url) {
        hideAllTabs();
        // Get Incognito Tab Index
        int tabIndex=INCOGNITO_TAB_DATA_LIST.size();
        // Create Fragment Bundle
        Bundle bundle = new Bundle();
        bundle.putString(KEY_NEW_TAB_URL, Url);
        // Create Fragment
        TabFragment newIncognitoTabFragment=new TabFragment();
        newIncognitoTabFragment.setTabIndex(tabIndex);
        newIncognitoTabFragment.setIncognito(true);
        newIncognitoTabFragment.setArguments(bundle);
        // Default Title
        String tabTitle=mContext.getString(R.string.new_incognito_tab_text);
        // Create New Data & Add List
        TabData data=new TabData(tabIndex, tabTitle, Url, null, newIncognitoTabFragment);
        INCOGNITO_TAB_DATA_LIST.add(data);
        // Set Current Tab
        CURRENT_TAB_FRAGMENT=newIncognitoTabFragment;
        CURRENT_TAB_DATA=data;
        // Check 'activity_main_simple.xml' and 'activity_main_dense.xml' in 'res/layouts/activity/'
        fragmentController.addFragment(R.id.browserFragmentViewForWeb, newIncognitoTabFragment);
        fragmentController.showFragment(newIncognitoTabFragment);
        fragmentController.attachFragment(newIncognitoTabFragment);
        // Update Menu Tab Counter
        TabCounterController.setMenuTabCounterButton(mContext, INCOGNITO_TAB_DATA_LIST.size());
    }

    @Override
    public void changeTab(int tabIndex, boolean isIncognitoTab) {
        hideAllTabs();
        // Get Tab Data
        TabData getChangeTab;
        if(isIncognitoTab)
            getChangeTab=INCOGNITO_TAB_DATA_LIST.get(tabIndex);
        else
            getChangeTab=TAB_DATA_LIST.get(tabIndex);
        if(getChangeTab.getTabFragment()==null){
            // Create Fragment Bundle
            Bundle bundle = new Bundle();
            bundle.putString(KEY_NEW_TAB_URL, getChangeTab.getUrl());
            // Create Tab Fragment
            TabFragment tabFragment=new TabFragment();
            tabFragment.setTabIndex(tabIndex);
            tabFragment.setIncognito(isIncognitoTab);
            tabFragment.setArguments(bundle);
            // Set Tab Fragment
            getChangeTab.setTabFragment(tabFragment);
            // Set Current Tab
            CURRENT_TAB_FRAGMENT=tabFragment;
            // Check 'activity_main_simple.xml' and 'activity_main_dense.xml' in 'res/layouts/activity/'
            fragmentController.addFragment(R.id.browserFragmentViewForWeb, tabFragment);
            fragmentController.showFragment(tabFragment);
            fragmentController.attachFragment(tabFragment);
        }
        else {
            // Set Current Tab
            CURRENT_TAB_FRAGMENT=getChangeTab.getTabFragment();
            fragmentController.showFragment(getChangeTab.getTabFragment());
            fragmentController.attachFragment(getChangeTab.getTabFragment());
        }
        CURRENT_TAB_DATA=getChangeTab;
        if(!isIncognitoTab)
            saveCurrentTabIndexPreference(tabIndex);
    }

    @Override
    public void closeTab(int tabIndex, boolean isIncognito) {
        if(isIncognito) {
            if (INCOGNITO_TAB_DATA_LIST.get(tabIndex).getTabFragment()!=null)
                fragmentController.removeFragment(INCOGNITO_TAB_DATA_LIST.get(tabIndex).getTabFragment());
            INCOGNITO_TAB_DATA_LIST.remove(tabIndex);
            recreateTabIndex(true);
        }
        else {
            if (TAB_DATA_LIST.get(tabIndex).getTabFragment() != null)
                fragmentController.removeFragment(TAB_DATA_LIST.get(tabIndex).getTabFragment());
            TAB_DATA_LIST.remove(tabIndex);
            recreateTabIndex(false);
            saveTabDataPreference();
        }
        // Check 'MainActivity:onResume:if.isTabClose'.
        // saveCurrentTabIndexPreference(0);
    }

    @Override
    public void closeAllTabs() {
        removeAllTabs();
        saveTabDataPreference();
        saveCurrentTabIndexPreference(0);
    }

    @Override
    public void onRestoreTabData(boolean isIncognito) {
        if(isIncognito){
            INCOGNITO_TAB_DATA_LIST.clear();
            INCOGNITO_TAB_DATA_LIST.addAll(INCOGNITO_TAB_DATA_LIST_RESTORE);
        }
        else{
            TAB_DATA_LIST.clear();
            TAB_DATA_LIST.addAll(TAB_DATA_LIST_RESTORE);
        }
    }

    @Override
    public void onBackupPointTabData(boolean isIncognito) {
        if(isIncognito){
            INCOGNITO_TAB_DATA_LIST_RESTORE.clear();
            INCOGNITO_TAB_DATA_LIST_RESTORE.addAll(INCOGNITO_TAB_DATA_LIST);
        }
        else{
            TAB_DATA_LIST_RESTORE.clear();
            TAB_DATA_LIST_RESTORE.addAll(TAB_DATA_LIST);
        }

    }

    @Override
    public int getTabCount() {
        return TAB_DATA_LIST.size();
    }

    @Override
    public int getIncognitoTabCount() {
        return INCOGNITO_TAB_DATA_LIST.size();
    }

    @Override
    public int getAllTabCount() {
        return (TAB_DATA_LIST.size()+INCOGNITO_TAB_DATA_LIST.size());
    }

    @Override
    public void recreateTabIndex(boolean isIncognito) {
        int tabCount;
        if(isIncognito){
            tabCount=INCOGNITO_TAB_DATA_LIST.size();

            for (int i=0; i < tabCount; i++) {
                INCOGNITO_TAB_DATA_LIST.get(i).setTabIndex(i);
                if (INCOGNITO_TAB_DATA_LIST.get(i).getTabFragment() != null)
                    INCOGNITO_TAB_DATA_LIST.get(i).getTabFragment().setTabIndex(i);
            }
        }
        else {
            tabCount=TAB_DATA_LIST.size();

            for (int i=0; i < tabCount; i++) {
                TAB_DATA_LIST.get(i).setTabIndex(i);
                if (TAB_DATA_LIST.get(i).getTabFragment() != null)
                    TAB_DATA_LIST.get(i).getTabFragment().setTabIndex(i);
            }
        }
    }

    @Override
    public void saveCurrentTabIndexPreference(int tabIndex) {
        preferenceController.setPreference(KEY_CURRENT_TAB_INDEX, tabIndex);
    }

    @Override
    public void saveTabDataPreference() {
        ArrayList<TabDataPreference> savedTabData=new ArrayList<>();
        if(TAB_DATA_LIST.size()<=0) {
            preferenceController.setPreference(KEY_TAB_LIST, TAB_NO_DATA);
        }
        else{
            for(int i=0; i<TAB_DATA_LIST.size(); i++) {
                TabData currentData=TAB_DATA_LIST.get(i);
                savedTabData.add(new TabDataPreference(currentData.getTabIndex(),
                        currentData.getTitle(),
                        currentData.getUrl()));
            }
            String newData=ListToJson.getJson(savedTabData);
            preferenceController.setPreference(KEY_TAB_LIST, newData);
        }
    }

    @Override
    public ArrayList<TabData> getTabDataListPreference() {
        String tabPreferenceList=preferenceController.getString(KEY_TAB_LIST);
        ArrayList<TabData> tabDataList=new ArrayList<>();

        ArrayList<TabDataPreference> getPreferenceTabData = new ArrayList<>(
                gson.fromJson(tabPreferenceList, new TypeToken<ArrayList<TabDataPreference>>() {}.getType()));
        // Convert 'TabDataPreference' to 'TabData'
        for(int i=0; i<getPreferenceTabData.size(); i++) {
            tabDataList.add(new TabData(getPreferenceTabData.get(i).getTabIndex(),
                    getPreferenceTabData.get(i).getTitle(),
                    getPreferenceTabData.get(i).getUrl(),
                    null, null));
        }
        return tabDataList;
    }

    @Override
    public void hideAllTabs(){
        for(int i=0; i<TAB_DATA_LIST.size(); i++) {
            if(TAB_DATA_LIST.get(i).getTabFragment()!=null) {
                fragmentController.hideFragment(TAB_DATA_LIST.get(i).getTabFragment());
            }
        }
        for(int i=0; i<INCOGNITO_TAB_DATA_LIST.size(); i++) {
            if(INCOGNITO_TAB_DATA_LIST.get(i).getTabFragment()!=null) {
                fragmentController.hideFragment(INCOGNITO_TAB_DATA_LIST.get(i).getTabFragment());
            }
        }
    }

    @Override
    public void removeAllTabs() {
        for(int i=0; i<TAB_DATA_LIST.size(); i++) {
            if(TAB_DATA_LIST.get(i).getTabFragment()!=null)
                fragmentController.removeFragment(TAB_DATA_LIST.get(i).getTabFragment());
        }
        TAB_DATA_LIST.clear();
        for(int i=0; i<INCOGNITO_TAB_DATA_LIST.size(); i++) {
            if(INCOGNITO_TAB_DATA_LIST.get(i).getTabFragment()!=null) {
                fragmentController.removeFragment(INCOGNITO_TAB_DATA_LIST.get(i).getTabFragment());
            }
        }
        INCOGNITO_TAB_DATA_LIST.clear();
    }

    @Override
    public void replaceTabData(int tabDataIndex, TabData newTabData) {
        TAB_DATA_LIST.get(tabDataIndex).setTitle(newTabData.getTitle());
        TAB_DATA_LIST.get(tabDataIndex).setUrl(newTabData.getUrl());
        TAB_DATA_LIST.get(tabDataIndex).setPreviewBitmap(newTabData.getPreviewBitmap());
        TAB_DATA_LIST.get(tabDataIndex).setTabFragment(newTabData.getTabFragment());
    }

    @Override
    public void replaceIncognitoTabData(int tabDataIndex, TabData newIncognitoTabData) {
        INCOGNITO_TAB_DATA_LIST.get(tabDataIndex).setTitle(newIncognitoTabData.getTitle());
        INCOGNITO_TAB_DATA_LIST.get(tabDataIndex).setUrl(newIncognitoTabData.getUrl());
        INCOGNITO_TAB_DATA_LIST.get(tabDataIndex).setPreviewBitmap(newIncognitoTabData.getPreviewBitmap());
        INCOGNITO_TAB_DATA_LIST.get(tabDataIndex).setTabFragment(newIncognitoTabData.getTabFragment());
    }

    @Nullable
    @Override
    public TabFragment getTab(int tabIndex) {
        return TAB_DATA_LIST.get(tabIndex).getTabFragment();
    }

    @Nullable
    @Override
    public TabFragment getIncognitoTab(int tabIndex) {
        return INCOGNITO_TAB_DATA_LIST.get(tabIndex).getTabFragment();
    }

    @Nullable
    @Override
    public TabFragment getCurrentTab() {
        return CURRENT_TAB_FRAGMENT;
    }

    @Override
    public TabData getTabData(int tabDataIndex) {
        return TAB_DATA_LIST.get(tabDataIndex);
    }

    @Override
    public TabData getIncognitoTabData(int tabDataIndex) {
        return INCOGNITO_TAB_DATA_LIST.get(tabDataIndex);
    }

    @Override
    public TabData getCurrentTabData() {
        return CURRENT_TAB_DATA;
    }

    @Override
    public ArrayList<TabData> getTabList() {
        return TAB_DATA_LIST;
    }

    @Override
    public ArrayList<TabData> getIncognitoTabList() {
        return INCOGNITO_TAB_DATA_LIST;
    }

}
