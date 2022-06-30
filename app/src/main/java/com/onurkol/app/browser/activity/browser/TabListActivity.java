package com.onurkol.app.browser.activity.browser;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.activity.installer.InstallerActivity;
import com.onurkol.app.browser.adapters.tabs.TabListPagerAdapter;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.browser.BrowserDataInitController;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.ActivityActionAnimator;

public class TabListActivity extends AppCompatActivity implements BrowserDataInterface {
    BrowserDataInitController browserDataController;
    PreferenceController preferenceController;
    DayNightModeController dayNightController;
    LanguageController languageController;
    TabController tabController;

    public static boolean isCreated;

    View toolbarTabListView;
    ImageButton settingsBackButton, tabListNewTabButton, tabListNewIncognitoTabButton;
    ViewPager2 tabListPager;

    // Pager Animation
    ArgbEvaluator argbEvaluator=new ArgbEvaluator();
    Integer[] animationBackground,animationControlColor;

    TypedValue typedValue = new TypedValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ContextController.setContext(this);

        preferenceController=PreferenceController.getController();
        browserDataController=BrowserDataInitController.getController();
        browserDataController.init();

        dayNightController=DayNightModeController.getController();
        languageController=LanguageController.getController();

        tabController=TabController.getController(this);

        if(!browserDataController.isInstallerCompleted()){
            startActivity(new Intent(this, InstallerActivity.class));
            finish();
        }

        // Set Theme|Language
        dayNightController.setDayNightMode(this, preferenceController.getInt(KEY_DAY_NIGHT_MODE));
        languageController.setLanguage(this, preferenceController.getInt(KEY_LANGUAGE));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_list);

        tabListPager=findViewById(R.id.tabListPager);
        settingsBackButton=findViewById(R.id.settingsBackButton);
        toolbarTabListView=findViewById(R.id.toolbarTabListView);
        tabListNewTabButton=findViewById(R.id.tabListNewTabButton);
        tabListNewIncognitoTabButton=findViewById(R.id.tabListNewIncognitoTabButton);

        // Get Theme Attributes (Background, colorControlNormal)
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        @ColorInt int themeBackground = typedValue.data;
        getTheme().resolveAttribute(android.R.attr.textColorHint, typedValue, true);
        @ColorInt int themeControlColor = typedValue.data;
        // Get Incognito Tabs Page Style (Background, colorControlNormal)
        int incognitoPageBackground=ContextCompat.getColor(this, R.color.incognito_list_background);
        int incognitoPageControlColor=ContextCompat.getColor(this, R.color.white);

        // Set Page Colors
        animationBackground = new Integer[]{
                themeBackground,
                incognitoPageBackground
        };
        animationControlColor = new Integer[]{
                themeControlColor,
                incognitoPageControlColor
        };

        settingsBackButton.setOnClickListener(v -> ActivityActionAnimator.finish(this));
        tabListNewTabButton.setOnClickListener(v -> {
            MainActivity.isNewTab=true;
            ActivityActionAnimator.finish(this);
        });
        tabListNewIncognitoTabButton.setOnClickListener(v -> {
            MainActivity.isNewIncognitoTab=true;
            ActivityActionAnimator.finish(this);
        });

        tabListPager.setAdapter(new TabListPagerAdapter(this));
        tabListPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int pageIndex=tabListPager.getAdapter().getItemCount() - 1;
                int colorIndex=animationBackground.length - 1;
                int backgroundColor,controlColor;
                if(position < pageIndex && position < colorIndex) {
                    int bgCol=(Integer) argbEvaluator.evaluate(positionOffset, animationBackground[position], animationBackground[position + 1]);
                    int ctrCol=(Integer) argbEvaluator.evaluate(positionOffset, animationControlColor[position], animationControlColor[position + 1]);
                    backgroundColor = bgCol;
                    controlColor = ctrCol;
                }
                else {
                    backgroundColor = animationBackground[colorIndex];
                    controlColor = animationControlColor[colorIndex];
                }
                // Set Background
                tabListPager.setBackgroundColor(backgroundColor);
                toolbarTabListView.setBackgroundColor(backgroundColor);
                // Set Control Color (Toolbar Buttons)
                ((ImageButton)toolbarTabListView.findViewById(R.id.tabListNewTabButton)).setColorFilter(controlColor, PorterDuff.Mode.SRC_ATOP);
                ((ImageButton)toolbarTabListView.findViewById(R.id.tabListNewIncognitoTabButton)).setColorFilter(controlColor, PorterDuff.Mode.SRC_ATOP);

                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });

        tabController.onBackupPointTabData(false);
        tabController.onBackupPointTabData(true);

        // Check Tabs
        if((tabController.getTabCount()<=0 && tabController.getIncognitoTabCount()>0) ||
                (tabController.getCurrentTab()!=null && tabController.getCurrentTab().isIncognito()))
            tabListPager.setCurrentItem(1);

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