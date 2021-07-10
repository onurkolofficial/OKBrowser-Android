package com.onurkol.app.browser.webview;

import android.webkit.WebSettings;

import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;

public class WebViewConfig {
    static WebViewConfig instance=null;

    public static synchronized WebViewConfig getInstance(){
        if(instance==null)
            instance=new WebViewConfig();
        return instance;
    }

    public void setWebViewConfig(OKWebView webView){
        // Get Classes
        AppPreferenceManager prefManager=AppPreferenceManager.getInstance();

        // Get WebSettings
        WebSettings browserWebSetting=webView.getSettings();

        // Get Settings
        boolean javascriptMode=prefManager.getBoolean(BrowserDefaultSettings.KEY_APP_CACHE),
                geoLocation=prefManager.getBoolean(BrowserDefaultSettings.KEY_GEO_LOCATION),
                popups=prefManager.getBoolean(BrowserDefaultSettings.KEY_POPUPS),
                domStorage=prefManager.getBoolean(BrowserDefaultSettings.KEY_DOM_STORAGE),
                zoom=prefManager.getBoolean(BrowserDefaultSettings.KEY_ZOOM),
                zoomButtons=prefManager.getBoolean(BrowserDefaultSettings.KEY_ZOOM_BUTTONS),
                saveForms=prefManager.getBoolean(BrowserDefaultSettings.KEY_SAVE_FORMS),
                desktopMode=prefManager.getBoolean(BrowserDefaultSettings.KEY_DESKTOP_MODE);

        // Set User Agent for WebView.
        if(desktopMode)
            browserWebSetting.setUserAgentString(BrowserDefaultSettings.DESKTOP_USER_AGENT);
        else
            browserWebSetting.setUserAgentString(null);

        // Set Javascript Mode
        browserWebSetting.setJavaScriptEnabled(javascriptMode);
        // Set Geo Location
        browserWebSetting.setGeolocationEnabled(geoLocation);
        // Set Popups
        browserWebSetting.setJavaScriptCanOpenWindowsAutomatically(popups);
        // Set Dom Storage
        browserWebSetting.setDomStorageEnabled(domStorage);
        browserWebSetting.setDatabaseEnabled(domStorage);
        // Set Zoom
        browserWebSetting.setSupportZoom(zoom);
        browserWebSetting.setBuiltInZoomControls(zoom);
        // Set Zoom Buttons
        browserWebSetting.setDisplayZoomControls(zoomButtons);
        // Set Save Forms
        browserWebSetting.setSaveFormData(saveForms);
        // Required Settings
        browserWebSetting.setAllowContentAccess(true);
        browserWebSetting.setAllowFileAccess(true);
        browserWebSetting.setLoadWithOverviewMode(true);
        browserWebSetting.setUseWideViewPort(true);
        browserWebSetting.setNeedInitialFocus(true);

        // Default Zoom Level
        webView.setInitialScale(100);
    }
}
