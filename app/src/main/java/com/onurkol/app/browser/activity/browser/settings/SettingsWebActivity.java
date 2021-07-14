package com.onurkol.app.browser.activity.browser.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.fragments.SettingsFragment;
import com.onurkol.app.browser.fragments.settings.SettingsWebFragment;
import com.onurkol.app.browser.lib.AppPreferenceManager;

public class SettingsWebActivity extends AppCompatActivity {

    // Elements
    ImageButton backButton;
    TextView settingName;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_web);

        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Get Elements
        backButton=findViewById(R.id.backSettingsButton);
        settingName=findViewById(R.id.settingName);

        // Set Toolbar Title
        settingName.setText(getString(R.string.web_settings_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Get Fragment
        getSupportFragmentManager().beginTransaction().add(R.id.settingsFragmentContent,new SettingsWebFragment()).commit();
    }
}