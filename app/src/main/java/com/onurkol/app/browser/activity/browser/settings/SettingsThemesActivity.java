package com.onurkol.app.browser.activity.browser.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.adapters.settings.DataCheckboxIconAdapter;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.data.installer.InstallerDataInteger;
import com.onurkol.app.browser.data.settings.SettingsPreferenceIconDataInteger;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.AppTheme;

import java.util.ArrayList;

public class SettingsThemesActivity extends AppCompatActivity {

    // Elements
    ImageButton backButton;
    TextView settingName;
    ListView settingsThemeList;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;

    ArrayList<SettingsPreferenceIconDataInteger> THEME_DATA_LIST=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_themes);

        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Get Elements
        backButton=findViewById(R.id.backSettingsButton);
        settingName=findViewById(R.id.settingName);
        settingsThemeList=findViewById(R.id.settingsThemeList);

        // Set Toolbar Title
        settingName.setText(getString(R.string.theme_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Set Adapter
        settingsThemeList.setAdapter(new DataCheckboxIconAdapter(this, settingsThemeList, THEME_DATA_LIST));

        // Get Data
        ArrayList<String> xmlStringValue=AppTheme.getInstance().getThemeNameList();
        ArrayList<Integer> xmlIntegerValue=AppTheme.getInstance().getThemeValueList();
        TypedArray xmlDataIcons = getResources().obtainTypedArray(R.array.app_themes_preference_icons);

        // Add Data
        for(int i=0; i<xmlStringValue.size(); i++){
            // API 29 and and oldest versions not supported System Theme.
            // Not add this option for API 29 and oldest.
            if (xmlIntegerValue.get(i)==2){
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                    THEME_DATA_LIST.add(new SettingsPreferenceIconDataInteger(xmlStringValue.get(i), xmlIntegerValue.get(i), xmlDataIcons.getDrawable(i), true, BrowserDefaultSettings.KEY_APP_THEME));
            }
            else
                THEME_DATA_LIST.add(new SettingsPreferenceIconDataInteger(xmlStringValue.get(i), xmlIntegerValue.get(i), xmlDataIcons.getDrawable(i), true, BrowserDefaultSettings.KEY_APP_THEME));
        }
    }
}