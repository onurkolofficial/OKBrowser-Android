package com.onurkol.app.browser.lib.tabs;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.tabs.ClassesTabData;
import com.onurkol.app.browser.data.tabs.IncognitoTabData;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.IncognitoTabFragment;
import com.onurkol.app.browser.fragments.tabs.TabFragment;
import com.onurkol.app.browser.interfaces.tabs.TabManagers;
import com.onurkol.app.browser.interfaces.tabs.TabSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.tools.ProcessDelay;
import com.onurkol.app.browser.webview.OKWebView;

import java.util.ArrayList;

public class TabManager implements TabSettings, TabManagers {
    // Variables
    AppPreferenceManager prefManager;

    // Tab Manager Objects
    private OKWebView manTabWebView=null, manIncognitoWebView=null;
    private TabFragment manTabFragment=null;
    private IncognitoTabFragment manIncognitoFragment=null;
    private int manTabIndex, manIncognitoIndex;

    Gson gson=new Gson();

    public void BuildManager(){
        prefManager=AppPreferenceManager.getInstance();
        // Load Preference Data
        loadTabPreferenceData();
    }

    private void loadTabPreferenceData(){
        // Check Preference Load
        if(prefManager.getString(KEY_TAB_PREFERENCE)==null)
            prefManager.setPreference(KEY_TAB_PREFERENCE,"");
    }

    Context getContext(){
        return ContextManager.getManager().getContext();
    }

    FragmentManager getFragmentManager(){
        return ((FragmentActivity)getContext()).getSupportFragmentManager();
    }

    @Override
    public void createNewTab() {
        createNewTabHandler("");
    }

    @Override
    public void createNewTab(String Url) {
        createNewTabHandler(Url);
    }

    private void createNewTabHandler(String Url){
        // Remove Active Incognito Tabs
        setActiveIncognitoFragment(null);
        setActiveIncognitoWebView(null);

        // Fragment Bundle
        Bundle bundle = new Bundle();
        // Put Send Url
        bundle.putString(KEY_TAB_URL_SENDER,Url);

        // Create Tab Fragment
        TabFragment newTabFragment=new TabFragment();
        // Add View
        addFragmentView(R.id.browserFragmentView, newTabFragment);
        // Set Bundle
        newTabFragment.setArguments(bundle);

        // Default Tab Title
        String tabTitle=getContext().getString(R.string.loading_text)+" ...";

        // Tab Current Data
        TabData data=new TabData(tabTitle,"");
        ClassesTabData classesTabData=new ClassesTabData(null);
        // Add Tab List Data & Classes Data
        BROWSER_TAB_LIST.add(data);
        BROWSER_CLASSES_TAB_LIST.add(classesTabData);
        // Add Tab Fragment Data
        BROWSER_TAB_FRAGMENT_LIST.add(newTabFragment);
        // Get Tab Index
        int tabSize=BROWSER_TAB_LIST.size();
        int newTabIndex=tabSize-1;
        // Set Tab Index
        setActiveTabIndex(newTabIndex);

        // Tab Save to Preference
        saveTabListPreference(BROWSER_TAB_LIST);

        // Show New Tab Fragment
        for (int i=0; i < tabSize; i++) {
            if(BROWSER_TAB_FRAGMENT_LIST.get(i)==newTabFragment)
                showFragment(newTabFragment);
            else
                hideFragment(BROWSER_TAB_FRAGMENT_LIST.get(i));
        }
        // Hide All Incognito Tabs
        hideAllIncognitos();
    }

    private void hideAllTabs(){
        // Hide All Normal Tabs
        for(int i=0; i<BROWSER_TAB_FRAGMENT_LIST.size(); i++){
            if(!BROWSER_TAB_FRAGMENT_LIST.get(i).isHidden())
                hideFragment(BROWSER_TAB_FRAGMENT_LIST.get(i));
        }
    }

    @Override
    public void changeTab(int tabIndex) {
        // Get Selected Fragment
        TabFragment selectFragment=BROWSER_TAB_FRAGMENT_LIST.get(tabIndex);
        // Set Tab Index
        setActiveTabIndex(tabIndex);
        // Set Tab Fragment
        setActiveTabFragment(selectFragment);

        // Remove Active Incognito Tabs
        setActiveIncognitoFragment(null);
        setActiveIncognitoWebView(null);
        // Hide All Incognito Tabs
        hideAllIncognitos();

        // Change Fragment View
        int fragList=BROWSER_TAB_FRAGMENT_LIST.size();
        for (int i=0; i < fragList; i++) {
            if(BROWSER_TAB_FRAGMENT_LIST.get(i)==selectFragment){
                if(selectFragment.isHidden()) {
                    showFragment(selectFragment);
                    // Call Fragment Data Refresh
                    selectFragment.refreshLoadView();
                    // Resume WebView <for BUG>
                    ((OKWebView)selectFragment.getView().findViewById(R.id.okBrowserWebView)).onResume();
                }
            }
            else {
                if(!BROWSER_TAB_FRAGMENT_LIST.get(i).isHidden()) {
                    // Hide Fragments
                    hideFragment(BROWSER_TAB_FRAGMENT_LIST.get(i));
                    // Pause WebView <for BUG>
                    ((OKWebView)BROWSER_TAB_FRAGMENT_LIST.get(i).getView().findViewById(R.id.okBrowserWebView)).onPause();
                }
            }
        }
    }

