package com.onurkol.app.browser.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.core.TabListViewActivity;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.data.tabs.IncognitoTabData;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.IncognitoTabFragment;
import com.onurkol.app.browser.fragments.tabs.TabFragment;
import com.onurkol.app.browser.fragments.tabs.list.IncognitoTabListFragment;
import com.onurkol.app.browser.fragments.tabs.list.TabListFragment;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.SearchEngine;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.lib.tabs.core.ActivityTabSignal;
import com.onurkol.app.browser.lib.tabs.core.ToolbarTabCounter;
import com.onurkol.app.browser.menu.MenuToolbarMain;
import com.onurkol.app.browser.menu.MenuToolbarNoTab;
import com.onurkol.app.browser.tools.KeyboardController;
import com.onurkol.app.browser.tools.ProcessDelay;
import com.onurkol.app.browser.tools.URLChecker;
import com.onurkol.app.browser.webview.OKWebView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // Classes
    BrowserDataManager dataManager;
    TabBuilder tabBuilder;
    ToolbarTabCounter tabCounter;
    // Elements
    SwipeRefreshLayout swipeRefresh;
    ProgressBar toolbarProgressBar;
    ImageView incognitoIcon;
    ImageButton browserTabListButton, browserMenuButton, noTabMenuButton, noTabNewTabButton, noTabNewIncognitoButton;
    LinearLayout browserFragmentView;
    EditText browserSearch;
    // Static Element Copy
    static WeakReference<ImageButton> browserTabListButtonStatic;
    static WeakReference<ImageView> incognitoIconStatic;
    static WeakReference<EditText> browserSearchStatic;
    // Intents
    Intent tabListIntent,welcomeIntent;
    // Variables
    boolean backPressHomeLayout=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Building ContextManager
        ContextManager.Build(this);
        // Get Classes
        dataManager=new BrowserDataManager();
        tabBuilder=TabBuilder.Build();
        tabCounter=new ToolbarTabCounter();

        // Load Application/Browser Data
        dataManager.initBrowserSettings();

        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Elements
        swipeRefresh=findViewById(R.id.browserSwipeRefresh);
        toolbarProgressBar=findViewById(R.id.browserProgressbar);
        browserTabListButton=findViewById(R.id.browserTabListButton);
        incognitoIcon=findViewById(R.id.incognitoIcon);
        browserMenuButton=findViewById(R.id.browserMenuButton);
        noTabMenuButton=findViewById(R.id.noTabMenuButton);
        browserFragmentView=findViewById(R.id.browserFragmentView);
        noTabNewTabButton=findViewById(R.id.noTabNewTabButton);
        browserSearch=findViewById(R.id.browserSearch);
        noTabNewIncognitoButton=findViewById(R.id.noTabNewIncognitoTabButton);
        // Get Static Copy
        browserTabListButtonStatic=new WeakReference<>(browserTabListButton);
        incognitoIconStatic=new WeakReference<>(incognitoIcon);
        browserSearchStatic=new WeakReference<>(browserSearch);

        // Get Intents
        tabListIntent=new Intent(this, TabListViewActivity.class);
        welcomeIntent=new Intent(this, InstallerActivity.class);

        // Disable Swipe Refresh (Default)
        swipeRefresh.setEnabled(false);
        // Hide Toolbar Progressbar (Default)
        toolbarProgressBar.setVisibility(View.GONE);
        // Hide Incognito Icon for toolbar_main.
        incognitoIcon.setVisibility(View.GONE);

        // Set Listeners
        browserTabListButton.setOnClickListener(showTabsClickListener);
        browserMenuButton.setOnClickListener(showMainMenuListener);
        noTabMenuButton.setOnClickListener(showNoTabMenuListener);
        noTabNewTabButton.setOnClickListener(createNewTabNoTabLayout);
        browserSearch.setOnKeyListener(searchWebUrlListener);
        noTabNewIncognitoButton.setOnClickListener(createNewIncognitoTabLayout);

        // Check Installer Activity
        if(dataManager.startInstallerActivity){
            // Start Welcome Activity
            startActivity(welcomeIntent);
            // Finish Current Activity
            finish();
        }
        else {
            // Continue
            // Check Saved Tabs
            if (tabBuilder.getSavedTabList().size() <= 0)
                // Create New Tab.
                tabBuilder.createNewTab();
            else
                // Synchronize Tabs
                tabBuilder.syncSavedTabs();
            // Update Tab Counts
            browserTabListButton.setImageDrawable(tabCounter.getTabCountDrawable());
        }
    }

    @Override
    protected void onResume() {
        // <BUG> Init Data onResume for Set Language Setting
        // Load Application/Browser Data
        dataManager.initBrowserSettings();
        // Re-Building ContextManager
        ContextManager.Build(this);
        View toolbarNoTab=(View)findViewById(R.id.includeNoTabToolbar);
        View toolbarMain=(View)findViewById(R.id.includeTabToolbar);

        // Reset Variables
        backPressHomeLayout=false;
        // Variables
        ArrayList<TabFragment> tabFragmentList=tabBuilder.getTabFragmentList();
        ArrayList<TabData> tabDataList=tabBuilder.getTabDataList();
        List<Integer> changeList=TabListFragment.changedIndexList;

        ArrayList<IncognitoTabFragment> incognitoFragmentList=tabBuilder.getIncognitoTabFragmentList();
        ArrayList<IncognitoTabData> incognitoDataList=tabBuilder.getIncognitoTabDataList();
        List<Integer> changeIncognitoList=IncognitoTabListFragment.changedIndexList;

        // **
        // * Check Closed Tabs.
        // * 1- If all tabs closed, show the 'No Tab Toolbar'.
        // * 2- If tabs closed at TabListActivity, remove views(Fragments) at MainActivity.onResume()
        // **
        // * 1 & 2 valid for Incognito Tabs
        // **
        // Check Tabs
        if(TabListFragment.isChanged || IncognitoTabListFragment.isChanged){
            // Check Exists Tab
            if (tabDataList.size() <= 0) {
                // TEST
                tabBuilder.getClassesTabDataList().clear();

                if (incognitoDataList.size() <= 0) {
                    // (Closed All Tabs)
                    // Change Toolbar
                    toolbarNoTab.setVisibility(View.VISIBLE);
                    toolbarMain.setVisibility(View.GONE);
                    // Remove Views
                    browserFragmentView.removeAllViews();
                }
                else {
                    // Show Incognito Tab
                    tabBuilder.changeIncognitoTab(0);
                    // Show Incognito Icon
                    incognitoIconStatic.get().setVisibility(View.VISIBLE);
                }
            }
            else {
                // Check Toolbar
                if (toolbarMain.getVisibility() == View.GONE) {
                    toolbarNoTab.setVisibility(View.GONE);
                    toolbarMain.setVisibility(View.VISIBLE);
                }
                // Remove Closed Tabs View (Check Closed *Tabs)
                int changedListSize = changeList.size();
                for (int i = 0; i < changedListSize; i++) {
                    int index = changeList.get(i);
                    // Get Fragments
                    TabFragment frag = tabFragmentList.get(index);
                    // Destroy WebView
                    frag.getWebView().destroy();
                    // Remove View
                    tabBuilder.removeFragment(frag);
                    // Update Data List
                    tabBuilder.getTabFragmentList().remove(index);
                }
                // Reset Index
                TabListFragment.changedIndexList.clear();
                // Remove Closed Tabs View (Check Closed *Incognito Tabs)
                int changedIncognitoListSize = changeIncognitoList.size();
                for (int i = 0; i < changedIncognitoListSize; i++) {
                    int index = changeIncognitoList.get(i);
                    // Get Fragments
                    IncognitoTabFragment frag = incognitoFragmentList.get(index);
                    // Destroy WebView
                    frag.getWebView().destroy();
                    // Remove View
                    tabBuilder.removeFragment(frag);
                    // Update Data List
                    tabBuilder.getIncognitoTabFragmentList().remove(index);
                }
                // Check Views
                if ((incognitoFragmentList.size() > 0) && (tabFragmentList.size() <= 0))
                    tabBuilder.changeIncognitoTab(0);
                else if ((tabFragmentList.size() > 0) && (incognitoFragmentList.size() <= 0)) {
                    tabBuilder.changeTab(0);
                    // Hide Incognito Icon
                    incognitoIcon.setVisibility(View.GONE);
                }
                // Reset Index
                IncognitoTabListFragment.changedIndexList.clear();
            }
            // Sync Tab Preferences.
            tabBuilder.saveTabListPreference(tabBuilder.getTabDataList());
            // Reset Variables
            TabListFragment.isChanged = false;
            IncognitoTabListFragment.isChanged = false;
        }

        // Update Tab Count
        // Check Active Tab Type (Tab or Incognito Tab)
        if(tabBuilder.getActiveTabFragment()!=null) {
            if (tabBuilder.getTabFragmentList().size() > 0)
                // Re-Update Tab Counts
                browserTabListButton.setImageDrawable(tabCounter.getTabCountDrawable());
        }
        else{
            if (tabBuilder.getIncognitoTabFragmentList().size() > 0)
                // Re-Update Tab Counts
                browserTabListButton.setImageDrawable(tabCounter.getIncognitoTabCountDrawable());
        }
        super.onResume();
    }

    public static void sendTabSignal(ActivityTabSignal signal){
        TabBuilder tabBuilder=TabBuilder.Build();
        // Runnable
        Runnable checkSignalRunnable=() -> {
            // Variables
            Drawable TabCountDrawable;
            // Check Signal Status
            // Check Incognito Tab Type
            if(!signal.getTabIsIncognito()) {
                // Check Incognito Icon
                if(incognitoIconStatic.get().getVisibility()==View.VISIBLE)
                    incognitoIconStatic.get().setVisibility(View.GONE);
                // Check Tab Events
                if (signal.getSignalStatus() == signal.TAB_ON_CHANGE)
                    tabBuilder.changeTab(signal.getSignalData().tab_position);
                else if (signal.getSignalStatus() == signal.TAB_ON_CREATE)
                    tabBuilder.createNewTab();
                // Get Count Drawable
                TabCountDrawable=new ToolbarTabCounter().getTabCountDrawable();
                // Get Tab Web Url
                browserSearchStatic.get().setText(tabBuilder.getTabDataList().get(signal.getSignalData().tab_position).getUrl());
            }
            else{
                // Check Incognito Icon
                if(incognitoIconStatic.get().getVisibility()==View.GONE)
                    incognitoIconStatic.get().setVisibility(View.VISIBLE);
                // Check Tab Events
                if(signal.getSignalStatus()==signal.INCOGNITO_ON_CHANGE)
                    tabBuilder.changeIncognitoTab(signal.getSignalData().tab_position);
                else if (signal.getSignalStatus() == signal.INCOGNITO_ON_CREATE)
                    tabBuilder.createNewIncognitoTab();
                // Get Count Drawable
                TabCountDrawable=new ToolbarTabCounter().getIncognitoTabCountDrawable();
                // Get Tab Web Url
                browserSearchStatic.get().setText(tabBuilder.getIncognitoTabDataList().get(signal.getSignalData().tab_position).getUrl());
            }
            // Update Tab Counts for Delay
            browserTabListButtonStatic.get().setImageDrawable(TabCountDrawable);
        };
        // Exec Runnable
        ProcessDelay.Delay(checkSignalRunnable, 100);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBackPressed() {
        // Elements
        View fragmentView;
        OKWebView webView;
        // Check Url Back, Layout & Tabs
        if(tabBuilder.getActiveTabFragment()!=null){
            // Check Tab WebView Layout
            // Get WebView & Fragment View
            fragmentView=tabBuilder.getActiveTabFragment().getView();
            webView=tabBuilder.getActiveTabWebView();
            // Check Exists WebView History
            if(webView.canGoBack()) {
                webView.goBack();
            }
            else{
                // Sync Tab Data
                webView.syncOnBack("");
                // Hide WebView & Show Page Layout
                webView.setVisibility(View.GONE);
                (fragmentView.findViewById(R.id.newTabHomeLayout)).setVisibility(View.VISIBLE);
                if(backPressHomeLayout)
                    super.onBackPressed();
                else
                    backPressHomeLayout=true;
            }
        }
        else{
            // Check Incognito WebView Layout
            fragmentView=tabBuilder.getActiveIncognitoFragment().getView();
            webView=tabBuilder.getActiveIncognitoWebView();
            // Check Exists WebView History
            if(webView.canGoBack())
                webView.goBack();
            else{
                // Hide WebView & Show Page Layout
                webView.setVisibility(View.GONE);
                (fragmentView.findViewById(R.id.incognitoHomeLayout)).setVisibility(View.VISIBLE);
                if(backPressHomeLayout)
                    super.onBackPressed();
                else
                    backPressHomeLayout=true;
            }
        }
    }

    // Event Listeners
    // Show Tabs
    View.OnClickListener showTabsClickListener=view -> {
        startActivity(tabListIntent);
    };
    // Open Toolbar Main Menu
    View.OnClickListener showMainMenuListener=view -> {
        MenuToolbarMain.getMenu().showAsDropDown(view);
    };
    // Open Toolbar No Tab Menu
    View.OnClickListener showNoTabMenuListener=view -> {
        MenuToolbarNoTab.getMenu().showAsDropDown(view);
    };
    // Create New Tab in No Tab Toolbar
    View.OnClickListener createNewTabNoTabLayout=view -> {
        // Get Elements
        View toolbarNoTab=(View)findViewById(R.id.includeNoTabToolbar);
        View toolbarMain=(View)findViewById(R.id.includeTabToolbar);
        // Hide No Tab Toolbar
        toolbarNoTab.setVisibility(View.GONE);
        toolbarMain.setVisibility(View.VISIBLE);
        // Create Tab
        tabBuilder.createNewTab();
        // Hide Incognito Icon
        incognitoIcon.setVisibility(View.GONE);
        // Re-Update Icon
        browserTabListButton.setImageDrawable(new ToolbarTabCounter().getTabCountDrawable());
        // Clear Url for New Tab
        browserSearch.setText("");
    };
    // Create New Incognito Tab in No Tab Toolbar
    View.OnClickListener createNewIncognitoTabLayout=view -> {
        // Get Elements
        View toolbarNoTab=(View)findViewById(R.id.includeNoTabToolbar);
        View toolbarMain=(View)findViewById(R.id.includeTabToolbar);
        // Hide No Tab Toolbar
        toolbarNoTab.setVisibility(View.GONE);
        toolbarMain.setVisibility(View.VISIBLE);
        // Create Tab
        tabBuilder.createNewIncognitoTab();
        // Show Incognito Icon
        incognitoIcon.setVisibility(View.VISIBLE);
        // Re-Update Icon
        browserTabListButton.setImageDrawable(new ToolbarTabCounter().getIncognitoTabCountDrawable());
        // Clear Url for New Tab
        browserSearch.setText("");
    };

    // Key Listener for toolbar Search Url
    View.OnKeyListener searchWebUrlListener=(view, i, keyEvent) -> {
        OKWebView webView;
        LinearLayout webLayout;
        // Get Fragment View (Normal or Incognito)
        View fragmentView;
        if(tabBuilder.getActiveTabFragment()!=null){
            // Get View
            fragmentView = tabBuilder.getActiveTabFragment().getView();
            // Get Elements
            webView=fragmentView.findViewById(R.id.okBrowserWebView);
            webLayout=fragmentView.findViewById(R.id.newTabHomeLayout);
        }
        else {
            // Get View
            fragmentView = tabBuilder.getActiveIncognitoFragment().getView();
            // Get Elements
            webView=fragmentView.findViewById(R.id.okBrowserIncognitoWebView);
            webLayout=fragmentView.findViewById(R.id.incognitoHomeLayout);
        }

        // Check Url
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
            // Hide Tab Page Layout
            webLayout.setVisibility(View.GONE);
            // Show WebView
            webView.setVisibility(View.VISIBLE);
            // WebView Load:
            String loadPage;
            String getUrl=browserSearch.getText().toString();
            if(URLChecker.isURL(getUrl)){
                if(getUrl.startsWith(URLChecker.URL_SCHEME_VIEW_SOURCE))
                    loadPage=URLChecker.convertViewSourceUrl(getUrl);
                else
                    loadPage=URLChecker.convertHttpUrl(browserSearch.getText().toString());
            }
            else{
                // Get Search Engine Query
                String seQuery=SearchEngine.getInstance().getSearchEngineQuery();
                // Convert Url
                loadPage=seQuery+getUrl;
            }
            // Load Url
            webView.loadUrl(loadPage);
            // Clear Search
            ((EditText)view).setText("");
            // Hide Keyboard
            KeyboardController.hideKeyboard(view);
        }
        return false;
    };
}