package com.onurkol.app.browser.activity;

import static com.onurkol.app.browser.libs.ActivityActionAnimator.finishAndStartActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.browser.TabListActivity;
import com.onurkol.app.browser.activity.installer.InstallerActivity;
import com.onurkol.app.browser.bottomsheets.menu.toolbars.BottomSheetMenuToolbarDense;
import com.onurkol.app.browser.bottomsheets.menu.toolbars.BottomSheetMenuToolbarNoTab;
import com.onurkol.app.browser.controller.ConnectionController;
import com.onurkol.app.browser.controller.FragmentController;
import com.onurkol.app.browser.controller.KeyboardController;
import com.onurkol.app.browser.controller.settings.DayNightModeController;
import com.onurkol.app.browser.controller.settings.GUIController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.controller.browser.BrowserDataInitController;
import com.onurkol.app.browser.controller.ContextController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.tabs.RecentSearchController;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.controller.tabs.TabCounterController;
import com.onurkol.app.browser.fragments.tabs.TabFragment;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.libs.ActivityActionAnimator;
import com.onurkol.app.browser.menu.toolbars.MenuToolbarNoTab;
import com.onurkol.app.browser.menu.toolbars.MenuToolbarSimple;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BrowserDataInterface {
    /***
     * Project: OKBrowser
     * Os: Currently only available on Android.
     * Language: Java
     * Type: 'free' Open-Source
     *
     * Author: Onur Kol
     * Web: https://onurkolofficial.epizy.com
     ***/
    BrowserDataInitController browserDataController;
    PreferenceController preferenceController;
    DayNightModeController dayNightController;
    LanguageController languageController;
    GUIController guiController;
    TabController tabController;
    FragmentController fragmentController;
    RecentSearchController recentSearchController;

    // Activity Static Variables
    public static boolean isCreated=false,
            isClearAllData=false,
            isSettingChanged=false,
            isNewTab=false,
            isNewIncognitoTab=false,
            isTabChanged=false,
            isTabChangedIncognito=false,
            isTabClosed=false,
            isTabClosedIncognito=false,
            isAllTabsClosed=false,
            isFindMode=false;

    public static String newTabUrl=null,
            newIncognitoTabUrl=null;

    public static int tabChangeIndex=0,
            tabChangeIncognitoIndex=0;
    public static List<Integer> tabCloseIndexList=new ArrayList<>(),
            tabCloseIncognitoIndexList=new ArrayList<>();

    Bundle activityInstanceState=null;

    LinearLayout browserFragmentViewForWeb;
    SwipeRefreshLayout browserSwipeRefresh;
    View toolbarSimpleView, toolbarDenseView, toolbarDenseBottomView, toolbarNoTabView, toolbarFindView;
    ImageView browserToolbarIncognitoIcon;
    EditText browserToolbarSearchInput;
    ImageButton browserToolbarTabCountButton, browserToolbarMenuButton,
            browserToolbarHomeButton, browserToolbarForwardButton, browserToolbarBackButton,
            noTabToolbarNewTabButton, noTabToolbarNewIncognitoTabButton, noTabToolbarMenuButton,
            findCloseButton, findNextButton, findBackButton;
    ProgressBar browserToolbarProgressbar;
    SearchView findSearchView;

    String findQueries;

    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;

    @Override
    protected void onStart() {
        // In-App Update
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)){
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/,
                            MainActivity.this,
                            RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                updateOnReadySnackBar();
            }
        });
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // SetBaseContext can only be used in MainActivity.
        ContextController.setBaseContext(this);

        activityInstanceState=savedInstanceState;

        preferenceController=PreferenceController.getController();
        browserDataController=BrowserDataInitController.getController();
        browserDataController.init();

        dayNightController=DayNightModeController.getController();
        languageController=LanguageController.getController();
        guiController=GUIController.getController();
        fragmentController=FragmentController.getController();
        fragmentController.setSupportFragmentManager(getSupportFragmentManager());
        tabController=TabController.getController(this);
        recentSearchController=RecentSearchController.getController();

        if(!browserDataController.isInstallerCompleted()){
            startActivity(new Intent(this, InstallerActivity.class));
            finish();
        }

        // Set Theme|Language
        dayNightController.setDayNightMode(this, preferenceController.getInt(KEY_DAY_NIGHT_MODE));
        languageController.setLanguage(this, preferenceController.getInt(KEY_LANGUAGE));

        super.onCreate(savedInstanceState);
        // Set GUI
        if(guiController.isSimpleMode()) {
            setContentView(R.layout.activity_main_simple);
            toolbarSimpleView=findViewById(R.id.toolbarMainSimpleView);
        }
        else if(guiController.isDenseMode()) {
            setContentView(R.layout.activity_main_dense);
            toolbarDenseView=findViewById(R.id.toolbarMainDenseView);
            toolbarDenseBottomView=findViewById(R.id.toolbarMainDenseBottomView);

            browserToolbarHomeButton=findViewById(R.id.browserToolbarHomeButton);
            browserToolbarForwardButton=findViewById(R.id.browserToolbarForwardButton);
            browserToolbarBackButton=findViewById(R.id.browserToolbarBackButton);

            browserToolbarHomeButton.setOnClickListener(v -> tabController.getCurrentTab().goHome());
            browserToolbarBackButton.setOnClickListener(v -> tabController.getCurrentTab().goBack());
            browserToolbarForwardButton.setOnClickListener(v -> tabController.getCurrentTab().goForward());
        }
        browserSwipeRefresh=findViewById(R.id.browserSwipeRefresh);
        browserFragmentViewForWeb=findViewById(R.id.browserFragmentViewForWeb);
        // Note! Toolbar items ids are the same. ( toolbar_main_simple.xml & toolbar_main_dense(_bottom).xml )
        browserToolbarSearchInput=findViewById(R.id.browserToolbarSearchInput);
        browserToolbarIncognitoIcon=findViewById(R.id.browserToolbarIncognitoIcon);
        browserToolbarTabCountButton=findViewById(R.id.browserToolbarTabCountButton);
        browserToolbarMenuButton=findViewById(R.id.browserToolbarMenuButton);
        browserToolbarProgressbar=findViewById(R.id.browserToolbarProgressbar);
        /////
        toolbarNoTabView=findViewById(R.id.toolbarNoTabView);
        toolbarFindView=findViewById(R.id.toolbarFindView);
        noTabToolbarNewTabButton=findViewById(R.id.noTabToolbarNewTabButton);
        noTabToolbarNewIncognitoTabButton=findViewById(R.id.noTabToolbarNewIncognitoTabButton);
        noTabToolbarMenuButton=findViewById(R.id.noTabToolbarMenuButton);
        findCloseButton=findViewById(R.id.findCloseButton);
        findNextButton=findViewById(R.id.findNextButton);
        findBackButton=findViewById(R.id.findBackButton);
        findSearchView=findViewById(R.id.findSearchView);

        // Ads Initialize
        String getAppId=getString(R.string.startapp_app_id);
        StartAppSDK.init(this, getAppId, false);
        // Disable Startapp Splash Screen.
        StartAppAd.disableSplash();
        // Get Banner
        final Banner appBanner=findViewById(R.id.startAppBanner);

        // Set Listener
        appBanner.setBannerListener(new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                appBanner.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailedToReceiveAd(View view) {
                appBanner.setVisibility(View.GONE);
            }
            @Override
            public void onImpression(View view) {}
            @Override
            public void onClick(View view) {}
        });
        // Hide Default
        appBanner.setVisibility(View.GONE);

        registerReceiver(onDownloadCompleted,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Exception @MenuStyle
        // Since the menu is ready in 'simple' mode, the 'dense' mode menu is not used here.
        // Check: setUIStateNewTab(), setUIStateNoTab();
        if(guiController.isSimpleMode())
            browserToolbarMenuButton.setOnClickListener(v ->
                    MenuToolbarSimple.getMenuWindow(this).showAsDropDown(v));
        noTabToolbarMenuButton.setOnClickListener(v ->
                MenuToolbarNoTab.getMenuWindow(this).showAsDropDown(v));

        browserToolbarTabCountButton.setOnClickListener(v ->
                ActivityActionAnimator.startActivity(this, new Intent(this, TabListActivity.class)));

        noTabToolbarNewTabButton.setOnClickListener(v -> {
            isNewTab=true;
            onResume();
        });
        noTabToolbarNewIncognitoTabButton.setOnClickListener(v -> {
            isNewIncognitoTab=true;
            onResume();
        });

        findCloseButton.setOnClickListener(v -> setFindMode(this,false));
        findBackButton.setOnClickListener(v -> {
            if(!findQueries.equals(""))
                tabController.getCurrentTab().getWebView().findNext(false);
        });
        findNextButton.setOnClickListener(v -> {
            if(!findQueries.equals(""))
                tabController.getCurrentTab().getWebView().findNext(true);
        });
        findSearchView.setOnQueryTextListener(findQueryTextListener);

        // Search Input
        // <BUG-Fixed>: 'onFocus' called one time. 'onClick' not called first time.
        browserToolbarSearchInput.setSelectAllOnFocus(true);
        browserToolbarSearchInput.setOnClickListener(v -> tabController.getCurrentTab().setUIStateSearch());
        browserToolbarSearchInput.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus)
                tabController.getCurrentTab().setUIStateSearch();
        });
        browserToolbarSearchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String getUrl=browserToolbarSearchInput.getText().toString();
                if(getUrl.equals("")) {
                    // Check if web view active to change ui state. (SearchBreak)
                    tabController.getCurrentTab().setUIStateSearchBreak();
                }
                else {
                    // Save Data
                    if(!tabController.getCurrentTab().isIncognito())
                        recentSearchController.newSearch(getUrl);
                    // Start Search
                    tabController.getCurrentTab().onStartWeb(getUrl);
                }
                browserToolbarSearchInput.setText("");
                KeyboardController.hideKeyboard(this, v);
                return true;
            }
            return false;
        });

        setUIStateNewTab(false);
        checkInternetConnection();
        tabController.onStart();
        isCreated=true;
    }

    private void setUIStateNewTab(boolean isIncognito){
        if(guiController.isDenseMode()) {
            browserToolbarForwardButton.setEnabled(false);
            browserToolbarBackButton.setEnabled(false);
            browserToolbarHomeButton.setEnabled(true);
            // Click Menu
            browserToolbarMenuButton.setOnClickListener(v ->
                    BottomSheetMenuToolbarDense.getMenuBottomSheet(this).show());
            toolbarDenseView.setVisibility(View.VISIBLE);
        }
        else if(guiController.isSimpleMode()){
            toolbarSimpleView.setVisibility(View.VISIBLE);
        }
        // Check Incognito Icon
        if(isIncognito)
            browserToolbarIncognitoIcon.setVisibility(View.VISIBLE);
        else
            browserToolbarIncognitoIcon.setVisibility(View.GONE);

        // Hide Progressbar & Toolbar
        toolbarNoTabView.setVisibility(View.GONE);
        toolbarFindView.setVisibility(View.GONE);
        browserToolbarProgressbar.setVisibility(View.GONE);
        browserToolbarTabCountButton.setEnabled(true);
        browserSwipeRefresh.setEnabled(true);
        browserToolbarSearchInput.setText("");
    }

    private void setUIStateNoTab(){
        if(guiController.isDenseMode()) {
            browserToolbarForwardButton.setEnabled(false);
            browserToolbarBackButton.setEnabled(false);
            browserToolbarHomeButton.setEnabled(false);
            // Click Menu
            browserToolbarMenuButton.setOnClickListener(v ->
                    BottomSheetMenuToolbarNoTab.getMenuBottomSheet(this).show());
            noTabToolbarMenuButton.setVisibility(View.GONE); // Only 'Dense Mode'
            toolbarDenseView.setVisibility(View.GONE);
        }
        else if(guiController.isSimpleMode()) {
            toolbarSimpleView.setVisibility(View.GONE);
            noTabToolbarMenuButton.setVisibility(View.VISIBLE);
        }
        browserToolbarIncognitoIcon.setVisibility(View.GONE);
        toolbarNoTabView.setVisibility(View.VISIBLE);
        browserToolbarTabCountButton.setEnabled(false);
        browserSwipeRefresh.setEnabled(false);
    }

    public static void setFindMode(Context context, boolean mode){
        isFindMode=mode;
        // Re-define. (Because 'setFindMode' is static method.)
        GUIController guiController=GUIController.getController();
        TabController tabController=TabController.getController(context);
        if(isFindMode){
            if(guiController.isSimpleMode())
                ((Activity)context).findViewById(R.id.toolbarMainSimpleView).setVisibility(View.GONE);
            else if(guiController.isDenseMode())
                ((Activity)context).findViewById(R.id.toolbarMainDenseView).setVisibility(View.GONE);
            ((Activity)context).findViewById(R.id.toolbarFindView).setVisibility(View.VISIBLE);
        }
        else{
            if(guiController.isSimpleMode())
                ((Activity)context).findViewById(R.id.toolbarMainSimpleView).setVisibility(View.VISIBLE);
            else if(guiController.isDenseMode())
                ((Activity)context).findViewById(R.id.toolbarMainDenseView).setVisibility(View.VISIBLE);
            ((Activity)context).findViewById(R.id.toolbarFindView).setVisibility(View.GONE);
            tabController.getCurrentTab().getWebView().clearMatches();
        }
    }
    SearchView.OnQueryTextListener findQueryTextListener=new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if(!newText.equals("")){
                findQueries=newText;
                tabController.getCurrentTab().getWebView().findAllAsync(newText);
            }
            else
                tabController.getCurrentTab().getWebView().clearMatches();
            return false;
        }
    };

    public void checkInternetConnection(){
        ConnectionController.startSession(this);
        // Create Snackbar Message
        Snackbar messageSnackbar=Snackbar.make(findViewById(R.id.browserCoordinatorLayout), getString(R.string.not_connected_internet_text),
                Snackbar.LENGTH_INDEFINITE);
        // Check Connection
        if(!ConnectionController.isConnected()){
            messageSnackbar.setAction(getString(R.string.ok_text),v -> messageSnackbar.dismiss());
            messageSnackbar.show();
        }
        else
            messageSnackbar.dismiss();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // ex. Status bar down and up to calling this.
        checkInternetConnection();
        // Dismiss dialog to calling onResume
        if(isAllTabsClosed || isNewTab || isNewIncognitoTab)
            onResume();
        // Workaround for UIMode on change 'DayNightModeController.DAY_NIGHT_MODE_AUTO'
        if(activityInstanceState!=null){
            isSettingChanged=true;
            onResume();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        // Settings changed or Reset application data.
        if(isClearAllData || isSettingChanged) {
            isClearAllData=false;
            isSettingChanged=false;

            // Re-create Tab Data.
            tabController.onDestroy();

            finishAndStartActivity(this, getIntent());
        }
        // Tab operation order: 'close, create, change'
        // Close Tab
        if(isTabClosed || isTabClosedIncognito || isAllTabsClosed){
            if(isTabClosed) {
                tabController.onRestoreTabData(false);
                for (int indexValue : tabCloseIndexList)
                    tabController.closeTab(indexValue, false);

                if(tabController.getTabList().size()<=0){
                    if(tabController.getIncognitoTabList().size()<=0)
                        isAllTabsClosed=true;
                    else {
                        tabController.changeTab(0, true);
                        setUIStateNewTab(true);
                    }
                }
                else {
                    tabController.changeTab(0, false);
                    setUIStateNewTab(false);
                }
                // Save completed process data. (for GridView data onchange.)
                tabController.onBackupPointTabData(false);
            }
            if(isTabClosedIncognito){
                tabController.onRestoreTabData(true);
                for (int indexValue : tabCloseIncognitoIndexList)
                    tabController.closeTab(indexValue, true);

                if(tabController.getIncognitoTabList().size()<=0){
                    if(tabController.getTabList().size()<=0)
                        isAllTabsClosed=true;
                    else {
                        tabController.changeTab(0, false);
                        setUIStateNewTab(false);
                    }
                }
                else {
                    tabController.changeTab(0, true);
                    setUIStateNewTab(true);
                }
                // Save completed process data. (for GridView data onchange.)
                tabController.onBackupPointTabData(true);
            }
            if(isAllTabsClosed){
                tabController.closeAllTabs();
                setUIStateNoTab();
            }
            isTabClosed=false;
            isTabClosedIncognito=false;
            isAllTabsClosed=false;
            tabCloseIndexList.clear();
            tabCloseIncognitoIndexList.clear();
        }
        // Create New Tab or Incognito Tab.
        if(isNewTab || isNewIncognitoTab){
            if(isNewTab){
                if(newTabUrl!=null)
                    tabController.newTab(newTabUrl);
                else
                    tabController.newTab();
            }
            if(isNewIncognitoTab){
                if(newIncognitoTabUrl!=null)
                    tabController.newIncognitoTab(newIncognitoTabUrl);
                else
                    tabController.newIncognitoTab();
            }
            setUIStateNewTab(isNewIncognitoTab);
            isNewTab=false;
            isNewIncognitoTab=false;
            newTabUrl=null;
            newIncognitoTabUrl=null;
        }
        // Change Tab.
        if(isTabChanged || isTabChangedIncognito){
            if(isTabChanged) {
                tabController.changeTab(tabChangeIndex, false);
                browserToolbarIncognitoIcon.setVisibility(View.GONE);
                if(!tabController.getTabData(tabChangeIndex).getUrl().equals(NEW_TAB_URL))
                    browserToolbarSearchInput.setText(tabController.getTabData(tabChangeIndex).getUrl());
                else
                    browserToolbarSearchInput.setText("");
            }
            if(isTabChangedIncognito) {
                tabController.changeTab(tabChangeIncognitoIndex, true);
                browserToolbarIncognitoIcon.setVisibility(View.VISIBLE);
                if(!tabController.getIncognitoTabData(tabChangeIndex).getUrl().equals(NEW_INCOGNITO_TAB_URL))
                    browserToolbarSearchInput.setText(tabController.getIncognitoTabData(tabChangeIndex).getUrl());
                else
                    browserToolbarSearchInput.setText("");
            }
            isTabChanged=false;
            isTabChangedIncognito=false;
            tabChangeIndex=0;
        }
        // Check 'Open As' Link exist.
        Uri data=getIntent().getData();
        if(data!=null) {
            String openAsUrl = data.toString();
            tabController.newTab(openAsUrl);
            getIntent().setData(null); // <BUG-Fixed> Recreate 'fragment'
        }

        // Re-setting tab count for on resume.
        if(tabController.getCurrentTab()!=null) {
            if(tabController.getCurrentTab().isIncognito())
                TabCounterController.setMenuTabCounterButton(this, tabController.getIncognitoTabCount());
            else
                TabCounterController.setMenuTabCounterButton(this, tabController.getTabCount());
        }

        browserToolbarProgressbar.setVisibility(View.GONE);
        super.onResume();
    }

    BroadcastReceiver onDownloadCompleted=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Snackbar.make(findViewById(R.id.browserCoordinatorLayout), context.getString(R.string.download_completed_text),
                            Snackbar.LENGTH_INDEFINITE)
                    .setAction(context.getString(R.string.ok_text),v -> {})
                    .show();
        }
    };

    @Override
    public void onBackPressed() {
        if(tabController.getCurrentTab().getUIState()==TabFragment.UI_SEARCH_STATE) {
            tabController.getCurrentTab().setUIStateSearchBreak();
        }
        else {
            if(tabController.getCurrentTab().getUIState()==TabFragment.UI_SEARCH_BREAK_STATE)
                tabController.getCurrentTab().goBack();
            else
                super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("TAG", "onActivityResult: app download failed");
            }
        }
    }

    @Override
    protected void onStop() {
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
        super.onStop();
    }

    private void updateOnReadySnackBar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.browserCoordinatorLayout),
                        getString(R.string.update_ready_text),
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.install_text), view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getColor(R.color.primary));
        snackbar.show();
    }

    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                updateOnReadySnackBar();
            } else if (state.installStatus() == InstallStatus.INSTALLED){
                if (mAppUpdateManager != null){
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {
                Log.i("TAG", "InstallStateUpdatedListener: state: " + state.installStatus());
            }
        }
    };
}