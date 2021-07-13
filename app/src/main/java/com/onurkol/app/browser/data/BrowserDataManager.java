package com.onurkol.app.browser.data;

import android.content.Context;
import android.os.Build;

import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.core.LanguageManager;
import com.onurkol.app.browser.lib.core.ThemeManager;
import com.onurkol.app.browser.lib.browser.SearchEngine;
import com.onurkol.app.browser.lib.tabs.TabBuilder;

public class BrowserDataManager implements BrowserDefaultSettings {
    Context context;
    AppPreferenceManager prefManager;
    SearchEngine searchEngine;
    TabBuilder tabBuilder;

    public boolean startInstallerActivity=false;

    @Override
    public void initBrowserDataClasses() {
        context=ContextManager.getManager().getContext();
        prefManager=AppPreferenceManager.getInstance();
        searchEngine=SearchEngine.getInstance();

        // Check & Load Preference Data
        if(!prefManager.getBoolean(KEY_LOAD_PREFERENCE))
            startInstallerActivity=true;
    }

    @Override
    public void initBrowserPreferenceSettings() {
        if(context==null)
            initBrowserDataClasses();
        // Load Default Data
        loadBrowserPreferenceData();

        // Set Default Search Engine
        searchEngine.setSearchEngine(DEFAULT_SEARCH_ENGINE);

        // Building Tabs
        tabBuilder=TabBuilder.Build();
        // Build Tab Data
        tabBuilder.BuildManager();

        // Apply View Settings (Language, Theme, ...)
        applyApplicationViewSettings();
    }

    private void loadBrowserPreferenceData(){
        // Javascript Mode
        if(!prefManager.getBoolean(KEY_JAVASCRIPT_MODE))
            prefManager.setPreference(KEY_JAVASCRIPT_MODE,DEFAULT_JAVASCRIPT_MODE);
        // Geo Location
        if(!prefManager.getBoolean(KEY_GEO_LOCATION))
            prefManager.setPreference(KEY_GEO_LOCATION,DEFAULT_GEO_LOCATION);
        // Popups
        if(!prefManager.getBoolean(KEY_POPUPS))
            prefManager.setPreference(KEY_POPUPS,DEFAULT_POPUPS);
        // DOM Storage
        if(!prefManager.getBoolean(KEY_DOM_STORAGE))
            prefManager.setPreference(KEY_DOM_STORAGE,DEFAULT_DOM_STORAGE);
        // Zoom
        if(!prefManager.getBoolean(KEY_ZOOM))
            prefManager.setPreference(KEY_ZOOM,DEFAULT_ZOOM);
        // Zoom Buttons
        if(!prefManager.getBoolean(KEY_ZOOM_BUTTONS))
            prefManager.setPreference(KEY_ZOOM_BUTTONS,DEFAULT_ZOOM_BUTTONS);
        // App Cache
        if(!prefManager.getBoolean(KEY_APP_CACHE))
            prefManager.setPreference(KEY_APP_CACHE,DEFAULT_APP_CACHE);
        // Save Forms
        if(!prefManager.getBoolean(KEY_SAVE_FORMS))
            prefManager.setPreference(KEY_SAVE_FORMS,DEFAULT_SAVE_FORMS);
        // Desktop Mode
        if(!prefManager.getBoolean(KEY_DESKTOP_MODE))
            prefManager.setPreference(KEY_DESKTOP_MODE,DEFAULT_DESKTOP_MODE);
        // Language Settings
        if(prefManager.getInt(KEY_APP_LANGUAGE)==AppPreferenceManager.INTEGER_NULL)
            prefManager.setPreference(KEY_APP_LANGUAGE, DEFAULT_APP_LANGUAGE);
        // Theme Settings
        int default_theme;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            default_theme=DEFAULT_APP_THEME_NEW_API;
        else
            default_theme=DEFAULT_APP_THEME;
        if(prefManager.getInt(KEY_APP_THEME)==AppPreferenceManager.INTEGER_NULL)
            prefManager.setPreference(KEY_APP_THEME, default_theme);
    }

    public void successDataLoad(){
        startInstallerActivity=false;
        // Check Preference Load
        if(!prefManager.getBoolean(KEY_LOAD_PREFERENCE))
            prefManager.setPreference(KEY_LOAD_PREFERENCE,true);
    }

    @Override
    public void applyApplicationViewSettings(){
        // Get Variables
        int getLanguage=prefManager.getInt(KEY_APP_LANGUAGE);
        int getTheme=prefManager.getInt(KEY_APP_THEME);

        // Apply Language & Theme
        LanguageManager.getInstance().setAppLanguage(getLanguage);
        ThemeManager.getInstance().setAppTheme(getTheme);
    }
}
