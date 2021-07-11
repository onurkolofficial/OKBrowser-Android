package com.onurkol.app.browser.webview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.onurkol.app.browser.fragments.tabs.IncognitoTabFragment;
import com.onurkol.app.browser.fragments.tabs.TabFragment;
import com.onurkol.app.browser.interfaces.webview.WebViewClientManager;

import java.util.Map;


public class OKWebView extends WebView implements WebViewClientManager {
    // Variables
    public boolean isIncognitoWebView=false,
            isRefreshing=false,onBackView=false;
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
    OKWebViewClient currentWebViewClient;
    OKWebViewChromeClient currentWebViewChromeClient;

    // WebView Client
    @Override
    public void setOKWebViewClient(OKWebViewClient webViewClient){
        currentWebViewClient=webViewClient;
        // Set Client
        setWebViewClient(webViewClient);
    }
    @Override
    public OKWebViewClient getOKWebViewClient(){
        return currentWebViewClient;
    }

    @Override
    public void setOKWebViewChromeClient(OKWebViewChromeClient webViewChromeClient){
        currentWebViewChromeClient=webViewChromeClient;
        // Set Client
        setWebChromeClient(webViewChromeClient);
    }
    @Override
    public OKWebViewChromeClient getOKWebViewChromeClient(){
        return currentWebViewChromeClient;
    }

    // Sync on Back Url
    public void syncOnBackForward(String url){
        currentWebViewClient.onPageFinished(this,url);
    }

    // Fullscreen Video
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void notifyVideoEnd(){
            // End Video
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run(){
                    if (currentWebViewChromeClient != null)
                        currentWebViewChromeClient.onHideCustomView();
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

}