    @Override
    public void setActiveTabFragment(TabFragment TabFragment) {
        manTabFragment=TabFragment;
    }
    @Override
    public void setActiveTabWebView(OKWebView TabWebView) {
        manTabWebView=TabWebView;
    }
    @Override
    public void setActiveTabIndex(int Index) {
        manTabIndex=Index;
    }

    @Override
    public TabFragment getActiveTabFragment() {
        return manTabFragment;
    }
    @Override
    public OKWebView getActiveTabWebView() {
        return manTabWebView;
    }
    @Override
    public int getActiveTabIndex() {
        return manTabIndex;
    }

    @Deprecated
    @Override
    public int getTabCount() {
        return BROWSER_TAB_FRAGMENT_LIST.size();
    }

    @Override
    public ArrayList<TabFragment> getTabFragmentList() {
        return BROWSER_TAB_FRAGMENT_LIST;
    }
    @Override
    public ArrayList<TabData> getTabDataList() {
        return BROWSER_TAB_LIST;
    }
    @Override
    public ArrayList<ClassesTabData> getClassesTabDataList() {
        return BROWSER_CLASSES_TAB_LIST;
    }

    @Override
    public void saveTabListPreference(ArrayList<TabData> tabDataList) {
        int listSize=tabDataList.size();
        // Variables
        String newData;
        if(listSize>0) {
            // Added New Data
            StringBuilder processData = new StringBuilder();
            for (int i = 0; i < listSize; i++) {
                if (i == (listSize - 1))
                    processData.append(gson.toJson(tabDataList.get(i)));
                else
                    processData.append(gson.toJson(tabDataList.get(i))).append(",");
            }
            newData = "[" + processData + "]";
        }
        else
            // Clear All Tabs
            newData="";
        // Save Preference
        prefManager.setPreference(KEY_TAB_PREFERENCE,newData);
    }

    @Override
    public ArrayList<TabData> getSavedTabList() {
        // New Data
        ArrayList<TabData> savedTabsList=new ArrayList<>();
        // Get Preference
        String savedTabsString=prefManager.getString(KEY_TAB_PREFERENCE);
        if(!savedTabsString.equals("")){
            // Convert String to List
            savedTabsList=gson.fromJson(savedTabsString, new TypeToken<ArrayList<TabData>>(){}.getType());
        }
        return savedTabsList;
    }

    @Override
    public void syncSavedTabs(){
        // Clear Tab Data
        BROWSER_TAB_LIST.clear();
        BROWSER_CLASSES_TAB_LIST.clear();
        BROWSER_TAB_FRAGMENT_LIST.clear();
        // SYNC
        BROWSER_TAB_LIST.addAll(getSavedTabList());
        // SYNC Fragments (create fragments & add views)
        int tabDataCount=getSavedTabList().size();
        for (int i=0; i<tabDataCount; i++) {
            // Create Fragments
            TabFragment newDataFragment=new TabFragment();
            // Add Views
            addFragmentView(R.id.browserFragmentView, newDataFragment);
            // Hide Fragments
            if(i==0)
                // Call Fragment Data Refresh
                ProcessDelay.Delay(() -> newDataFragment.refreshLoadView(),500);
            else
                hideFragment(newDataFragment);
            // Add List
            BROWSER_TAB_FRAGMENT_LIST.add(newDataFragment);
            // Tab Classes Data
            BROWSER_CLASSES_TAB_LIST.add(new ClassesTabData(null));
        }
    }

    @Override
    public void updateSyncTabData(int position, TabData updateData, ClassesTabData updateClassesData, OKWebView webView) {
        // Remove Old Datas
        BROWSER_TAB_LIST.remove(position);
        BROWSER_CLASSES_TAB_LIST.remove(position);
        // Add New Data in Index
        BROWSER_TAB_LIST.add(position, updateData);
        // Update Tab Classes Data
        BROWSER_CLASSES_TAB_LIST.add(position, updateClassesData);
        // Save New Preference
        saveTabListPreference(BROWSER_TAB_LIST);
    }

    @Override
    public void createNewIncognitoTab() {
        createIncognitoTabHandler("");
    }

    @Override
    public void createNewIncognitoTab(String Url) {
        createIncognitoTabHandler(Url);
    }

