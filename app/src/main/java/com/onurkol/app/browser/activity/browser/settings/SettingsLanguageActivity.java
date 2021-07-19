package com.onurkol.app.browser.activity.browser.settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.adapters.settings.DataCheckboxIconAdapter;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.data.settings.SettingsPreferenceIconDataInteger;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.settings.AppLanguage;

import java.util.ArrayList;

public class SettingsLanguageActivity extends AppCompatActivity {

    // Elements
    ImageButton backButton;
    TextView settingName;
    ListView settingsLanguageList;
    // Classes
    BrowserDataManager dataManager;
    AppPreferenceManager prefManager;

    ArrayList<SettingsPreferenceIconDataInteger> LANGUAGE_DATA_LIST=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set Current Activity Context
        ContextManager.Build(this);
        // Get Classes
        dataManager=new BrowserDataManager();
        prefManager=AppPreferenceManager.getInstance();
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_language);

        // Get Elements
        backButton=findViewById(R.id.backSettingsButton);
        settingName=findViewById(R.id.settingName);
        settingsLanguageList=findViewById(R.id.settingsLanguageList);

        // Set Toolbar Title
        settingName.setText(getString(R.string.language_text));

        // Button Click Events
        backButton.setOnClickListener(view -> finish());

        // Set Adapter
        settingsLanguageList.setAdapter(new DataCheckboxIconAdapter(this, settingsLanguageList, LANGUAGE_DATA_LIST));

        // Get Data
        ArrayList<String> xmlStringValue=AppLanguage.getInstance().getLanguageNameList();
        ArrayList<Integer> xmlIntegerValue=AppLanguage.getInstance().getLanguageValueList();
        TypedArray xmlDataIcons = getResources().obtainTypedArray(R.array.app_languages_preference_icons);

        // Add Data
        for(int i=0; i<xmlStringValue.size(); i++){
            LANGUAGE_DATA_LIST.add(new SettingsPreferenceIconDataInteger(xmlStringValue.get(i),xmlIntegerValue.get(i),xmlDataIcons.getDrawable(i), true, BrowserDefaultSettings.KEY_APP_LANGUAGE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // Init Browser Data ( Applying View Settings )
        dataManager.initBrowserPreferenceSettings();
        return super.onCreateView(name, context, attrs);
    }
}