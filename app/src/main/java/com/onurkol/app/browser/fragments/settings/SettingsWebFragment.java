package com.onurkol.app.browser.fragments.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.onurkol.app.browser.R;

public class SettingsWebFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Set Resource
        setPreferencesFromResource(R.xml.preference_settings_web,rootKey);


    }
}
