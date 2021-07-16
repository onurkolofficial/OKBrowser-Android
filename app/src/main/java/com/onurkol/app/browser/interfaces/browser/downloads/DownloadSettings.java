package com.onurkol.app.browser.interfaces.browser.downloads;

import com.onurkol.app.browser.data.browser.DownloadData;

import java.util.ArrayList;
import java.util.List;

public interface DownloadSettings {
    // Preference Keys
    String KEY_DOWNLOAD_PREFERENCE="BROWSER_DOWNLOADS_PREFERENCE";

    // Bookmark List
    List<DownloadData> BROWSER_DOWNLOAD_LIST=new ArrayList<>();
}
