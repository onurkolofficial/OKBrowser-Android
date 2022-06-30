package com.onurkol.app.browser.controller.browser;

import android.os.Build;

import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

import java.lang.ref.WeakReference;

public class BrowserDataInitController implements BrowserDataInterface {
    private static WeakReference<BrowserDataInitController> instance=null;
    PreferenceController preferenceController;

    private BrowserDataInitController(){
        preferenceController=PreferenceController.getController();
    }

    public static synchronized BrowserDataInitController getController(){
        if(instance==null || instance.get()==null)
            instance=new WeakReference<>(new BrowserDataInitController());
        return instance.get();
    }

    private boolean installerCompleted;

    public void init(){
        installerCompleted=preferenceController.getBoolean(KEY_BROWSER_INSTALLER_COMPLETE);

        // Javascript Mode
        if (!preferenceController.isExist(KEY_JAVASCRIPT_MODE))
            preferenceController.setPreference(KEY_JAVASCRIPT_MODE, DEFAULT_JAVASCRIPT_MODE);
        // Geo Location
        if (!preferenceController.isExist(KEY_GEO_LOCATION))
            preferenceController.setPreference(KEY_GEO_LOCATION, DEFAULT_GEO_LOCATION);
        // Popups
        if (!preferenceController.isExist(KEY_POPUPS))
            preferenceController.setPreference(KEY_POPUPS, DEFAULT_POPUPS);
        // DOM Storage
        if (!preferenceController.isExist(KEY_DOM_STORAGE))
            preferenceController.setPreference(KEY_DOM_STORAGE, DEFAULT_DOM_STORAGE);
        // Zoom
        if (!preferenceController.isExist(KEY_ZOOM))
            preferenceController.setPreference(KEY_ZOOM, DEFAULT_ZOOM);
        // Zoom Buttons
        if (!preferenceController.isExist(KEY_ZOOM_BUTTONS))
            preferenceController.setPreference(KEY_ZOOM_BUTTONS, DEFAULT_ZOOM_BUTTONS);
        // App Cache
        if (!preferenceController.isExist(KEY_APP_CACHE))
            preferenceController.setPreference(KEY_APP_CACHE, DEFAULT_APP_CACHE);
        // Save Forms
        if (!preferenceController.isExist(KEY_SAVE_FORMS))
            preferenceController.setPreference(KEY_SAVE_FORMS, DEFAULT_SAVE_FORMS);
        // Search Engine
        if(!preferenceController.isExist(KEY_SEARCH_ENGINE))
            preferenceController.setPreference(KEY_SEARCH_ENGINE,DEFAULT_SEARCH_ENGINE);
        // Language Settings
        if(!preferenceController.isExist(KEY_LANGUAGE))
            preferenceController.setPreference(KEY_LANGUAGE, DEFAULT_LANGUAGE);
        // Day/Night Mode Settings
        int default_dn_mode;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            default_dn_mode=DEFAULT_NIGHT_MODE_V30;
        else
            default_dn_mode=DEFAULT_NIGHT_MODE;
        if(!preferenceController.isExist(KEY_DAY_NIGHT_MODE))
            preferenceController.setPreference(KEY_DAY_NIGHT_MODE, default_dn_mode);
        // Gui Mode Settings
        if(!preferenceController.isExist(KEY_GUI_MODE))
            preferenceController.setPreference(KEY_GUI_MODE, DEFAULT_GUI_MODE);
        // Tabs And Data
        if(!preferenceController.isExist(KEY_TAB_LIST) ||
                preferenceController.getString(KEY_TAB_LIST).equals(""))
            preferenceController.setPreference(KEY_TAB_LIST, TAB_NO_DATA);
        if(!preferenceController.isExist(KEY_CURRENT_TAB_INDEX))
            preferenceController.setPreference(KEY_CURRENT_TAB_INDEX, TAB_DEFAULT_INDEX);
        // User Web Collection Data
        if(!preferenceController.isExist(KEY_TAB_USER_WEB_COLLECTION) ||
                preferenceController.getString(KEY_TAB_USER_WEB_COLLECTION).equals(""))
            preferenceController.setPreference(KEY_TAB_USER_WEB_COLLECTION, COLLECTION_NO_DATA);
        // Recent Search Data
        if(!preferenceController.isExist(KEY_TAB_RECENT_SEARCH) ||
                preferenceController.getString(KEY_TAB_RECENT_SEARCH).equals(""))
            preferenceController.setPreference(KEY_TAB_RECENT_SEARCH, RECENT_SEARCH_NO_DATA);
        // History Data
        if(!preferenceController.isExist(KEY_BROWSER_HISTORY) ||
                preferenceController.getString(KEY_BROWSER_HISTORY).equals(""))
            preferenceController.setPreference(KEY_BROWSER_HISTORY, HISTORY_NO_DATA);
        // Bookmark Data
        if(!preferenceController.isExist(KEY_BROWSER_BOOKMARK) ||
                preferenceController.getString(KEY_BROWSER_BOOKMARK).equals(""))
            preferenceController.setPreference(KEY_BROWSER_BOOKMARK, BOOKMARK_NO_DATA);
        // Download Data
        if(!preferenceController.isExist(KEY_BROWSER_DOWNLOAD) ||
                preferenceController.getString(KEY_BROWSER_DOWNLOAD).equals(""))
            preferenceController.setPreference(KEY_BROWSER_DOWNLOAD, DOWNLOAD_NO_DATA);
    }

    public void setInstallerCompleted(boolean value){
        PreferenceController.getController().setPreference(KEY_BROWSER_INSTALLER_COMPLETE, value);
    }
    public boolean isInstallerCompleted(){
        return installerCompleted;
    }
}
