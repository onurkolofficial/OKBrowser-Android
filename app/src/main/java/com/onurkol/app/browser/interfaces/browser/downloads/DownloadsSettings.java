package com.onurkol.app.browser.interfaces.browser.downloads;

import com.onurkol.app.browser.data.browser.DownloadsData;

import java.util.ArrayList;

public interface DownloadsSettings {
    // Preference Keys
    String KEY_DOWNLOAD_PREFERENCE="BROWSER_DOWNLOADS_PREFERENCE";

    // Bookmark List
    ArrayList<DownloadsData> BROWSER_DOWNLOAD_LIST=new ArrayList<>();
}
