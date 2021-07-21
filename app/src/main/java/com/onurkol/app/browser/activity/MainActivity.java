package com.onurkol.app.browser.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.core.TabListViewActivity;
import com.onurkol.app.browser.activity.browser.installer.InstallerActivity;
import com.onurkol.app.browser.data.BrowserDataManager;
import com.onurkol.app.browser.data.browser.tabs.IncognitoTabData;
import com.onurkol.app.browser.data.browser.tabs.TabData;
import com.onurkol.app.browser.fragments.browser.tabs.IncognitoTabFragment;
import com.onurkol.app.browser.fragments.browser.tabs.TabFragment;
import com.onurkol.app.browser.fragments.browser.tabs.list.IncognitoTabListFragment;
import com.onurkol.app.browser.fragments.browser.tabs.list.TabListFragment;
import com.onurkol.app.browser.interfaces.BrowserActionKeys;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.downloads.DownloadsHelper;
import com.onurkol.app.browser.lib.settings.SearchEngine;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.lib.browser.tabs.core.ActivityTabSignal;
import com.onurkol.app.browser.lib.browser.tabs.core.ToolbarTabCounter;
import com.onurkol.app.browser.menu.MenuToolbarMain;
import com.onurkol.app.browser.menu.MenuToolbarNoTab;
import com.onurkol.app.browser.tools.KeyboardController;
import com.onurkol.app.browser.tools.ProcessDelay;
import com.onurkol.app.browser.tools.URLChecker;
import com.onurkol.app.browser.webview.OKWebView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BrowserActionKeys {
    // Classes
    BrowserDataManager dataManager;
    TabBuilder tabBuilder;
    ToolbarTabCounter tabCounter;
    // Elements
    SwipeRefreshLayout browserSwipeRefresh;
    ProgressBar toolbarProgressBar;
    ImageView incognitoIcon;
    ImageButton browserTabListButton, browserMenuButton, noTabMenuButton, noTabNewTabButton, noTabNewIncognitoButton,
            findCloseButton, findNextButton, findBackButton;
    LinearLayout browserFragmentView;
    EditText browserSearch;
    SearchView findPageView;
    // Static Element Copy
    public static WeakReference<ImageButton> browserTabListButtonStatic;
    public static WeakReference<ImageView> incognitoIconStatic;
    static WeakReference<EditText> browserSearchStatic;
    static WeakReference<SwipeRefreshLayout> browserSwipeRefreshStatic;
    // Intents
    Intent tabListIntent,installerIntent;
    public static Intent updatedIntent=null;
    // Variables
    public static boolean isCreated=false,isCreatedView=false,isConfigChanged=false;
    boolean backPressHomeLayout=false;
    String findQueryString="";

    // Update Manager
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Building ContextManager
        // .BuildBase only MainActivity.
        ContextManager.BuildBase(this);
        // Get Classes
        dataManager=new BrowserDataManager();
        tabBuilder=TabBuilder.Build();
        tabCounter=new ToolbarTabCounter();

        // Create View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init Browser Data
        dataManager.initBrowserDataClasses();

        // Get Elements
        browserSwipeRefresh=findViewById(R.id.browserSwipeRefresh);
        toolbarProgressBar=findViewById(R.id.browserProgressbar);
        browserTabListButton=findViewById(R.id.browserTabListButton);
        incognitoIcon=findViewById(R.id.incognitoIcon);
        browserMenuButton=findViewById(R.id.browserMenuButton);
        noTabMenuButton=findViewById(R.id.noTabMenuButton);
        browserFragmentView=findViewById(R.id.browserFragmentView);
        noTabNewTabButton=findViewById(R.id.noTabNewTabButton);
        browserSearch=findViewById(R.id.browserSearch);
        noTabNewIncognitoButton=findViewById(R.id.noTabNewIncognitoTabButton);
        findCloseButton=findViewById(R.id.findCloseButton);
        findNextButton=findViewById(R.id.findNextButton);
        findBackButton=findViewById(R.id.findBackButton);
        findPageView=findViewById(R.id.browserFindView);
        // Get Static Copy
        browserTabListButtonStatic=new WeakReference<>(browserTabListButton);
        incognitoIconStatic=new WeakReference<>(incognitoIcon);
        browserSearchStatic=new WeakReference<>(browserSearch);
        browserSwipeRefreshStatic=new WeakReference<>(browserSwipeRefresh);

        // Get Intents
        tabListIntent=new Intent(this, TabListViewActivity.class);
        installerIntent=new Intent(this, InstallerActivity.class);

        // Hide Toolbar Progressbar (Default)
        toolbarProgressBar.setVisibility(View.GONE);
        // Hide Incognito Icon for toolbar_main.
        incognitoIcon.setVisibility(View.GONE);

        // Find Page
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        findPageView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Set Listeners
        browserTabListButton.setOnClickListener(showTabsClickListener);
        browserMenuButton.setOnClickListener(showMainMenuListener);
        noTabMenuButton.setOnClickListener(showNoTabMenuListener);
        noTabNewTabButton.setOnClickListener(view -> activityNewTabHandler(""));
        browserSearch.setOnKeyListener(searchWebUrlListener);
        noTabNewIncognitoButton.setOnClickListener(createNewIncognitoTabLayout);
        findCloseButton.setOnClickListener(closeFindListener);
        findPageView.setOnQueryTextListener(findQueryTextListener);
        findNextButton.setOnClickListener(findNextButtonListener);
        findBackButton.setOnClickListener(findBackButtonListener);

        // Swipe Refresh
        browserSwipeRefresh.getViewTreeObserver().addOnScrollChangedListener(swipeRefreshOnScrollChanged);
        browserSwipeRefresh.setOnRefreshListener(swipeRefreshListener);

        // <BUG> Night mode changed to inflating new tab page, but tab count is 0.

        // Check Installer Activity
        if(dataManager.startInstallerActivity){
            // Start Welcome Activity
            startActivity(installerIntent);
            // Finish Current Activity
            finish();
        }
        else {
            // Continue or onCreateView()
            checkIntentData(getIntent());
        }
        // Set isCreated variable
        isCreated=true;
    }

    private void startTabsSync(){
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

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // Init Browser Data ( Applying View Settings )
        if(!dataManager.startInstallerActivity){
            dataManager.initBrowserPreferenceSettings();
            if(!isCreatedView) {
                /**
                 * <BUG> Application ui mode changed, recreating views and some create view bugs.
                 * Using delay for fixed tab fragments bug.
                 **/
                // Check Saved Tabs.
                ProcessDelay.Delay(() -> {
                    startTabsSync();
                    browserTabListButton.setImageDrawable(tabCounter.getTabCountDrawable());
                }, 200);

                // Register Download Receiver
                registerReceiver(DownloadsHelper.fileDownloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            }
            isCreatedView=true;
        }
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isConfigChanged=true;
        // Get Preference Manager
        AppPreferenceManager prefManager=AppPreferenceManager.getInstance();

        if(prefManager.getInt(BrowserDefaultSettings.KEY_APP_THEME)!=2){
            int nightModeFlags=getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if(nightModeFlags==Configuration.UI_MODE_NIGHT_YES || nightModeFlags==Configuration.UI_MODE_NIGHT_NO){
                checkDataView(isConfigChanged);
            }
        }
    }

    private void checkIntentData(Intent intent){
        if(intent.getStringExtra(ACTION_NAME)!=null) {
            if(intent.getStringExtra(ACTION_NAME).equals(KEY_ACTION_TAB_ON_START)) {
                String intentUrl = intent.getStringExtra(ACTION_VALUE);
                // Get Elements
                OKWebView webView = tabBuilder.getActiveTabFragment().getWebView();
                View fragmentView = tabBuilder.getActiveTabFragment().getView();
                // Set Visibilities
                fragmentView.findViewById(R.id.newTabHomeLayout).setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                // Load Url
                webView.loadUrl(intentUrl);
            }
            else if(intent.getStringExtra(ACTION_NAME).equals(KEY_ACTION_INCOGNITO_ON_START)) {
                String intentUrl = intent.getStringExtra(ACTION_VALUE);
                // Get Elements
                OKWebView webView = tabBuilder.getActiveIncognitoFragment().getWebView();
                View fragmentView = tabBuilder.getActiveIncognitoFragment().getView();
                // Set Visibilities
                fragmentView.findViewById(R.id.incognitoHomeLayout).setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                // Load Url
                webView.loadUrl(intentUrl);
            }
            else if(intent.getStringExtra(ACTION_NAME).equals(KEY_ACTION_TAB_ON_CREATE)) {
                String intentUrl = intent.getStringExtra(ACTION_VALUE);
                ProcessDelay.Delay(() -> activityNewTabHandler(intentUrl),320);
            }
            else if(intent.getStringExtra(ACTION_NAME).equals(KEY_ACTION_BROWSER_START_INSTALLER)){
                // Reset Intent
                getIntent().removeExtra(ACTION_NAME);
                // Reset Tabs
                tabBuilder.getTabFragmentList().clear();
                tabBuilder.getTabDataList().clear();
                tabBuilder.getSavedTabList().clear();
                // Clear Preferences
                tabBuilder.saveTabListPreference(new ArrayList<>());
                // Re-sync tabs
                startTabsSync();
            }
        }
    }

    @Override
    protected void onResume() {
        // Re-Building ContextManager
        ContextManager.BuildBase(this);
        View toolbarNoTab=findViewById(R.id.includeNoTabToolbar);
        View toolbarMain=findViewById(R.id.includeTabToolbar);

        // Reset Variables
        backPressHomeLayout=false;
        // Variables
        ArrayList<TabFragment> tabFragmentList=tabBuilder.getTabFragmentList();
        ArrayList<TabData> tabDataList=tabBuilder.getTabDataList();
        List<Integer> changeList=TabListFragment.changedIndexList;

        ArrayList<IncognitoTabFragment> incognitoFragmentList=tabBuilder.getIncognitoTabFragmentList();
        ArrayList<IncognitoTabData> incognitoDataList=tabBuilder.getIncognitoTabDataList();
        List<Integer> changeIncognitoList=IncognitoTabListFragment.changedIndexList;

        // <BUG> Check Views for Theme Changed
        if(isConfigChanged)
            checkDataView(true);

        // Check Action Keys
        // Check Get Intents
        if(updatedIntent!=null) {
            checkIntentData(updatedIntent);
            updatedIntent=null;
        }

        // * Check Closed Tabs.
        // * 1- If all tabs closed, show the 'No Tab Toolbar'.
        // * 2- If tabs closed at TabListActivity, remove views(Fragments) at MainActivity.onResume()
        // **
        // * 1 & 2 valid for Incognito Tabs
        // Check Tabs
        if(TabListFragment.isChanged || IncognitoTabListFragment.isChanged){
            // Check Exists Tab
            if (tabDataList.size() <= 0) {
                tabBuilder.getClassesTabDataList().clear();
                if (incognitoDataList.size() <= 0) {
                    // (Closed All Tabs)
                    // Change Toolbar
                    toolbarNoTab.setVisibility(View.VISIBLE);
                    toolbarMain.setVisibility(View.GONE);
                    // Remove Views & Fragments
                    browserFragmentView.removeAllViews();
                    for(int in=0; in<tabBuilder.getTabDataList().size(); in++) {
                        tabBuilder.getTabFragmentList().get(in).getWebView().destroy();
                        tabBuilder.removeFragment(tabBuilder.getTabFragmentList().get(in));
                    }
                    for(int ii=0; ii<tabBuilder.getIncognitoTabDataList().size(); ii++) {
                        tabBuilder.getIncognitoTabFragmentList().get(ii).getWebView().destroy();
                        tabBuilder.removeFragment(tabBuilder.getIncognitoTabFragmentList().get(ii));
                    }
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
                // Remove Closed Tabs View (Check Closed *Tabs & *Incognito Tabs)
                int changedListSize = changeList.size();
                int changedIncognitoListSize = changeIncognitoList.size();
                // Tabs
                if(changedListSize>0 && tabFragmentList.size()>0) {
                    for (int i = 0; i < changedListSize; i++) {
                        int index = changeList.get(i);
                        if(index!=0){
                            // Get Fragments
                            TabFragment frag = tabFragmentList.get(index);
                            // Remove View
                            tabBuilder.removeFragment(frag);
                            // Update Data List
                            tabBuilder.getTabFragmentList().remove(index);
                            tabBuilder.recreateTabIndex();
                        }
                    }
                    // Reset Index
                    TabListFragment.changedIndexList.clear();
                }
                // Incognito
                if(changedIncognitoListSize>0 && incognitoFragmentList.size()>0) {
                    for (int i = 0; i < changedIncognitoListSize; i++) {
                        int index = changeIncognitoList.get(i);
                        if(index!=0) {
                            // Get Fragments
                            IncognitoTabFragment frag = incognitoFragmentList.get(index);
                            // Remove View
                            tabBuilder.removeFragment(frag);
                            // Update Data List
                            tabBuilder.getIncognitoTabFragmentList().remove(index);
                            tabBuilder.recreateIncognitoTabIndex();
                        }
                    }
                    // Reset Index
                    IncognitoTabListFragment.changedIndexList.clear();
                }
                // Check Views
                if ((incognitoFragmentList.size() > 0) && (tabFragmentList.size() <= 0))
                    tabBuilder.changeIncognitoTab(0);
                else if ((tabFragmentList.size() > 0) && (incognitoFragmentList.size() <= 0)) {
                    tabBuilder.changeTab(0);
                    // Hide Incognito Icon
                    incognitoIcon.setVisibility(View.GONE);
                }
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

    private void checkDataView(boolean isCheck){
        if(isCheck) {
            if (tabBuilder.getTabDataList().size()>0 || tabBuilder.getIncognitoTabDataList().size()>0) {
                ((View) findViewById(R.id.includeTabToolbar)).setVisibility(View.VISIBLE);
                ((View) findViewById(R.id.includeNoTabToolbar)).setVisibility(View.GONE);
                browserFragmentView.setVisibility(View.VISIBLE);
            } else {
                ((View) findViewById(R.id.includeTabToolbar)).setVisibility(View.GONE);
                ((View) findViewById(R.id.includeNoTabToolbar)).setVisibility(View.VISIBLE);
                browserFragmentView.setVisibility(View.GONE);
            }
        }
    }

    public static void sendTabSignal(ActivityTabSignal signal){
        TabBuilder tabBuilder=TabBuilder.Build();
        // Runnable
        Runnable checkSignalRunnable=() -> {
            // Variables
            Drawable TabCountDrawable;
            // Check Signal Status
            // Check Incognito Tab Type
            if (!signal.getTabIsIncognito()) {
                // Check Incognito Icon
                if (incognitoIconStatic.get().getVisibility() == View.VISIBLE)
                    incognitoIconStatic.get().setVisibility(View.GONE);
                // Check Tab Events
                if (signal.getSignalStatus() == ActivityTabSignal.TAB_ON_CHANGE)
                    tabBuilder.changeTab(signal.getSignalData().tab_position);
                else if (signal.getSignalStatus() == ActivityTabSignal.TAB_ON_CREATE)
                    tabBuilder.createNewTab();
                // Get Count Drawable
                TabCountDrawable = new ToolbarTabCounter().getTabCountDrawable();
                // Get Tab Web Url
            } else {
                // Check Incognito Icon
                if (incognitoIconStatic.get().getVisibility() == View.GONE)
                    incognitoIconStatic.get().setVisibility(View.VISIBLE);
                // Check Tab Events
                if (signal.getSignalStatus() == ActivityTabSignal.INCOGNITO_ON_CHANGE)
                    tabBuilder.changeIncognitoTab(signal.getSignalData().tab_position);
                else if (signal.getSignalStatus() == ActivityTabSignal.INCOGNITO_ON_CREATE)
                    tabBuilder.createNewIncognitoTab();
                // Get Count Drawable
                TabCountDrawable = new ToolbarTabCounter().getIncognitoTabCountDrawable();
                // Get Tab Web Url
            }
            browserSearchStatic.get().setText(signal.getSignalData().tab_url);
            // Stop SwipeRefresh Status
            browserSwipeRefreshStatic.get().setRefreshing(false);
            // Update Tab Counts for Delay
            browserTabListButtonStatic.get().setImageDrawable(TabCountDrawable);
        };
        // Exec Runnable
        ProcessDelay.Delay(checkSignalRunnable, 100);
    }

    @Override
    public void onBackPressed() {
        // Elements
        View fragmentView;
        OKWebView webView;
        // Variables
        String backUrl;

        // Check Url Back, Layout & Tabs
        if(tabBuilder.getActiveTabFragment()!=null){
            // Check Tab WebView Layout
            // Get WebView & Fragment View
            fragmentView=tabBuilder.getActiveTabFragment().getView();
            webView=tabBuilder.getActiveTabFragment().getWebView();
            // Check Exists WebView History
            if(webView.canGoBack() && webView.getVisibility()==View.VISIBLE) {
                webView.goBack();
                backUrl=webView.getUrl();
            }
            else{
                // Sync Tab Data
                webView.syncOnBackForward("");
                // Hide WebView & Show Page Layout
                webView.setVisibility(View.GONE);
                (fragmentView.findViewById(R.id.newTabHomeLayout)).setVisibility(View.VISIBLE);

                if (backPressHomeLayout)
                    moveTaskToBack(true);
                else
                    backPressHomeLayout = true;
                backUrl="";
            }
        }
        else{
            // Check Incognito WebView Layout
            fragmentView=tabBuilder.getActiveIncognitoFragment().getView();
            webView=tabBuilder.getActiveIncognitoFragment().getWebView();
            // Check Exists WebView History
            if(webView.canGoBack() && webView.getVisibility()==View.VISIBLE){
                webView.goBack();
                backUrl=webView.getUrl();
            }
            else{
                // Hide WebView & Show Page Layout
                webView.setVisibility(View.GONE);
                (fragmentView.findViewById(R.id.incognitoHomeLayout)).setVisibility(View.VISIBLE);

                if (backPressHomeLayout)
                    moveTaskToBack(true);
                else
                    backPressHomeLayout = true;
                backUrl="";
            }
        }
        // Show Url
        browserSearch.setText(backUrl);
    }

    // Event Listeners
    // Custom Classes
    private void activityNewTabHandler(String url){
        // Get Elements
        View toolbarNoTab=findViewById(R.id.includeNoTabToolbar);
        View toolbarMain=findViewById(R.id.includeTabToolbar);
        // Hide No Tab Toolbar
        toolbarNoTab.setVisibility(View.GONE);
        toolbarMain.setVisibility(View.VISIBLE);
        // Create Tab
        tabBuilder.createNewTab(url);
        // Hide Incognito Icon
        incognitoIcon.setVisibility(View.GONE);
        // Re-Update Icon
        browserTabListButton.setImageDrawable(new ToolbarTabCounter().getTabCountDrawable());
        // Clear Url for New Tab
        browserSearch.setText(url);
    }
    // Show Tabs
    View.OnClickListener showTabsClickListener=view -> startActivity(tabListIntent);
    // Open Toolbar Main Menu
    View.OnClickListener showMainMenuListener=view -> MenuToolbarMain.getMenu().showAsDropDown(view);
    // Open Toolbar No Tab Menu
    View.OnClickListener showNoTabMenuListener=view -> MenuToolbarNoTab.getMenu().showAsDropDown(view);
    // Create New Incognito Tab in No Tab Toolbar
    View.OnClickListener createNewIncognitoTabLayout=view -> {
        // Get Elements
        View toolbarNoTab=findViewById(R.id.includeNoTabToolbar);
        View toolbarMain=findViewById(R.id.includeTabToolbar);
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
            // Clear Focus
            view.clearFocus();
        }
        return false;
    };
    // Swipe Refresh
    ViewTreeObserver.OnScrollChangedListener swipeRefreshOnScrollChanged=() -> {
        // Get WebView
        OKWebView webView;
        if(tabBuilder.getActiveTabFragment()!=null)
            webView = tabBuilder.getActiveTabFragment().getWebView();
        else
            webView = tabBuilder.getActiveIncognitoFragment().getWebView();
        // Check Swipe Refresh Enable
        browserSwipeRefresh.setEnabled(webView.getScrollY()==0);
    };
    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener=() -> {
        // Get WebView
        OKWebView webView;
        if(tabBuilder.getActiveTabFragment()!=null)
            webView = tabBuilder.getActiveTabFragment().getWebView();
        else
            webView = tabBuilder.getActiveIncognitoFragment().getWebView();
        // Check Url
        if(webView.getUrl()==null || webView.getUrl().equals(""))
            // Stop Swipe Refresh
            browserSwipeRefresh.setRefreshing(false);
        else {
            // Set Refresh Status
            webView.isRefreshing = true;
            // Set Swipe Refresh
            browserSwipeRefresh.setRefreshing(true);
            // Refres WebView
            webView.reload();
        }
    };
    View.OnClickListener closeFindListener=view -> {
        // Hide Find Toolbar & Show Main Toolbar
        findViewById(R.id.includeFindToolbar).setVisibility(View.GONE);
        findViewById(R.id.includeTabToolbar).setVisibility(View.VISIBLE);

        // Get WebView
        OKWebView webView;
        if(tabBuilder.getActiveTabFragment()!=null)
            webView=tabBuilder.getActiveTabFragment().getWebView();
        else
            webView=tabBuilder.getActiveIncognitoFragment().getWebView();

        webView.clearMatches();
    };
    SearchView.OnQueryTextListener findQueryTextListener=new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(tabBuilder==null)
                tabBuilder=TabBuilder.Build();
            // Get WebView
            OKWebView webView;
            if(tabBuilder.getActiveTabFragment()!=null)
                webView=tabBuilder.getActiveTabFragment().getWebView();
            else if(tabBuilder.getActiveIncognitoFragment()!=null)
                webView=tabBuilder.getActiveIncognitoFragment().getWebView();
            else
                webView=null;

            if(webView!=null) {
                if (!newText.equals("")) {
                    findQueryString = newText;
                    webView.findAllAsync(newText);
                }
                else
                    webView.clearMatches();
            }
            return false;
        }
    };
    View.OnClickListener findNextButtonListener=view -> {
        // Get WebView
        OKWebView webView;
        if(tabBuilder.getActiveTabFragment()!=null)
            webView=tabBuilder.getActiveTabFragment().getWebView();
        else
            webView=tabBuilder.getActiveIncognitoFragment().getWebView();

        if(!findQueryString.equals("")) {
            webView.findNext(true);
        }
    };
    View.OnClickListener findBackButtonListener=view -> {
        OKWebView webView;
        if(tabBuilder.getActiveTabFragment()!=null)
            webView=tabBuilder.getActiveTabFragment().getWebView();
        else
            webView=tabBuilder.getActiveIncognitoFragment().getWebView();

        if(!findQueryString.equals("")) {
            webView.findNext(false);

        }
    };

    @Override
    protected void onStart() {
        // Create App Update Manager
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        // Register Listener
        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(result -> {
            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){
                try {
                    mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.FLEXIBLE, MainActivity.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (result.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                //Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
        super.onStart();
    }
    @Override
    protected void onStop() {
        if (mAppUpdateManager != null)
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK)
                Toast.makeText(this, getString(R.string.update_download_failed_text), Toast.LENGTH_LONG).show();
        }
    }
    // Popup for Update
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.activityMainLayout),
                getString(R.string.update_new_version_available_text),
                Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(getString(R.string.install_text), view -> {
            if (mAppUpdateManager != null)
                mAppUpdateManager.completeUpdate();
        });

        snackbar.setActionTextColor(ContextCompat.getColor(this,R.color.primary));
        snackbar.show();
    }
    // Update Listener
    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            }
            else if (state.installStatus() == InstallStatus.INSTALLED){
                if (mAppUpdateManager != null)
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                // Show Message
                //Toast.makeText(this, getString(R.string.update_install_completed), Toast.LENGTH_SHORT).show();
            }
            else {
                //Log.e("MainAct/389", "InstallStateUpdatedListener: state: " + state.installStatus());
            }
        }
    };
}