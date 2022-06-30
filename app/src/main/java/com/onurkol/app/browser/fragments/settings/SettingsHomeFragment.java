package com.onurkol.app.browser.fragments.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsAboutActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsDayNightActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsGuiActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsLanguageActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsSearchEngineActivity;
import com.onurkol.app.browser.activity.browser.settings.SettingsWebActivity;
import com.onurkol.app.browser.controller.ApplicationResetController;
import com.onurkol.app.browser.libs.ActivityActionAnimator;

public class SettingsHomeFragment extends PreferenceFragmentCompat {
    Preference preferenceWebSettings,preferenceSearchEngine,prefereneceDayNightMode,prefereneceGUIMode,
            preferenceLanguage,preferenceAboutApp,preferenceResetApp;

    public static boolean isCreated;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Set Resource
        setPreferencesFromResource(R.xml.preference_settings_home,rootKey);

        prefereneceDayNightMode=findPreference("prefereneceDayNightMode");
        preferenceLanguage=findPreference("preferenceLanguage");
        preferenceAboutApp=findPreference("preferenceAboutApp");
        prefereneceGUIMode=findPreference("prefereneceGUIMode");
        preferenceSearchEngine=findPreference("preferenceSearchEngine");
        preferenceWebSettings=findPreference("preferenceWebSettings");
        preferenceResetApp=findPreference("preferenceResetApp");

        prefereneceDayNightMode.setOnPreferenceClickListener(preference -> {
            ActivityActionAnimator.startActivity(requireActivity(),
                    new Intent(requireActivity(), SettingsDayNightActivity.class));
            return false;
        });
        preferenceLanguage.setOnPreferenceClickListener(preference -> {
            ActivityActionAnimator.startActivity(requireActivity(),
                    new Intent(requireActivity(), SettingsLanguageActivity.class));
            return false;
        });
        preferenceAboutApp.setOnPreferenceClickListener(preference -> {
            ActivityActionAnimator.startActivity(requireActivity(),
                    new Intent(requireActivity(), SettingsAboutActivity.class));
            return false;
        });
        prefereneceGUIMode.setOnPreferenceClickListener(preference -> {
            ActivityActionAnimator.startActivity(requireActivity(),
                    new Intent(requireActivity(), SettingsGuiActivity.class));
            return false;
        });
        preferenceSearchEngine.setOnPreferenceClickListener(preference -> {
            ActivityActionAnimator.startActivity(requireActivity(),
                    new Intent(requireActivity(), SettingsSearchEngineActivity.class));
            return false;
        });
        preferenceWebSettings.setOnPreferenceClickListener(preference -> {
            ActivityActionAnimator.startActivity(requireActivity(),
                    new Intent(requireActivity(), SettingsWebActivity.class));
            return false;
        });
        preferenceResetApp.setOnPreferenceClickListener(preference -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setMessage(getString(R.string.question_delete_all_data_text))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getString(R.string.yes_text),(dialog, which) -> {
                        ApplicationResetController.clearAllData();
                        MainActivity.isClearAllData=true;
                        ActivityActionAnimator.finish(requireActivity());
                    })
                    .setNegativeButton(getString(R.string.no_text), null)
                    .show();
            return false;
        });

        isCreated=true;
    }
}
