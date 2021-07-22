package com.onurkol.app.browser.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
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
    public static boolean isCreated=false,isCreatedView=false,isConfigChanged=false,isCloseActivity=false;
    public static String changedConfigName;
    public static Integer changedConfigValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
        if(!SettingsFragment.isPreferenceFragmentCreated)
            getSupportFragmentManager().beginTransaction().add(R.id.settingsFragmentContent, new SettingsFragment()).commit();

        isCreated=true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // Init Browser Data ( Applying View Settings )
        if(!dataManager.startInstallerActivity){
            dataManager.initBrowserPreferenceSettings();
            /*
            if(!isCreatedView) {
                //...
            }
             */
            isCreatedView=true;
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        // Re-Building ContextManager
        ContextManager.Build(this);

        if(isCloseActivity){
            isCloseActivity=false;
            finish();
        }

        if(isConfigChanged) {
            MainActivity.isConfigChanged=true;
            if(changedConfigName!=null && changedConfigValue!=null)
                dataManager.setApplicationSettings(changedConfigName, changedConfigValue);
            // Reset Values
            isConfigChanged=false;
            changedConfigName=null;
            changedConfigValue=null;
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCreated=false;
        SettingsFragment.isPreferenceFragmentCreated=false;
    }
}