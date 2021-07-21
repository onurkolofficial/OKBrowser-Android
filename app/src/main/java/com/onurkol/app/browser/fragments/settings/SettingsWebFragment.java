package com.onurkol.app.browser.fragments.settings;

import android.os.Bundle;

import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;

public class SettingsWebFragment extends PreferenceFragmentCompat {

    CheckBoxPreference geoLocationPref,javascriptPref,popupsPref,saveFormsPref,zoomPref,zoomButtonsPref,localStoragePref,appCachePref;
    boolean geoLocationData,javascriptData,popupsData,saveFormsData,zoomData,zoomButtonsData,localStorageData,appCacheData;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Set Resource
        setPreferencesFromResource(R.xml.preference_settings_web,rootKey);

        // Get Classes
        AppPreferenceManager prefManager=AppPreferenceManager.getInstance();

        // Get Preferences
        geoLocationPref=findPreference("setting_geo_location");
        javascriptPref=findPreference("setting_javascript");
        popupsPref=findPreference("setting_popups");
        saveFormsPref=findPreference("setting_save_forms");
        zoomPref=findPreference("setting_zoom");
        zoomButtonsPref=findPreference("setting_zoom_buttons");
        localStoragePref=findPreference("setting_local_storage");
        appCachePref=findPreference("setting_app_cache");

        // Get Preference Data
        geoLocationData=prefManager.getBoolean(BrowserDefaultSettings.KEY_GEO_LOCATION);
        javascriptData=prefManager.getBoolean(BrowserDefaultSettings.KEY_JAVASCRIPT_MODE);
        popupsData=prefManager.getBoolean(BrowserDefaultSettings.KEY_POPUPS);
        saveFormsData=prefManager.getBoolean(BrowserDefaultSettings.KEY_SAVE_FORMS);
        zoomData=prefManager.getBoolean(BrowserDefaultSettings.KEY_ZOOM);
        zoomButtonsData=prefManager.getBoolean(BrowserDefaultSettings.KEY_ZOOM_BUTTONS);
        localStorageData=prefManager.getBoolean(BrowserDefaultSettings.KEY_DOM_STORAGE);
        appCacheData=prefManager.getBoolean(BrowserDefaultSettings.KEY_APP_CACHE);

        // Preference Click Events
        // Geo Location
        geoLocationPref.setOnPreferenceClickListener(preference -> {
            // Set Value
            geoLocationData=(!geoLocationData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_GEO_LOCATION, geoLocationData);
            return false;
        });
        // Javascript
        javascriptPref.setOnPreferenceClickListener(preference -> {
            // Set Value
            javascriptData=(!javascriptData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_JAVASCRIPT_MODE, javascriptData);
            return false;
        });
        // Popups
        popupsPref.setOnPreferenceClickListener(preference -> {
            // Set Value
            popupsData=(!popupsData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_POPUPS, popupsData);
            return false;
        });
        // Save Forms
        saveFormsPref.setOnPreferenceClickListener(preference -> {
            // Set Value
            saveFormsData=(!saveFormsData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_SAVE_FORMS, saveFormsData);
            return false;
        });
        // Zoom
        zoomPref.setOnPreferenceClickListener(preference -> {
            // Set Value
            zoomData=(!zoomData);
            // Check Zoom Settings
            zoomButtonsPref.setEnabled(zoomData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_ZOOM, zoomData);
            return false;
        });
        // Zoom Buttons
        zoomButtonsPref.setOnPreferenceClickListener(preference -> {
            // Set Value
            zoomButtonsData=(!zoomButtonsData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_ZOOM_BUTTONS, zoomButtonsData);
            return false;
        });
        // Local Storage
        localStoragePref.setOnPreferenceClickListener(preference -> {
            // Set Value
            localStorageData=(!localStorageData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_DOM_STORAGE, localStorageData);
            return false;
        });
        // App Cache
        appCachePref.setOnPreferenceClickListener(preference -> {
            // Set Value
            appCacheData=(!appCacheData);
            // Save Preference
            prefManager.setPreference(BrowserDefaultSettings.KEY_APP_CACHE, appCacheData);
            return false;
        });

        // Check Settings
        geoLocationPref.setChecked(geoLocationData);
        javascriptPref.setChecked(javascriptData);
        popupsPref.setChecked(popupsData);
        saveFormsPref.setChecked(saveFormsData);
        zoomPref.setChecked(zoomData);
        zoomButtonsPref.setEnabled(zoomData);
        zoomButtonsPref.setChecked(zoomButtonsData);
        localStoragePref.setChecked(localStorageData);
        appCachePref.setChecked(appCacheData);
    }
}
