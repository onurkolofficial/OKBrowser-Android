package com.onurkol.app.browser.fragments.tabs;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.adapters.tabs.RecentSearchListAdapter;
import com.onurkol.app.browser.adapters.tabs.UserWebCollectionListAdapter;
import com.onurkol.app.browser.controller.FragmentController;
import com.onurkol.app.browser.controller.KeyboardController;
import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.browser.SearchEngineController;
import com.onurkol.app.browser.controller.settings.GUIController;
import com.onurkol.app.browser.controller.settings.LanguageController;
import com.onurkol.app.browser.controller.tabs.RecentSearchController;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.controller.tabs.UserWebCollectionController;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.dialogs.tabs.DialogUserCollectionNewPage;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.interfaces.tabs.TabInterface;
import com.onurkol.app.browser.libs.JavascriptManager;
import com.onurkol.app.browser.libs.ScreenManager;
import com.onurkol.app.browser.libs.URLChecker;
import com.onurkol.app.browser.menu.webview.ContextMenuAnchor;
import com.onurkol.app.browser.menu.webview.ContextMenuAnchorImage;
import com.onurkol.app.browser.menu.webview.ContextMenuImage;
import com.onurkol.app.browser.webview.OKWebView;
import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;
import com.onurkol.app.browser.webview.config.WebViewConfig;
import com.onurkol.app.browser.webview.listeners.DownloadListener;

public class TabFragment extends Fragment implements BrowserDataInterface, TabInterface {
    View fragmentView;
    private Bitmap fragmentScreenShot;

    // TAB VARIABLES
    private int TAB_FRAGMENT_UI_STATE;
    private int TAB_FRAGMENT_MENU_UI_STATE;
    private boolean TAB_IS_INCOGNITO=false;
    private boolean TAB_ON_HOME=false;
    private boolean TAB_ON_ERROR=false;
    private int TAB_INDEX;
    TabData TAB_DATA;

    private boolean TAB_IS_DESKTOP_MODE=false;

    // FRAGMENT VARIABLES
    private boolean FRAGMENT_IS_RECREATED=false;

    Activity activity;

    TabController tabController;
    UserWebCollectionController webCollectionController;
    RecentSearchController recentSearchController;
    SearchEngineController searchEngineController;
    GUIController guiController;
    LanguageController languageController;
    FragmentController fragmentController;
    PreferenceController preferenceController;

    JavascriptManager javascriptManager;

    // In Fragment
    View tabNewTabView, tabIncognitoTabView, tabNotConnectionView, tabSearchesLayout;
    LinearLayout newCollectionWebLayoutButton;
    LinearLayoutCompat focusSearchLayoutButton;
    GridView webCollectionGridView;
    ListView recentSearchesList;

    OKWebView okWebView;
    private FrameLayout customViewContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private View mCustomView;
    // Out Fragment (Get Activity)
    SwipeRefreshLayout browserSwipeRefresh;
    EditText browserToolbarSearchInput;
    ImageButton browserToolbarForwardButton, browserToolbarBackButton;

    // WebView
    OKWebViewClient okWebViewClient;
    OKWebViewChromeClient okWebViewChromeClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferenceController=PreferenceController.getController();
        tabController=TabController.getController(requireActivity());
        webCollectionController=UserWebCollectionController.getController();
        recentSearchController=RecentSearchController.getController();
        searchEngineController=SearchEngineController.getController(requireActivity());
        guiController=GUIController.getController();
        languageController=LanguageController.getController();
        fragmentController=FragmentController.getController();
        javascriptManager=JavascriptManager.getManager();

