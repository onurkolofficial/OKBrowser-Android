package com.onurkol.app.browser.activity.browser.settings;

import static com.onurkol.app.browser.data.settings.xml.LanguageXMLToList.getLanguageIconList;
import static com.onurkol.app.browser.data.settings.xml.LanguageXMLToList.getLanguageNameList;
import static com.onurkol.app.browser.data.settings.xml.LanguageXMLToList.getLanguageValueList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.installer.InstallerActivity;
import com.onurkol.app.browser.adapters.settings.SettingsPreferenceListWithIconAdapter;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.browser.BrowserDataInitController;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.data.settings.SettingXMLDataWithIcon;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.ActivityActionAnimator;

import java.util.ArrayList;

public class SettingsLanguageActivity extends AppCompatActivity implements BrowserDataInterface {
    BrowserDataInitController browserDataController;
    PreferenceController preferenceController;
    DayNightModeController dayNightController;
    LanguageController languageController;

    public static boolean isCreated;

    ImageButton backButton;
    TextView settingName;
    ListView settingsLanguageList;

    ArrayList<SettingXMLDataWithIcon> DATA_LIST=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ContextController.setContext(this);

        preferenceController=PreferenceController.getController();
        browserDataController=BrowserDataInitController.getController();
        browserDataController.init();

        dayNightController=DayNightModeController.getController();
        languageController=LanguageController.getController();

        if(!browserDataController.isInstallerCompleted()){
            startActivity(new Intent(this, InstallerActivity.class));
            finish();
        }

        // Set Theme|Language
        dayNightController.setDayNightMode(this, preferenceController.getInt(KEY_DAY_NIGHT_MODE));
        languageController.setLanguage(this, preferenceController.getInt(KEY_LANGUAGE));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_language);

        backButton=findViewById(R.id.settingsBackButton);
        settingName=findViewById(R.id.settingsTitle);
        settingsLanguageList=findViewById(R.id.settingsLanguageList);

        settingName.setText(getString(R.string.language_text));

        backButton.setOnClickListener(view -> ActivityActionAnimator.finish(this));

        // Set ListView Adapter
        settingsLanguageList.setAdapter(new SettingsPreferenceListWithIconAdapter(this, DATA_LIST, true));

        // Get Data
        ArrayList<String> xmlStringValue=getLanguageNameList(this);
        ArrayList<Integer> xmlIntegerValue=getLanguageValueList(this);
        TypedArray xmlDataIcons=getLanguageIconList(this);

        // Add Data
        for(int i=0; i<xmlStringValue.size(); i++)
            DATA_LIST.add(new SettingXMLDataWithIcon(xmlStringValue.get(i), xmlIntegerValue.get(i), xmlDataIcons.getDrawable(i), KEY_LANGUAGE));

        isCreated=true;
    }

    @Override
    public void onBackPressed() {
        ActivityActionAnimator.finish(this);
    }

    @Override
    protected void onDestroy() {
        isCreated=false;
        super.onDestroy();
    }
}