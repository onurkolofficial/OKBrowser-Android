package com.onurkol.app.browser.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.fragments.SettingsFragment;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;

public class SettingsActivity extends AppCompatActivity {

    // Elements
    ImageButton backButton;
    TextView settingName;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;
    // Intents
    Intent installerIntent,settingsWebIntent,settingsAboutIntent,settingsSearchEnginesIntent,
            settingsThemesIntent,settingsLanguagesIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Get Intents
        installerIntent=new Intent(this, InstallerActivity.class);
        // Check Get Shortcut
        if(isTaskRoot())
            dataManager.initBrowserPreferenceSettings();

        // Check Installer Activity
        if(dataManager.startInstallerActivity){
            // Start Welcome Activity
            startActivity(installerIntent);
            // Finish Current Activity
            finish();
        }

        // Get Elements
        backButton=findViewById(R.id.backSettingsButton);
        settingName=findViewById(R.id.settingName);

        // Set Toolbar Title
        settingName.setText(getString(R.string.settings_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Get Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.settingsFragmentContent,new SettingsFragment()).commit();
    }

    @Override
    protected void onResume() {
        // Re-Building ContextManager
        ContextManager.Build(this);

        super.onResume();
    }
}