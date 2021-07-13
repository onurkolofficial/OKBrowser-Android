package com.onurkol.app.browser.activity.browser.installer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.adapters.installer.InstallerPagerAdapter;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.lib.ContextManager;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class InstallerActivity extends AppCompatActivity {
    // Elements
    ViewPager2 installerPager;
    // Static Elements
    public static WeakReference<ViewPager2> installerPagerStatic;
    // Classes
    BrowserDataManager dataManager;
    // Intents
    static Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installer);
        // Building ContextManager
        ContextManager.Build(this);

        // Get Classes
        dataManager=new BrowserDataManager();

        // Get Elements
        installerPager=findViewById(R.id.installerPager);
        // Set Static Elements
        installerPagerStatic=new WeakReference<>(installerPager);

        // Get Intents
        mainIntent=new Intent(this, MainActivity.class);

        // Set Pager Adapter
        installerPager.setAdapter(new InstallerPagerAdapter(this));
        installerPager.setUserInputEnabled(false);
    }

    @Override
    protected void onStart() {
        dataManager.initBrowserPreferenceSettings();
        super.onStart();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(installerPager.getCurrentItem()>0)
            installerPager.setCurrentItem(installerPager.getCurrentItem()-1);
        else
            super.onBackPressed();
    }
}