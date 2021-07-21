package com.onurkol.app.browser.webview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.onurkol.app.browser.fragments.browser.tabs.IncognitoTabFragment;
import com.onurkol.app.browser.fragments.browser.tabs.TabFragment;
import com.onurkol.app.browser.interfaces.webview.WebViewClientManager;

import java.util.Map;


public class OKWebView extends WebView implements WebViewClientManager {
    // Variables
    public boolean isIncognitoWebView=false,
            isRefreshing=false,isLoading=false,onBackView=false,
            isDesktopMode=false;
    private boolean addedJavascriptInterface;
    // Fragments
    private TabFragment webviewTabFragment;
    private IncognitoTabFragment webviewIncognitoTabFragment;
    // Get Tab Fragments
    public void setTabFragment(TabFragment fragment){
        webviewTabFragment=fragment;
    }
    public TabFragment getTabFragment(){
        return webviewTabFragment;
    }
    // Get Incognito Tab Fragments
    public void setIncognitoTabFragment(IncognitoTabFragment fragment){
        webviewIncognitoTabFragment=fragment;
    }
    public IncognitoTabFragment getIncognitoTabFragment(){
        return webviewIncognitoTabFragment;
    }

    // WebView Clients
    OKWebViewClient okWebViewClient;
    OKWebViewChromeClient okWebViewChromeClient;

    // WebView Client
    @Override
    public void setOKWebViewClient(OKWebViewClient webViewClient){
        okWebViewClient=webViewClient;
        // Set Client
        setWebViewClient(webViewClient);
    }
    @Override
    public OKWebViewClient getOKWebViewClient(){
        return okWebViewClient;
    }

    @Override
    public void setOKWebViewChromeClient(OKWebViewChromeClient webViewChromeClient){
        okWebViewChromeClient=webViewChromeClient;
        // Set Client
        setWebChromeClient(webViewChromeClient);
    }
    @Override
    public OKWebViewChromeClient getOKWebViewChromeClient(){
        return okWebViewChromeClient;
    }

    // Sync on Back Url
    public void syncOnBackForward(String url){
        okWebViewClient.updateSyncForWeb(this, url);
    }

    // Fullscreen Video
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void notifyVideoEnd(){
            // End Video
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run(){
                    if (okWebViewChromeClient != null)
                        okWebViewChromeClient.onHideCustomView();
                }
            });
        }
    }

    public OKWebView(@NonNull Context context) {
        super(context);
        addedJavascriptInterface = false;
    }
    public OKWebView(@NonNull Context context, AttributeSet attrs){
        super(context, attrs);
        addedJavascriptInterface = false;
    }
    public OKWebView(@NonNull Context context, AttributeSet attrs, int defStyle){
        super(context, attrs);
        addedJavascriptInterface = false;
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        addJavascriptInterface();
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(@Nullable String baseUrl, @NonNull String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
        addJavascriptInterface();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadUrl(@NonNull String url) {
        addJavascriptInterface();
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(@NonNull String url, @NonNull Map<String, String> additionalHttpHeaders) {
        addJavascriptInterface();
        super.loadUrl(url, additionalHttpHeaders);
    }

    public boolean isVideoFullscreen(){
        return okWebViewChromeClient != null && okWebViewChromeClient.isVideoFullscreen();
    }

    private void addJavascriptInterface() {
        if (!addedJavascriptInterface){
            // Add javascript interface to be called when the video ends (must be done before page load)
            addJavascriptInterface(new JavascriptInterface(), "OKWebView"); // Must match Javascript interface name of VideoEnabledWebChromeClient
            addedJavascriptInterface = true;
        }
    }
}
