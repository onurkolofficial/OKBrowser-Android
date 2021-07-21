package com.onurkol.app.browser.lib.browser.downloads;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.data.browser.DownloadsData;
import com.onurkol.app.browser.interfaces.browser.downloads.DownloadsManagers;
import com.onurkol.app.browser.interfaces.browser.downloads.DownloadsSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.tools.ListToJson;

import java.util.ArrayList;

public class DownloadsManager implements DownloadsSettings, DownloadsManagers {
    private static DownloadsManager instance;

    // Classes
    AppPreferenceManager prefManager=AppPreferenceManager.getInstance();
    Gson gson=new Gson();

    public static synchronized DownloadsManager getInstance(){
        if(instance==null)
            instance=new DownloadsManager();
        return instance;
    }

    @Override
    public void newDownload(DownloadsData downloadsData) {
        // Get Preference Data
        if(BROWSER_DOWNLOAD_LIST.size()<=0)
            syncSavedDownloadsData();
        // Add Data
        BROWSER_DOWNLOAD_LIST.add(0,downloadsData);
        // Save Preferences
        saveDownloadsListPreference(BROWSER_DOWNLOAD_LIST);
    }

    @Override
    public void saveDownloadsListPreference(ArrayList<DownloadsData> downloadsData) {
        String listData;
        if(downloadsData.size()<=0)
            listData="";
        else
            listData=ListToJson.getJson(downloadsData);
        // Save Preference
        prefManager.setPreference(KEY_DOWNLOAD_PREFERENCE,listData);
    }

    @Override
    public ArrayList<DownloadsData> getSavedDownloadsData() {
        if(prefManager==null)
            prefManager=AppPreferenceManager.getInstance();
        // New Data
        ArrayList<DownloadsData> savedDownloadsList=new ArrayList<>();
        // Get Preference
        String savedDownloadsString=prefManager.getString(KEY_DOWNLOAD_PREFERENCE);
        if(savedDownloadsString!=null && !savedDownloadsString.equals(""))
            // Convert String to List
            savedDownloadsList=gson.fromJson(savedDownloadsString, new TypeToken<ArrayList<DownloadsData>>(){}.getType());
        return savedDownloadsList;
    }

    @Override
    public void syncSavedDownloadsData() {
        // Clear History Data
        BROWSER_DOWNLOAD_LIST.clear();
        // Sync
        BROWSER_DOWNLOAD_LIST.addAll(getSavedDownloadsData());
    }
}
