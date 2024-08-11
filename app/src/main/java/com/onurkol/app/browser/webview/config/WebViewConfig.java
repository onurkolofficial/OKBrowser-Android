package com.onurkol.app.browser.webview.config;

import android.content.Context;
import android.webkit.WebSettings;

import com.onurkol.app.browser.controller.PreferenceController;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.interfaces.BrowserDataInterface;
import com.onurkol.app.browser.webview.OKWebView;

public class WebViewConfig implements BrowserDataInterface {
    public static void apply(Context context, OKWebView webView){
        PreferenceController preferenceController=PreferenceController.getController();
        TabController tabController=TabController.getController(context);
        // Set WebView Settings
        webView.setInitialScale(100);

        // Get WebSettings
        WebSettings browserWebSetting=webView.getSettings();

        // Get Settings
        boolean javascriptMode=preferenceController.getBoolean(KEY_JAVASCRIPT_MODE),
                appCache=preferenceController.getBoolean(KEY_APP_CACHE),
                geoLocation=preferenceController.getBoolean(KEY_GEO_LOCATION),
                popups=preferenceController.getBoolean(KEY_POPUPS),
                domStorage=preferenceController.getBoolean(KEY_DOM_STORAGE),
                zoom=preferenceController.getBoolean(KEY_ZOOM),
                zoomButtons=preferenceController.getBoolean(KEY_ZOOM_BUTTONS),
                saveForms=preferenceController.getBoolean(KEY_SAVE_FORMS);

        // Set Javascript Mode
        browserWebSetting.setJavaScriptEnabled(javascriptMode);
        // Set Geo Location
        browserWebSetting.setGeolocationEnabled(geoLocation);
        // Set Popups
        browserWebSetting.setJavaScriptCanOpenWindowsAutomatically(popups);

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

        if(tabController.getCurrentTab().isIncognito()){
            // Set App Cache (Incognito)
            //browserWebSetting.setAppCacheEnabled(false);
            // Set Dom Storage (Incognito)
            browserWebSetting.setDomStorageEnabled(false);
            browserWebSetting.setDatabaseEnabled(false);
        }
        else{
            // Set App Cache
            //browserWebSetting.setAppCacheEnabled(appCache);
            // Set Dom Storage
            browserWebSetting.setDomStorageEnabled(domStorage);
            browserWebSetting.setDatabaseEnabled(domStorage);
        }
    }
}
