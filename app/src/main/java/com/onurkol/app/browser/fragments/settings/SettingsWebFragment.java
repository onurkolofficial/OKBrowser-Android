package com.onurkol.app.browser.fragments.settings;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

public class SettingsWebFragment extends PreferenceFragmentCompat implements BrowserDataInterface {
    CheckBoxPreference settingGeoLocation, settingJavascript, settingPopups, settingSaveForms, settingZoom,
            settingZoomButtons, settingWebStorage, settingAppCache;
    boolean geoLocation, javascript, popups, saveForms, zoom, zoomButtons, webStorage, appCache;

    PreferenceController preferenceController;

    public static boolean isCreated;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        // Set Resource
        setPreferencesFromResource(R.xml.preference_settings_web,rootKey);

        preferenceController=PreferenceController.getController();

        settingGeoLocation=findPreference("settingGeoLocation");
        settingJavascript=findPreference("settingJavascript");
        settingPopups=findPreference("settingPopups");
        settingSaveForms=findPreference("settingSaveForms");
        settingZoom=findPreference("settingZoom");
        settingZoomButtons=findPreference("settingZoomButtons");
        settingWebStorage=findPreference("settingWebStorage");
        settingAppCache=findPreference("settingAppCache");

        // Get Data
        geoLocation=preferenceController.getBoolean(KEY_GEO_LOCATION);
        javascript=preferenceController.getBoolean(KEY_JAVASCRIPT_MODE);
        popups=preferenceController.getBoolean(KEY_POPUPS);
        saveForms=preferenceController.getBoolean(KEY_SAVE_FORMS);
        zoom=preferenceController.getBoolean(KEY_ZOOM);
        zoomButtons=preferenceController.getBoolean(KEY_ZOOM_BUTTONS);
        webStorage=preferenceController.getBoolean(KEY_DOM_STORAGE);
        appCache=preferenceController.getBoolean(KEY_APP_CACHE);

        settingGeoLocation.setOnPreferenceClickListener(preference -> {
            // New Value
            geoLocation=(!geoLocation);
            // Save Preference
            preferenceController.setPreference(KEY_GEO_LOCATION, geoLocation);
            return false;
        });
        settingJavascript.setOnPreferenceClickListener(preference -> {
            // New Value
            javascript=(!javascript);
            // Save Preference
            preferenceController.setPreference(KEY_JAVASCRIPT_MODE, javascript);
            return false;
        });
        settingPopups.setOnPreferenceClickListener(preference -> {
            // New Value
            popups=(!popups);
            // Save Preference
            preferenceController.setPreference(KEY_POPUPS, popups);
            return false;
        });
        settingSaveForms.setOnPreferenceClickListener(preference -> {
            // New Value
            saveForms=(!saveForms);
            // Save Preference
            preferenceController.setPreference(KEY_SAVE_FORMS, saveForms);
            return false;
        });
        settingZoom.setOnPreferenceClickListener(preference -> {
            // New Value
            zoom=(!zoom);
            // Zoom Button Status
            settingZoomButtons.setEnabled(zoom);
            // Save Preference
            preferenceController.setPreference(KEY_ZOOM, zoom);
            return false;
        });
        settingZoomButtons.setOnPreferenceClickListener(preference -> {
            // New Value
            zoomButtons=(!zoomButtons);
            // Save Preference
            preferenceController.setPreference(KEY_ZOOM_BUTTONS, zoomButtons);
            return false;
        });
        settingWebStorage.setOnPreferenceClickListener(preference -> {
            // New Value
            webStorage=(!webStorage);
            // Save Preference
            preferenceController.setPreference(KEY_DOM_STORAGE, webStorage);
            return false;
        });
        settingAppCache.setOnPreferenceClickListener(preference -> {
            // New Value
            appCache=(!appCache);
            // Save Preference
            preferenceController.setPreference(KEY_APP_CACHE, appCache);
            return false;
        });

        settingGeoLocation.setChecked(geoLocation);
        settingJavascript.setChecked(javascript);
        settingPopups.setChecked(popups);
        settingSaveForms.setChecked(saveForms);
        settingZoom.setChecked(zoom);
        settingZoomButtons.setEnabled(zoom);
        settingZoomButtons.setChecked(zoomButtons);
        settingWebStorage.setChecked(webStorage);
        settingAppCache.setChecked(appCache);

        isCreated=true;
    }
}
