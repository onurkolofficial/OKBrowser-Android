package com.onurkol.app.browser.activity.browser.installer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.adapters.installer.InstallerPagerAdapter;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.LanguageManager;

import java.lang.ref.WeakReference;

public class InstallerActivity extends AppCompatActivity {
    // Elements
    ViewPager2 installerPager;
    // Static Elements
    public static WeakReference<ViewPager2> installerPagerStatic;
    // Classes
    static WeakReference<BrowserDataManager> dataManagerStatic;
    // Intents
    static Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installer);
        // Building ContextManager
        ContextManager.Build(this);
        // Get Classes
        dataManagerStatic=new WeakReference<>(new BrowserDataManager());

        // Init Settings
        dataManagerStatic.get().initBrowserSettings();

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