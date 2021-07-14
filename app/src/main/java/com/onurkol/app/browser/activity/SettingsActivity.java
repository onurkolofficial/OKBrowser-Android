package com.onurkol.app.browser.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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
    Intent installerIntent;
    // Variables
    public static boolean isCreated=false,isCreatedView=false,isConfigChanged=false;
    public static String changedConfigName;
    public static Integer changedConfigValue;

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
        if(isTaskRoot() && !isCreated)
            dataManager.initBrowserPreferenceSettings();

        // Check Installer Activity
        if(dataManager.startInstallerActivity){
            // Start Welcome Activity
            startActivity(installerIntent);
            // Finish Current Activity
            finish();
        }
        else
            if(!isTaskRoot() && !isConfigChanged)
                dataManager.initBrowserPreferenceSettings();


        // Get Elements
        backButton=findViewById(R.id.backSettingsButton);
        settingName=findViewById(R.id.settingName);

        // Set Toolbar Title
        settingName.setText(getString(R.string.settings_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Get Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.settingsFragmentContent,new SettingsFragment()).commit();

        isCreated=true;
    }

    @Override
    protected void onResume() {
        // Re-Building ContextManager
        ContextManager.Build(this);

        if(isConfigChanged) {
            if(changedConfigName!=null && changedConfigValue!=null)
                dataManager.setApplicationSettings(changedConfigName, changedConfigValue);
            // Reset Values
            isConfigChanged=false;
            changedConfigName=null;
            changedConfigValue=null;
        }

        super.onResume();
    }
}