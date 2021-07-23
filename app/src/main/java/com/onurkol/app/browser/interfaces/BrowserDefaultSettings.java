package com.onurkol.app.browser.interfaces;

import android.os.Environment;

public interface BrowserDefaultSettings {
    // Setting Preference Keys
    String KEY_LOAD_PREFERENCE="DATA_LOAD_PREFERENCE",
            KEY_JAVASCRIPT_MODE="BROWSER_JAVASCRIPT_MODE",
            KEY_GEO_LOCATION="BROWSER_GEO_LOCATION",
            KEY_POPUPS="BROWSER_POPUPS",
            KEY_DOM_STORAGE="BROWSER_DOM_STORAGE",
            KEY_ZOOM="BROWSER_ZOOM",
            KEY_ZOOM_BUTTONS="BROWSER_ZOOM_BUTTONS",
            KEY_APP_CACHE="BROWSER_APP_CACHE",
            KEY_SAVE_FORMS="BROWSER_SAVE_FORMS",
            KEY_SEARCH_ENGINE="BROWSER_SEARCH_ENGINE",
            KEY_APP_LANGUAGE="APPLICATION_LANGUAGE",
            KEY_APP_THEME="APPLICATION_THEME";

    // Default Settings
    boolean DEFAULT_JAVASCRIPT_MODE=true,
            DEFAULT_GEO_LOCATION=false,
            DEFAULT_POPUPS=false,
            DEFAULT_DOM_STORAGE=true,
            DEFAULT_ZOOM=true,
            DEFAULT_ZOOM_BUTTONS=false,
            DEFAULT_APP_CACHE=true,
            DEFAULT_SAVE_FORMS=true;

    // Download Folder
    String BROWSER_DOWNLOAD_FOLDER=Environment.DIRECTORY_DOWNLOADS+"/OKDownloads";
    String BROWSER_STORAGE_FOLDER=Environment.getExternalStorageDirectory().getPath();

    // browser/values/search_engines.xml
    int DEFAULT_SEARCH_ENGINE=0;

    // browser/values/app_themes.xml
    int DEFAULT_APP_THEME=0;
    int DEFAULT_APP_THEME_NEW_API=2; // API 28 and Newest version. (2 is auto)

    int DEFAULT_APP_LANGUAGE=0; // browser/values/app_languages.xml

    String DESKTOP_USER_AGENT="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36";

    void initBrowserDataClasses();
    void initBrowserPreferenceSettings();
    void applyApplicationViewSettings();
}
