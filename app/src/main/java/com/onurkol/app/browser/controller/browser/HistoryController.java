package com.onurkol.app.browser.controller.browser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.data.browser.HistoryData;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.browser.HistoryControllerInterface;
import com.onurkol.app.browser.libs.DateManager;
import com.onurkol.app.browser.libs.ListToJson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class HistoryController implements HistoryControllerInterface, BrowserDataInterface {
    private static WeakReference<HistoryController> instance=null;
    PreferenceController preferenceController;

    Gson gson=new Gson();

    private HistoryController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized HistoryController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new HistoryController());
        return instance.get();
    }

    @Override
    public void newHistory(String historyTitle, String historyUrl) {
        // Get Date
        String historyDate=DateManager.getDate();
        HistoryData data=new HistoryData(historyTitle, historyUrl, historyDate);
        HISTORY_LIST.add(0, data);
        saveHistoryDataPreference();
    }

    @Override
    public void deleteHistory(int position) {
        HISTORY_LIST.remove(position);
        saveHistoryDataPreference();
    }

    @Override
    public void deleteAllHistory() {
        HISTORY_LIST.clear();
        saveHistoryDataPreference();
    }

    @Override
    public void updateHistory(int position, HistoryData historyData) {
        HISTORY_LIST.get(position).setTitle(historyData.getTitle());
        saveHistoryDataPreference();
    }

    @Override
    public void syncHistoryData() {
        String historyPreferenceList=preferenceController.getString(KEY_BROWSER_HISTORY);
        if(historyPreferenceList!=null && !historyPreferenceList.equals(HISTORY_NO_DATA)) {
            ArrayList<HistoryData> getHistoryPreferenceData = new ArrayList<>(
                    gson.fromJson(historyPreferenceList, new TypeToken<ArrayList<HistoryData>>() {
                    }.getType()));
            HISTORY_LIST.clear();
            HISTORY_LIST.addAll(getHistoryPreferenceData);
        }
    }

    @Override
    public void saveHistoryDataPreference() {
        if(HISTORY_LIST.size()<=0) {
            preferenceController.setPreference(KEY_BROWSER_HISTORY, HISTORY_NO_DATA);
        }
        else{
            String newData=ListToJson.getJson(HISTORY_LIST);
            preferenceController.setPreference(KEY_BROWSER_HISTORY, newData);
        }
    }

    @Override
    public ArrayList<HistoryData> getHistoryList() {
        return HISTORY_LIST;
    }
}
