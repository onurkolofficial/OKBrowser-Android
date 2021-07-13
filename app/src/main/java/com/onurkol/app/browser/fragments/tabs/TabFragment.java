package com.onurkol.app.browser.fragments.tabs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.tools.KeyboardController;
import com.onurkol.app.browser.tools.ScreenManager;
import com.onurkol.app.browser.lib.browser.SearchEngine;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.webview.OKWebView;
import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;
import com.onurkol.app.browser.webview.WebViewConfig;
import com.onurkol.app.browser.webview.listeners.VideoFullScreen;

public class TabFragment extends Fragment {
    // Elements
    OKWebView okBrowserWebView;
    LinearLayout newTabHomeLayout, connectFailedLayout;
    EditText searchInput;
    View fragmentView, webViewLayout;
    ViewGroup webViewVideoLayout;
    // Types
    private Bitmap fragmentScreen;

    // Variables
    private int activeTabIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_new_tab, container, false);
        Activity activity=ContextManager.getManager().getContextActivity();

        // Get Elements
        okBrowserWebView=fragmentView.findViewById(R.id.okBrowserWebView);
        newTabHomeLayout=fragmentView.findViewById(R.id.newTabHomeLayout);
        searchInput=fragmentView.findViewById(R.id.newTabSearchInput);
        connectFailedLayout=fragmentView.findViewById(R.id.connectFailedLayout);
        webViewLayout=fragmentView.findViewById(R.id.webViewLayout);
        webViewVideoLayout=activity.findViewById(R.id.webViewVideoLayout);
        // Loading Video View
        View loadingView = getLayoutInflater().inflate(R.layout.layout_video_loading, null);
        // Default
        connectFailedLayout.setVisibility(View.GONE);

        // WebView Clients
        OKWebViewClient okWebViewClient=new OKWebViewClient();
        OKWebViewChromeClient okWebViewChromeClient=new OKWebViewChromeClient(webViewLayout, webViewVideoLayout, loadingView, okBrowserWebView);
        // Set WebView Config
        WebViewConfig.getInstance().setWebViewConfig(okBrowserWebView);
        // Set WebView Clients
        okBrowserWebView.setOKWebViewClient(okWebViewClient);
        okBrowserWebView.setOKWebViewChromeClient(okWebViewChromeClient);
        // Set WebView Listeners
        okBrowserWebView.getOKWebViewChromeClient().setOnToggledFullscreen(VideoFullScreen.fullscreenCallback);
        // Set WebView Fragment
        okBrowserWebView.setTabFragment(this);
        // Set Variables
        okBrowserWebView.isIncognitoWebView=false;

        // Set Listeners
        searchInput.setOnKeyListener((cView, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                // Hide New Tab Layout
                newTabHomeLayout.setVisibility(View.GONE);
                // Show WebView
                okBrowserWebView.setVisibility(View.VISIBLE);
                // Get Query
                String seQuery=SearchEngine.getInstance().getSearchEngineQuery();
                String webQuery=seQuery+searchInput.getText().toString();
                // Load Url
                okBrowserWebView.loadUrl(webQuery);
                // Hide Keyboard
                KeyboardController.hideKeyboard(cView);
            }
            return false;
        });

        return fragmentView;
    }

    public void setTabIndex(int tabIndex){
        activeTabIndex=tabIndex;
    }
    public int getTabIndex(){
        return activeTabIndex;
    }

    public OKWebView getWebView(){
        return okBrowserWebView;
    }

    @Nullable
    @Override
    public View getView() {
        if(fragmentView!=null)
            return fragmentView;
        else
            return super.getView();
    }

    public void updateScreenShot(){
        fragmentScreen=ScreenManager.getScreenshot(fragmentView.findViewById(R.id.tabPageRootLayout));
    }
    public Bitmap getUpdatedScreenShot(){
        return fragmentScreen;
    }

    public void refreshLoadView(){
        TabBuilder tabBuilder = TabBuilder.Build();
        // Get Tab Data
        TabData tabData=tabBuilder.getTabDataList().get(getTabIndex());
        String tabDataUrl=tabData.getUrl();
        String webViewUrl=okBrowserWebView.getUrl();

        // Check Load Saved Url
        if(tabDataUrl.equals("")){
            // Show New Tab Layout
            newTabHomeLayout.setVisibility(View.VISIBLE);
            // Hide WebView
            okBrowserWebView.setVisibility(View.GONE);
        }
        else {
            if(!tabDataUrl.equals(webViewUrl)) {
                // Hide New Tab Layout
                newTabHomeLayout.setVisibility(View.GONE);
                // Show WebView
                okBrowserWebView.setVisibility(View.VISIBLE);
                // Load Url
                okBrowserWebView.loadUrl(tabData.getUrl());
            }
        }
    }
}