    private void createIncognitoTabHandler(String Url){
        // Remove Active Incognito Tabs
        setActiveTabFragment(null);
        setActiveTabWebView(null);

        // Fragment Bundle
        Bundle bundle = new Bundle();
        // Put Send Url
        bundle.putString(KEY_TAB_URL_SENDER,Url);

        // Create Incognito Tab Fragment
        IncognitoTabFragment incognitoTabFragment=new IncognitoTabFragment();
        // Add View
        addFragmentView(R.id.browserFragmentView, incognitoTabFragment);
        // Set Bundle
        incognitoTabFragment.setArguments(bundle);

        // Default Tab Title
        String tabTitle=getContext().getString(R.string.loading_text)+" ...";

        // Tab Current Data
        IncognitoTabData data=new IncognitoTabData(tabTitle, Url, incognitoTabFragment, null);
        // Add Tab Data & Fragment
        BROWSER_INCOGNITO_LIST.add(data);
        BROWSER_INCOGNITO_FRAGMENT_LIST.add(incognitoTabFragment);
        // Get Tab Index
        int tabSize=BROWSER_INCOGNITO_LIST.size();
        int tabIndex=tabSize-1;
        // Set Tab Index
        setActiveIncognitoIndex(tabIndex);
        // Show New Tab Fragment
        for (int i=0; i < tabSize; i++) {
            if(BROWSER_INCOGNITO_FRAGMENT_LIST.get(i)==incognitoTabFragment)
                showFragment(incognitoTabFragment);
            else
                hideFragment(BROWSER_INCOGNITO_FRAGMENT_LIST.get(i));
        }
        // Hide All Tabs
        hideAllTabs();
    }

    private void hideAllIncognitos(){
        // Hide All Incognito Tabs
        for(int i=0; i<BROWSER_INCOGNITO_FRAGMENT_LIST.size(); i++){
            if(!BROWSER_INCOGNITO_FRAGMENT_LIST.get(i).isHidden())
                hideFragment(BROWSER_INCOGNITO_FRAGMENT_LIST.get(i));
        }
    }

    @Override
    public void changeIncognitoTab(int tabIndex) {
        // Get Selected Fragment
        IncognitoTabFragment selectFragment=BROWSER_INCOGNITO_FRAGMENT_LIST.get(tabIndex);
        // Set Tab Index
        setActiveIncognitoIndex(tabIndex);
        // Set Tab Fragment
        setActiveIncognitoFragment(selectFragment);

        // Remove Active Incognito Tabs
        setActiveTabFragment(null);
        setActiveTabWebView(null);
        // Hide All Incognito Tabs
        hideAllTabs();

        // Change Fragment View
        int fragList=BROWSER_INCOGNITO_FRAGMENT_LIST.size();

        for (int i=0; i < fragList; i++) {
            if(BROWSER_INCOGNITO_FRAGMENT_LIST.get(i)==selectFragment){
                if(selectFragment.isHidden())
                    // Note!
                    // Incognito tabs not saved. So fragment webview not required refresh view.
                    showFragment(selectFragment);
            }
            else{
                if(!BROWSER_INCOGNITO_FRAGMENT_LIST.get(i).isHidden())
                    // Hide Fragments
                    hideFragment(BROWSER_INCOGNITO_FRAGMENT_LIST.get(i));
            }
        }
    }

    @Override
    public void setActiveIncognitoFragment(IncognitoTabFragment IncognitoFragment) {
        manIncognitoFragment=IncognitoFragment;
    }
    @Override
    public void setActiveIncognitoWebView(OKWebView IncognitoWebView) {
        manIncognitoWebView=IncognitoWebView;
    }
    @Override
    public IncognitoTabFragment getActiveIncognitoFragment() {
        return manIncognitoFragment;
    }
    @Override
    public OKWebView getActiveIncognitoWebView() {
        return manIncognitoWebView;
    }

    @Override
    public void setActiveIncognitoIndex(int Index) {
        manIncognitoIndex=Index;
    }
    @Override
    public int getActiveIncognitoTabIndex() {
        return manIncognitoIndex;
    }

    @Deprecated
    @Override
    public int getIncognitoTabCount() {
        return BROWSER_INCOGNITO_FRAGMENT_LIST.size();
    }

    @Override
    public ArrayList<IncognitoTabFragment> getIncognitoTabFragmentList() {
        return BROWSER_INCOGNITO_FRAGMENT_LIST;
    }

    @Override
    public ArrayList<IncognitoTabData> getIncognitoTabDataList() {
        return BROWSER_INCOGNITO_LIST;
    }

    // Fragments
    @Override
    public void addFragmentView(int viewId, Fragment fragment) {
        getFragmentManager().beginTransaction().add(viewId, fragment).commit();
    }

    @Override
    public void removeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().show(fragment).commit();
    }
    @Override
    public void hideFragment(Fragment fragment) {
        if(!fragment.isHidden())
            getFragmentManager().beginTransaction().hide(fragment).commit();
    }

    @Override
    public void attachFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().attach(fragment).commit();
    }

    @Override
    public void detachFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().detach(fragment).commit();
    }
}