        // Set (for Language)
        languageController.setLanguage(requireContext(), preferenceController.getInt(KEY_LANGUAGE));

        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_tab, container, false);
        activity=requireActivity();

        // Get current tab url,title, etc. data.
        TAB_DATA=tabController.getCurrentTabData();

        tabNewTabView=fragmentView.findViewById(R.id.tabNewTabView);
        tabIncognitoTabView=fragmentView.findViewById(R.id.tabIncognitoTabView);
        tabNotConnectionView=fragmentView.findViewById(R.id.tabNotConnectionView);
        newCollectionWebLayoutButton=fragmentView.findViewById(R.id.newCollectionWebLayoutButton);
        webCollectionGridView=fragmentView.findViewById(R.id.webCollectionGridView);
        browserSwipeRefresh=activity.findViewById(R.id.browserSwipeRefresh);
        tabSearchesLayout=fragmentView.findViewById(R.id.tabSearchesLayout);
        recentSearchesList=fragmentView.findViewById(R.id.recentSearchesList);
        browserToolbarSearchInput=activity.findViewById(R.id.browserToolbarSearchInput);
        focusSearchLayoutButton=fragmentView.findViewById(R.id.focusSearchLayoutButton);
        okWebView=fragmentView.findViewById(R.id.okBrowserWebView);
        browserToolbarForwardButton=activity.findViewById(R.id.browserToolbarForwardButton);
        browserToolbarBackButton=activity.findViewById(R.id.browserToolbarBackButton);

        ViewCompat.setNestedScrollingEnabled(webCollectionGridView,true);
        webCollectionGridView.setAdapter(
                new UserWebCollectionListAdapter(requireActivity(), this, webCollectionController.getWebCollectionList()));

        recentSearchesList.setAdapter(
                new RecentSearchListAdapter(requireActivity(), this, recentSearchController.getRecentSearchList()));

        newCollectionWebLayoutButton.setOnClickListener(v ->
                DialogUserCollectionNewPage.getDialog(this).show());

        // Click on Search
        focusSearchLayoutButton.setOnClickListener(v -> {
            setUIStateSearch();
            browserToolbarSearchInput.requestFocus();
            KeyboardController.showKeyboard(requireActivity(), browserToolbarSearchInput);
        });

        customViewContainer=activity.findViewById(R.id.customViewContainer);

        // Initialize OKWebView Configuration
        okWebViewClient=new OKWebViewClient(requireActivity());
        okWebViewChromeClient=new OKWebViewChromeClient(requireActivity(), okWebView, customViewContainer);
        // Set Clients
        okWebView.setOKWebViewClient(okWebViewClient);
        okWebView.setOKWebViewChromeClient(okWebViewChromeClient);
        // Set Listeners
        okWebView.setDownloadListener(new DownloadListener(requireActivity()));
        // Config
        WebViewConfig.apply(requireActivity(), okWebView);

        // Register context menu
        this.registerForContextMenu(okWebView);

        // Set JavascriptManager Exec WebView
        javascriptManager.setExecWebView(okWebView);

        // Start Fragment
        webCollectionController.syncCollectionData();
        recentSearchController.syncRecentSearchData();
        setBackForwardState(MENU_UI_DEFAULT_STATE);
        setUIStateDefault();

        // REPORT! <BUG-Fixed>: Recreate current fragment if system language and application language are different.
        String systemLang=Resources.getSystem().getConfiguration().locale.toString();
        String appLang=languageController.getLanguage();
        if(!systemLang.equals(appLang) && !FRAGMENT_IS_RECREATED){
            FRAGMENT_IS_RECREATED=true;
            fragmentController.detachFragment(this);
            fragmentController.attachFragment(this);
        }

        // Check SYNC Data
        if(getArguments()!=null){
            if(getArguments().getString(KEY_NEW_TAB_URL)!=null &&
                    !getArguments().getString(KEY_NEW_TAB_URL).equals(NEW_TAB_URL) &&
                    !getArguments().getString(KEY_NEW_TAB_URL).equals(NEW_INCOGNITO_TAB_URL))
                onStartWeb(getArguments().getString(KEY_NEW_TAB_URL));
        }
        return fragmentView;
    }

    // for Fullscreen Video View
    public boolean inCustomView() {
        return (mCustomView != null);
    }
    public void hideCustomView() {
        okWebViewChromeClient.onHideCustomView();
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
        tabController.getCurrentTab().getWebView().onPause();
    }

    @Override
    public void onStop() {
        super.onStop();    //To change body of overridden methods use File | Settings | File Templates.
        if (inCustomView()) {
            hideCustomView();
        }
    }

    @Override
    public void onStartWeb(@NonNull String searchOrUrl) {
        TAB_ON_HOME=false;
        setUIStateSearchBreak();
        // Check QUERY
        String loadURL="";
        if(URLChecker.isURL(searchOrUrl)){
            if(searchOrUrl.startsWith(URLChecker.URL_SCHEME_VIEW_SOURCE))
                loadURL=URLChecker.convertViewSourceUrl(searchOrUrl);
            else
                loadURL=URLChecker.convertHttpUrl(searchOrUrl);
        }
        else{
            searchEngineController.syncSearchEngineData();
            String seQuery=searchEngineController.getCurrentSearchEngineQuery();
            loadURL=seQuery+searchOrUrl;
        }
        // Load Url
        okWebView.loadUrl(loadURL);
    }

    @Override
    public void goBack() {
        String saveURL, saveTITLE;
        if(!okWebView.canGoBack() && !TAB_ON_HOME){
            // WebView: Not go back, Layout: WebView.
            if(TAB_IS_INCOGNITO) {
                saveURL=NEW_INCOGNITO_TAB_URL;
                saveTITLE=getString(R.string.new_incognito_tab_text);
            }
            else {
                saveURL=NEW_TAB_URL;
                saveTITLE=getString(R.string.new_tab_text);
            }
            tabController.getCurrentTabData().setUrl(saveURL);
            tabController.getCurrentTabData().setTitle(saveTITLE);
            setUIStateDefault();
            setBackForwardState(MENU_UI_CAN_FORWARD_NO_BACK_STATE);
            setMenuUIStateUpdate();
            browserToolbarSearchInput.setText("");
        }
        else if((!okWebView.canGoBack() && TAB_ON_HOME) || (okWebView.canGoBack() && TAB_ON_HOME)){
            // WebView: Not go back, Layout: Home. (Pressed home button)
            // WebView: go back, Layout: Home. (Pressed home button and webview can go back)
            TAB_ON_HOME=false;
            saveURL=okWebView.getUrl();
            saveTITLE=okWebView.getTitle();
            tabController.getCurrentTabData().setUrl(saveURL);
            tabController.getCurrentTabData().setTitle(saveTITLE);
            setUIStateSearchBreak();
            setBackForwardState(MENU_UI_CAN_FORWARD_BACK_STATE);
            setMenuUIStateUpdate();
            browserToolbarSearchInput.setText(saveURL);
        }
        else if(okWebView.canGoBack()){
            // WebView: go back
            okWebView.goBack();
            saveURL=okWebView.getUrl();
            saveTITLE=okWebView.getTitle();
            tabController.getCurrentTabData().setUrl(saveURL);
            tabController.getCurrentTabData().setTitle(saveTITLE);
            setBackForwardState(MENU_UI_CAN_FORWARD_BACK_STATE);
            setMenuUIStateUpdate();
            browserToolbarSearchInput.setText(saveURL);
        }

        if(!TAB_IS_INCOGNITO)
            tabController.saveTabDataPreference();
    }

    @Override
    public void goForward() {
        String saveURL, saveTITLE;
        if((!okWebView.canGoForward() && TAB_ON_HOME) || (okWebView.canGoForward() && TAB_ON_HOME)){
            // WebView: Not go forward, Layout: home.
            TAB_ON_HOME=false;
            saveURL=okWebView.getUrl();
            saveTITLE=okWebView.getTitle();
            tabController.getCurrentTabData().setUrl(saveURL);
            tabController.getCurrentTabData().setTitle(saveTITLE);
            setUIStateSearchBreak();
            if(okWebView.canGoForward())
                setBackForwardState(MENU_UI_CAN_FORWARD_BACK_STATE);
            else
                setBackForwardState(MENU_UI_CAN_BACK_NO_FORWARD_STATE);
            setMenuUIStateUpdate();
            browserToolbarSearchInput.setText(saveURL);
        }
        else if(okWebView.canGoForward()){
            // WebView: go forward
            okWebView.goForward();
            saveURL=okWebView.getUrl();
            saveTITLE=okWebView.getTitle();
            tabController.getCurrentTabData().setUrl(saveURL);
            tabController.getCurrentTabData().setTitle(saveTITLE);
            setBackForwardState(MENU_UI_CAN_FORWARD_BACK_STATE);
            setMenuUIStateUpdate();
            browserToolbarSearchInput.setText(saveURL);
        }

        if(!TAB_IS_INCOGNITO)
            tabController.saveTabDataPreference();
    }

    @Override
    public void goHome() {
        String saveURL, saveTITLE;
        if(TAB_IS_INCOGNITO) {
            saveURL=NEW_INCOGNITO_TAB_URL;
            saveTITLE=getString(R.string.new_incognito_tab_text);
        }
        else {
            saveURL=NEW_TAB_URL;
            saveTITLE=getString(R.string.new_tab_text);
        }
        tabController.getCurrentTabData().setUrl(saveURL);
        tabController.getCurrentTabData().setTitle(saveTITLE);
        setUIStateDefault();
        if((!okWebView.canGoBack() && !TAB_ON_HOME) || (okWebView.canGoBack() && TAB_ON_HOME)){
            setBackForwardState(MENU_UI_CAN_BACK_STATE);
            setMenuUIStateUpdate();
        }
        browserToolbarSearchInput.setText("");

        if(!TAB_IS_INCOGNITO)
            tabController.saveTabDataPreference();
    }

    @Override
    public int getBackForwardState() {
        return TAB_FRAGMENT_MENU_UI_STATE;
    }

    @Override
    public void setBackForwardState(int stateId) {
        TAB_FRAGMENT_MENU_UI_STATE=stateId;
    }

    @Override
    public void setMenuUIStateUpdate() {
        if(guiController.isDenseMode() && tabController.getCurrentTab()!=null){
            if (getBackForwardState()==MENU_UI_CAN_BACK_STATE) {
                browserToolbarBackButton.setEnabled(true);
            }
            else if (getBackForwardState()==MENU_UI_CAN_FORWARD_STATE) {
                browserToolbarForwardButton.setEnabled(true);
            }
            else if (getBackForwardState()==MENU_UI_CAN_FORWARD_BACK_STATE) {
                browserToolbarBackButton.setEnabled(true);
                browserToolbarForwardButton.setEnabled(true);
            }
            else if (getBackForwardState()==MENU_UI_CAN_FORWARD_NO_BACK_STATE) {
                browserToolbarBackButton.setEnabled(false);
                browserToolbarForwardButton.setEnabled(true);
            }
            else if (getBackForwardState()==MENU_UI_CAN_BACK_NO_FORWARD_STATE) {
                browserToolbarBackButton.setEnabled(true);
                browserToolbarForwardButton.setEnabled(false);
            }
            else if (getBackForwardState()==MENU_UI_DEFAULT_STATE) {
                browserToolbarBackButton.setEnabled(false);
                browserToolbarForwardButton.setEnabled(false);
            }
        }
    }

    @Override
    public void restoreMenuUIState() {
        if(tabController.getCurrentTab()!=null)
            setBackForwardState(tabController.getCurrentTab().getBackForwardState());
        else
            setBackForwardState(MENU_UI_DEFAULT_STATE);
        setMenuUIStateUpdate();
    }

    @Override
    public void restoreMenuUIState(int getState) {
        setBackForwardState(getState);
        setMenuUIStateUpdate();
    }

    @Override
    public void setUIStateSearch(){
        tabNewTabView.setVisibility(View.GONE);
        tabIncognitoTabView.setVisibility(View.GONE);
        tabNotConnectionView.setVisibility(View.GONE);
        tabSearchesLayout.setVisibility(View.VISIBLE);
        okWebView.setVisibility(View.GONE);

        TAB_FRAGMENT_UI_STATE=UI_SEARCH_STATE;
    }

    @Override
    public void setUIStateSearchBreak(){
        tabSearchesLayout.setVisibility(View.GONE);
        // Hide Search Layout. If webview is shown public webview, otherwise show home layout.
        if(TAB_ON_HOME) {
            setUIStateDefault();
            // Update Data
            if(!TAB_IS_INCOGNITO) {
                tabController.getCurrentTabData().setTitle(getString(R.string.new_tab_text));
                tabController.getCurrentTabData().setUrl(NEW_TAB_URL);
                tabController.saveTabDataPreference();
            }
        }
        else{
            tabIncognitoTabView.setVisibility(View.GONE);
            tabNewTabView.setVisibility(View.GONE);
            tabNotConnectionView.setVisibility(View.GONE);
            okWebView.setVisibility(View.VISIBLE);

            // Update Data
            if (!TAB_IS_INCOGNITO) {
                tabController.getCurrentTabData().setTitle(okWebView.getTitle());
                tabController.getCurrentTabData().setUrl(okWebView.getUrl());
                tabController.saveTabDataPreference();
            }

            TAB_ON_HOME=false;
            TAB_ON_ERROR=false;
        }

        TAB_FRAGMENT_UI_STATE=UI_SEARCH_BREAK_STATE;
    }

    @Override
    public void setUIStateDefault(){
        if(TAB_IS_INCOGNITO) {
            tabIncognitoTabView.setVisibility(View.VISIBLE);
            tabNewTabView.setVisibility(View.GONE);
        }
        else {
            tabIncognitoTabView.setVisibility(View.GONE);
            tabNewTabView.setVisibility(View.VISIBLE);
        }
        tabNotConnectionView.setVisibility(View.GONE);
        tabSearchesLayout.setVisibility(View.GONE);
        okWebView.setVisibility(View.GONE);
        browserToolbarSearchInput.setText("");

        TAB_ON_HOME=true;
        TAB_FRAGMENT_UI_STATE=UI_DEFAULT_STATE;
    }

    @Override
    public void setUIStateError() {
        tabIncognitoTabView.setVisibility(View.GONE);
        tabNewTabView.setVisibility(View.GONE);
        tabNotConnectionView.setVisibility(View.VISIBLE);
        tabSearchesLayout.setVisibility(View.GONE);
        okWebView.setVisibility(View.GONE);
        TAB_ON_ERROR=true;
        TAB_FRAGMENT_UI_STATE=UI_ERROR_STATE;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            webCollectionGridView.setNumColumns(COLLECTION_GRID_COUNT_LANDSCAPE);
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            webCollectionGridView.setNumColumns(COLLECTION_GRID_COUNT);
    }

    @Override
    public void onResume() {
        updateScreenShot();
        webCollectionGridView.invalidateViews();
        // Gets the state of the webview when it resumes.
        browserSwipeRefresh.setOnRefreshListener(swipeRefreshListener);

        // Update JavascriptManager Exec WebView
        javascriptManager.setExecWebView(tabController.getCurrentTab().getWebView());

        tabController.getCurrentTab().getWebView().onResume();
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        // Get Hit Result
        WebView.HitTestResult result = okWebView.getHitTestResult();

        // Get Menu Type
        int type=result.getType();
        if(type == WebView.HitTestResult.IMAGE_TYPE){
            ContextMenuImage.getContextMenuWindow(requireActivity())
                    .showAtLocation(okWebView, Gravity.CENTER, 0,0);
        }
        else if(type == WebView.HitTestResult.SRC_ANCHOR_TYPE){
            ContextMenuAnchor.getContextMenuWindow(requireActivity())
                    .showAtLocation(okWebView, Gravity.CENTER, 0,0);
        }
        else if(type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE){
            ContextMenuAnchorImage.getContextMenuWindow(requireActivity())
                    .showAtLocation(okWebView, Gravity.CENTER, 0,0);
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void setTabIndex(int newTabIndex){
        TAB_INDEX=newTabIndex;
    }

    @Override
    public void setIncognito(boolean tabIncognito) {
        TAB_IS_INCOGNITO=tabIncognito;
    }
    @Override
    public boolean isIncognito(){
        return TAB_IS_INCOGNITO;
    }

    @Override
    public void setTabHome(boolean tabIsHomePage) {
        TAB_ON_HOME=tabIsHomePage;
    }
    @Override
    public boolean isTabHome() {
        return TAB_ON_HOME;
    }

    @Override
    public void setDesktopMode(boolean desktopMode) {
        TAB_IS_DESKTOP_MODE=desktopMode;
        // Set User Agent
        if(desktopMode)
            okWebView.getSettings().setUserAgentString(DESKTOP_USER_AGENT);
        else
            okWebView.getSettings().setUserAgentString(null);
        // check refresh
        if(!TAB_ON_HOME)
            okWebView.reload();
    }
    @Override
    public boolean isDesktopMode() {
        return TAB_IS_DESKTOP_MODE;
    }

    @Override
    public void setTabError(boolean tabIsErrorPage) {
        TAB_ON_ERROR=tabIsErrorPage;
        // Show Error View
        if(tabIsErrorPage)
            setUIStateError();
        else
            setUIStateSearchBreak();
    }
    @Override
    public boolean isTabError() {
        return TAB_ON_ERROR;
    }

    @NonNull
    @Override
    public OKWebView getWebView(){
        return okWebView;
    }

    @Override
    public int getUIState(){
        return TAB_FRAGMENT_UI_STATE;
    }

    public void updateScreenShot(){
        fragmentScreenShot=ScreenManager.getScreenshot(requireActivity(), fragmentView);
        if(TAB_IS_INCOGNITO)
            tabController.getIncognitoTabList().get(TAB_INDEX).setPreviewBitmap(fragmentScreenShot);
        else
            tabController.getTabList().get(TAB_INDEX).setPreviewBitmap(fragmentScreenShot);
    }

    // Listeners
    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener=() -> {
        if(!TAB_ON_HOME){
            if(!(!TAB_IS_INCOGNITO && TAB_DATA.getUrl().equals(NEW_TAB_URL)) ||
                    !(TAB_IS_INCOGNITO && TAB_DATA.getUrl().equals(NEW_INCOGNITO_TAB_URL))){
                if(TAB_ON_ERROR)
                    setUIStateSearchBreak();
                okWebView.reload();
            }
        }
        browserSwipeRefresh.setRefreshing(false);
    };
}
