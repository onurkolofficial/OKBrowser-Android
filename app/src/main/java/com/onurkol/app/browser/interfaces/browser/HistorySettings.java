package com.onurkol.app.browser.interfaces.browser;

import com.onurkol.app.browser.data.browser.history.HistoryData;
import com.onurkol.app.browser.data.browser.history.HistoryDate_Data;

import java.util.ArrayList;

public interface HistorySettings {
    // Preference Keys
    String KEY_HISTORY_DATE_PREFERENCE="BROWSER_HISTORY_DATE_PREFERENCE",
            KEY_HISTORY_PREFERENCE="BROWSER_HISTORY_PREFERENCE";

    // Tab Data List
    ArrayList<HistoryData> BROWSER_HISTORY_LIST=new ArrayList<>();
    ArrayList<HistoryDate_Data> BROWSER_HISTORY_DATE_LIST=new ArrayList<>();
}
