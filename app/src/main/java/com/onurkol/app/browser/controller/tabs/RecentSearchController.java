package com.onurkol.app.browser.controller.tabs;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.tabs.RecentSearchData;
import com.onurkol.app.browser.data.tabs.UserWebCollectionData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.tabs.RecentSearchControllerInterface;
import com.onurkol.app.browser.libs.ListToJson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RecentSearchController implements RecentSearchControllerInterface, BrowserDataInterface {
    private static WeakReference<RecentSearchController> instance=null;
    PreferenceController preferenceController;

    Gson gson=new Gson();

    private RecentSearchController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized RecentSearchController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new RecentSearchController());
        return instance.get();
    }

    @Override
    public void newSearch(@NonNull String searchSentence) {
        RecentSearchData data=new RecentSearchData(searchSentence);
        RECENT_SEARCHES.add(0,data);
        saveRecentSearchDataPreference();
    }

    @Override
    public void deleteSearch(int position) {
        RECENT_SEARCHES.remove(position);
        saveRecentSearchDataPreference();
    }

    @Override
    public void deleteAllSearch() {
        RECENT_SEARCHES.clear();
        saveRecentSearchDataPreference();
    }

    @Override
    public void syncRecentSearchData() {
        String recentSearchPreferenceList=preferenceController.getString(KEY_TAB_RECENT_SEARCH);
        if(recentSearchPreferenceList!=null && !recentSearchPreferenceList.equals(RECENT_SEARCH_NO_DATA)) {
            ArrayList<RecentSearchData> getPreferenceRecentSearchData=new ArrayList<>(
                    gson.fromJson(recentSearchPreferenceList, new TypeToken<ArrayList<RecentSearchData>>() {
                    }.getType()));
            RECENT_SEARCHES.clear();
            RECENT_SEARCHES.addAll(getPreferenceRecentSearchData);
        }
    }

    @Override
    public void saveRecentSearchDataPreference() {
        if(RECENT_SEARCHES.size()<=0) {
            preferenceController.setPreference(KEY_TAB_RECENT_SEARCH, RECENT_SEARCH_NO_DATA);
        }
        else{
            String newData=ListToJson.getJson(RECENT_SEARCHES);
            preferenceController.setPreference(KEY_TAB_RECENT_SEARCH, newData);
        }
    }

    @Override
    public ArrayList<RecentSearchData> getRecentSearchList() {
        return RECENT_SEARCHES;
    }
}
