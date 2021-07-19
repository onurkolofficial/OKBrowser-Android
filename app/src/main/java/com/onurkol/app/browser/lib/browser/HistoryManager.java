package com.onurkol.app.browser.lib.browser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.data.browser.HistoryData;
import com.onurkol.app.browser.interfaces.browser.history.HistoryManagers;
import com.onurkol.app.browser.interfaces.browser.history.HistorySettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.tools.ListToJson;

import java.util.ArrayList;

public class HistoryManager implements HistorySettings, HistoryManagers {
    private static HistoryManager instance;
    // Classes
    AppPreferenceManager prefManager=AppPreferenceManager.getInstance();

    Gson gson=new Gson();

    public static synchronized HistoryManager getInstance(){
        if(instance==null)
            instance=new HistoryManager();
        return instance;
    }

    @Override
    public void newHistory(HistoryData historyData) {
        // Get Preference Data
        if(BROWSER_HISTORY_LIST.size()<=0)
            syncSavedHistoryData();
        // Add Data
        BROWSER_HISTORY_LIST.add(0,historyData);
        // Save Preferences
        saveHistoryListPreference(BROWSER_HISTORY_LIST);
    }

    @Override
    public void deleteAllHistory() {
        // Clear Datas
        BROWSER_HISTORY_LIST.clear();
        // Save Preferences
        saveHistoryListPreference(BROWSER_HISTORY_LIST);
    }

    @Override
    public void saveHistoryListPreference(ArrayList<HistoryData> historyData) {
        String listData;
        if(historyData.size()<=0)
            listData="";
        else
            listData=ListToJson.getJson(historyData);
        // Save Preference
        prefManager.setPreference(KEY_HISTORY_PREFERENCE,listData);
    }

    @Override
    public ArrayList<HistoryData> getSavedHistoryData() {
        if(prefManager==null)
            prefManager=AppPreferenceManager.getInstance();
        // New Data
        ArrayList<HistoryData> savedHistoriesList=new ArrayList<>();
        // Get Preference
        String savedHistoriesString=prefManager.getString(KEY_HISTORY_PREFERENCE);
        if(savedHistoriesString!=null && !savedHistoriesString.equals(""))
            // Convert String to List
            savedHistoriesList=gson.fromJson(savedHistoriesString, new TypeToken<ArrayList<HistoryData>>(){}.getType());
        return savedHistoriesList;
    }


    @Override
    public void syncSavedHistoryData() {
        // Clear History Data
        BROWSER_HISTORY_LIST.clear();
        // Sync
        BROWSER_HISTORY_LIST.addAll(getSavedHistoryData());
    }
}

