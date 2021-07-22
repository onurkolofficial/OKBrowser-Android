package com.onurkol.app.browser.fragments.settings;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.onurkol.app.browser.BuildConfig;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.activity.SettingsActivity;
import com.onurkol.app.browser.interfaces.BrowserActionKeys;
import com.onurkol.app.browser.lib.ContextManager;

public class SettingsAboutFragment extends PreferenceFragmentCompat implements BrowserActionKeys {

    Preference appVersionPref,androidVersionPref,openDevWebPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Set Resource
        setPreferencesFromResource(R.xml.preference_about, rootKey);

        // Get Classes
        ContextManager contextManager=ContextManager.getManager();

        // Get Version Data
        String appVersion=BuildConfig.VERSION_NAME+" - "+BuildConfig.VERSION_CODE;
        String androidVersion=Build.VERSION.RELEASE+" - API "+Build.VERSION.SDK_INT;
        String developerWebPage="https://onurkolofficial.cf/en";

        // Get Preferences
        appVersionPref=findPreference("pref_app_version");
        androidVersionPref=findPreference("pref_android_version");
        openDevWebPref=findPreference("pref_dev_web_page");

        // Set Summary Texts
        appVersionPref.setSummary(appVersion);
        androidVersionPref.setSummary(androidVersion);

        // Set Click Listener
        openDevWebPref.setOnPreferenceClickListener(preference -> {
            // Get Intent
            Intent mainActivityIntent;
            boolean isCreateNewActivity;

            // Check Context
            if(contextManager.getBaseContext()==null) {
                isCreateNewActivity=true;
                mainActivityIntent = new Intent(getActivity(), MainActivity.class);
            }
            else {
                isCreateNewActivity=false;
                if(contextManager.getBaseContextActivity().getIntent()!=null)
                    mainActivityIntent = contextManager.getBaseContextActivity().getIntent();
                else
                    mainActivityIntent = new Intent(getActivity(), MainActivity.class);
            }
            // Create new Bundle
            Bundle bundle = new Bundle();
            // Check Action
            bundle.putString(ACTION_NAME, KEY_ACTION_TAB_ON_CREATE);
            bundle.putString(ACTION_VALUE, developerWebPage);
            // Set extras
            mainActivityIntent.putExtras(bundle);

            // Check Activity
            if(isCreateNewActivity)
                getActivity().startActivity(mainActivityIntent);
            else
                MainActivity.updatedIntent=mainActivityIntent;
            // Close Current and Parent (SettingsActivity) Activity
            SettingsActivity.isCloseActivity=true;
            getActivity().finish();

            return false;
        });
    }
}
