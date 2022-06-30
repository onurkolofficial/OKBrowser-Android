package com.onurkol.app.browser.activity.browser.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.activity.installer.InstallerActivity;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.browser.BrowserDataInitController;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.ActivityActionAnimator;

public class SettingsGuiActivity extends AppCompatActivity implements BrowserDataInterface {
    BrowserDataInitController browserDataController;
    PreferenceController preferenceController;
    DayNightModeController dayNightController;
    LanguageController languageController;

    public static boolean isCreated;

    ImageButton backButton;
    TextView settingName;
    LinearLayout simpleGuiLayoutButton, denseGuiLayoutButton;

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
        setContentView(R.layout.activity_settings_gui);

        backButton=findViewById(R.id.settingsBackButton);
        settingName=findViewById(R.id.settingsTitle);
        simpleGuiLayoutButton=findViewById(R.id.simpleGuiLayoutButton);
        denseGuiLayoutButton=findViewById(R.id.denseGuiLayoutButton);

        settingName.setText(getString(R.string.gui_text));

        backButton.setOnClickListener(view -> ActivityActionAnimator.finish(this));

        // Check Values in 'res/browser/values/gui_modes.xml' (copy code: InstallerGUIFragment)
        simpleGuiLayoutButton.setOnClickListener(v -> {
            preferenceController.setPreference(BrowserDataInterface.KEY_GUI_MODE, GUI_MODE_SIMPLE);
            resetLayoutButtons();
            setSettingState();
        });
        denseGuiLayoutButton.setOnClickListener(v -> {
            preferenceController.setPreference(BrowserDataInterface.KEY_GUI_MODE, GUI_MODE_DENSE);
            resetLayoutButtons();
            setSettingState();
        });

        resetLayoutButtons();
        isCreated=true;
    }

    private void setSettingState(){
        MainActivity.isSettingChanged=true;
    }

    private void resetLayoutButtons(){
        TypedValue themedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.appcompat.R.attr.selectableItemBackground, themedValue, true);
        simpleGuiLayoutButton.setBackgroundResource(themedValue.resourceId);
        denseGuiLayoutButton.setBackgroundResource(themedValue.resourceId);
        int defaultGui=preferenceController.getInt(BrowserDataInterface.KEY_GUI_MODE);
        if(defaultGui==GUI_MODE_SIMPLE)
            simpleGuiLayoutButton.setBackground(AppCompatResources.getDrawable(this, R.drawable.layout_select_border));
        else if(defaultGui==GUI_MODE_DENSE)
            denseGuiLayoutButton.setBackground(AppCompatResources.getDrawable(this, R.drawable.layout_select_border));
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