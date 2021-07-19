package com.onurkol.app.browser.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.SettingsActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsAboutActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsLanguageActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsSearchEnginesActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsThemesActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsWebActivity;
import com.onurkol.app.browser.data.BrowserDataManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    // Preferences
    Preference settingsWebPref,settingsAboutPref,settingsSearchEnginesPref,settingsThemePref,
            settingsLanguagePref;
    // Intents
    Intent settingsWebIntent,settingsAboutIntent,settingsSearchEnginesIntent,settingsThemesIntent,
            settingsLanguagesIntent;
    // Classes
    BrowserDataManager dataManager;
    // Variables
    public static boolean isPreferenceFragmentCreated=false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Get Classes
        dataManager=new BrowserDataManager();
        // Init
        dataManager.initBrowserPreferenceSettings();
        // Set Resource
        setPreferencesFromResource(R.xml.preference_settings,rootKey);

        // Get Intents
        settingsWebIntent=new Intent(getActivity(), SettingsWebActivity.class);
        settingsAboutIntent=new Intent(getActivity(), SettingsAboutActivity.class);
        settingsSearchEnginesIntent=new Intent(getActivity(), SettingsSearchEnginesActivity.class);
        settingsThemesIntent=new Intent(getActivity(), SettingsThemesActivity.class);
        settingsLanguagesIntent=new Intent(getActivity(), SettingsLanguageActivity.class);

        // Get Preferences
        settingsWebPref=findPreference("pref_web_settings");
        settingsAboutPref=findPreference("pref_show_about");
        settingsSearchEnginesPref=findPreference("pref_search_engine");
        settingsThemePref=findPreference("pref_theme");
        settingsLanguagePref=findPreference("pref_language");

        // Preference Click Listeners
        settingsWebPref.setOnPreferenceClickListener(preference -> {
            startActivity(settingsWebIntent);
            return false;
        });
        settingsAboutPref.setOnPreferenceClickListener(preference -> {
            startActivity(settingsAboutIntent);
            return false;
        });
        settingsSearchEnginesPref.setOnPreferenceClickListener(preference -> {
            startActivity(settingsSearchEnginesIntent);
            return false;
        });
        settingsThemePref.setOnPreferenceClickListener(preference -> {
            startActivity(settingsThemesIntent);
            return false;
        });
        settingsLanguagePref.setOnPreferenceClickListener(preference -> {
            startActivity(settingsLanguagesIntent);
            return false;
        });

        isPreferenceFragmentCreated=true;
    }
}