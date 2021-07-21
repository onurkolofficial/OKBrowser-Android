package com.onurkol.app.browser.activity.browser.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.fragments.settings.SettingsWebFragment;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;

public class SettingsWebActivity extends AppCompatActivity {

    // Elements
    ImageButton backButton;
    TextView settingName;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_web);

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

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // Init Browser Data ( Applying View Settings )
        dataManager.initBrowserPreferenceSettings();
        return super.onCreateView(name, context, attrs);
    }
}