package com.onurkol.app.browser.fragments.tabs;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.tools.KeyboardController;
import com.onurkol.app.browser.tools.ScreenManager;
import com.onurkol.app.browser.lib.browser.SearchEngine;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.webview.OKWebView;
import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;
import com.onurkol.app.browser.webview.WebViewConfig;

public class TabFragment extends Fragment {
    // Elements
    OKWebView okBrowserWebView;
    LinearLayout newTabHomeLayout, connectFailedLayout;
    EditText searchInput;
    View fragmentView;

    // Types
    private Bitmap fragmentScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_new_tab, container, false);

        // Get Elements
        okBrowserWebView=fragmentView.findViewById(R.id.okBrowserWebView);
        newTabHomeLayout=fragmentView.findViewById(R.id.newTabHomeLayout);
        searchInput=fragmentView.findViewById(R.id.newTabSearchInput);
        connectFailedLayout=fragmentView.findViewById(R.id.connectFailedLayout);
        // Default
        connectFailedLayout.setVisibility(View.GONE);

        // Set WebView Config
        WebViewConfig.getInstance().setWebViewConfig(okBrowserWebView);
        // Set WebView Clients
        okBrowserWebView.setOKWebViewClient(new OKWebViewClient());
        okBrowserWebView.setOKWebViewChromeClient(new OKWebViewChromeClient());
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

        // Set Active Tab
        thisTabSetToActive();

        return fragmentView;
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
        TabData tabData=tabBuilder.getSavedTabList().get(tabBuilder.getActiveTabIndex());
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

    private void thisTabSetToActive(){
        TabBuilder tabBuilder=TabBuilder.Build();
        // Set Active Tab Data
        tabBuilder.setActiveTabFragment(this);
        tabBuilder.setActiveTabWebView(okBrowserWebView);
    }
}