package com.onurkol.app.browser.activity.installer;

import static com.onurkol.app.browser.libs.ActivityActionAnimator.startAndFinishActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.os.Bundle;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.adapters.installer.InstallerPagerAdapter;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;

public class InstallerActivity extends AppCompatActivity implements BrowserDataInterface {
    private PreferenceController preferenceController;
    private DayNightModeController dayNightController;
    private LanguageController languageController;

    boolean isConfigChanged;

    private final String KEY_INSTALLER_CURRENT_STEP="PAGER_CURRENT_STEP";

    ViewPager2 installerPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ContextController.setContext(this);

        preferenceController=PreferenceController.getController();
        dayNightController=DayNightModeController.getController();
        languageController=LanguageController.getController();

        // Set Theme|Language
        dayNightController.setDayNightMode(this, preferenceController.getInt(KEY_DAY_NIGHT_MODE));
        languageController.setLanguage(this, preferenceController.getInt(KEY_LANGUAGE));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installer);

        installerPager=findViewById(R.id.installerPager);

        // Disabled pager touch scroll.
        installerPager.setUserInputEnabled(false);
        // Set Pager Adapter
        installerPager.setAdapter(new InstallerPagerAdapter(this));

        // Check getExtras to send data.
        if(getIntent().getExtras()!=null) {
            installerPager.setCurrentItem(getIntent().getExtras().getInt(KEY_INSTALLER_CURRENT_STEP));
        }
    }

    /*
     * Recreate Alternative:
     * Change animation exist.
     * if call the below code to add 'android:configChanges="uiMode"' on Manifest.
    @Override
    public void recreate() {
        // Recreate for Theme|Locale
        // Sending pager active step
        getIntent().putExtra(KEY_INSTALLER_CURRENT_STEP,installerPager.getCurrentItem());

        startAndFinishActivity(this, getIntent());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isConfigChanged=true;

        dayNightController.onConfigChanges(this);
    }
     */

    @Override
    public void onBackPressed() {
        if(installerPager.getCurrentItem()>0)
            installerPager.setCurrentItem(installerPager.getCurrentItem()-1);
        else
            super.onBackPressed();
    }
}