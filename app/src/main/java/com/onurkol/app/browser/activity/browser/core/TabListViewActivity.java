package com.onurkol.app.browser.activity.browser.core;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.MainActivity;
import com.onurkol.app.browser.adapters.browser.tabs.TabListsPageAdapter;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.lib.browser.tabs.core.ActivityTabSignal;

public class TabListViewActivity extends AppCompatActivity {
    // Elements
    ViewPager2 tabListPager;
    Toolbar toolbarTabList;
    ImageButton tabListNewTabButton, tabListNewIncognitoTabButton;

    // Pager Animation
    ArgbEvaluator argbEvaluator=new ArgbEvaluator();
    Integer[] animationBackground,animationControlColor;

    TypedValue typedValue = new TypedValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_list_view);
        // Building ContextManager
        ContextManager.Build(this);

        // Get Elements
        tabListPager = findViewById(R.id.tabListPager);
        toolbarTabList = findViewById(R.id.toolbarTabList);
        tabListNewTabButton = findViewById(R.id.tabListNewTabButton);
        tabListNewIncognitoTabButton=findViewById(R.id.tabListNewIncognitoTabButton);

        // Get Theme Attribute (Background)
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        @ColorInt int themeBackground = typedValue.data;
        // Incognito Tabs Page Background
        int incognitoPageBackground=ContextCompat.getColor(this, R.color.incognito_list_background);
        // Get Theme Attribute (Control Color)
        getTheme().resolveAttribute(R.attr.imageButtonStyle, typedValue, true);
        @ColorInt int themeControlColor = typedValue.data;
        // Incognito Tabs Page Control Color
        int incognitoPageControlColor=ContextCompat.getColor(this, R.color.white);

        // Page Colors
        animationBackground = new Integer[]{
                themeBackground,
                incognitoPageBackground
        };
        animationControlColor = new Integer[]{
                themeControlColor,
                incognitoPageControlColor
        };
        // Click Events
        toolbarTabList.setOnTouchListener(clickToCloseListener);
        tabListNewTabButton.setOnClickListener(clickNewTabListener);
        tabListNewIncognitoTabButton.setOnClickListener(clickNewIncognitoTabListener);

        // Set Pager Adapter
        tabListPager.setAdapter(new TabListsPageAdapter(this));
        tabListPager.registerOnPageChangeCallback(pageChangeCb);

        // Check Start Page
        TabBuilder tabBuilder=TabBuilder.Build();

        if(tabBuilder.getTabDataList().size()<=0)
            // Show Incognito Tabs
            tabListPager.setCurrentItem(1);
    }

    // Listeners
    View.OnTouchListener clickToCloseListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // Close Activity
            finish();
            return false;
        }
    };
    View.OnClickListener clickNewTabListener=view -> {
        // Close This Activity
        finish();
        // Send Tab Signal
        sendTabSignal(ActivityTabSignal.TAB_ON_CREATE, false);
    };
    View.OnClickListener clickNewIncognitoTabListener=view -> {
        // Close This Activity
        finish();
        // Send Tab Signal
        sendTabSignal(ActivityTabSignal.INCOGNITO_ON_CREATE, true);
    };

    // Tab Signals for Listeners
    private void sendTabSignal(int tabSignalCode, boolean incognitoMode){
        // Send Activity Status
        // Create Tab Signal
        ActivityTabSignal tabSignal=new ActivityTabSignal();
        ActivityTabSignal.TabSignalData signalData=new ActivityTabSignal.TabSignalData();
        // Set Status
        tabSignal.setSignalStatus(tabSignalCode);
        // Check Data
        if(tabSignalCode==ActivityTabSignal.TAB_ON_CREATE ||
                tabSignalCode==ActivityTabSignal.INCOGNITO_ON_CREATE)
            signalData.tab_url="";
        // Send Data
        tabSignal.setSignalData(signalData);
        tabSignal.setTabIsIncognito(incognitoMode);
        // Send Signal
        MainActivity.sendTabSignal(tabSignal);
    }

    // Pager onPageChange
    ViewPager2.OnPageChangeCallback pageChangeCb=new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int pageIndex= TabListsPageAdapter.pageCount - 1;
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
            toolbarTabList.setBackgroundColor(backgroundColor);
            // Set Control Color (Toolbar Buttons)
            ((ImageButton)toolbarTabList.findViewById(R.id.tabListNewTabButton)).setColorFilter(controlColor, PorterDuff.Mode.SRC_ATOP);
            ((ImageButton)toolbarTabList.findViewById(R.id.tabListNewIncognitoTabButton)).setColorFilter(controlColor, PorterDuff.Mode.SRC_ATOP);

            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    };
}