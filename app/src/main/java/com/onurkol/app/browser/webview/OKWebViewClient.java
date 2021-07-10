package com.onurkol.app.browser.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.tabs.ClassesTabData;
import com.onurkol.app.browser.data.tabs.IncognitoTabData;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.tools.ScreenManager;

public class OKWebViewClient extends WebViewClient {
    // Variables
    boolean redirectLoad;
    // Classes
    TabBuilder tabBuilder;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // Get Root View
        View rootView = view.getRootView();
        // Variables
        redirectLoad=false;
        // Get Classes
        tabBuilder=TabBuilder.Build();
        // Get Elements
        EditText browserSearch=rootView.findViewById(R.id.browserSearch);

        // Set Url
        browserSearch.setText(url);

        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // Get Root View
        View rootView = view.getRootView();

        // Get Elements
        SwipeRefreshLayout browserSwipeRefresh = rootView.findViewById(R.id.browserSwipeRefresh);
        NestedScrollView browserNestedScroll = rootView.findViewById(R.id.browserNestedScroll);
        // Get WebView
        OKWebView webView=((OKWebView)view);

        // Fixed Nested Scroll Height:
        int getScreenHeigth=ScreenManager.getScreenHeight();
        int getContentHeight = webView.getContentHeight();
        // Params
        FrameLayout.LayoutParams heightWrap=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams heightMatch=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);

        // Check View Layout Params
        if(getScreenHeigth>getContentHeight) {
            // Check WebView Layout Params
            if(webView.getLayoutParams().height!=LinearLayout.LayoutParams.WRAP_CONTENT)
                webView.setLayoutParams(heightWrap);
        }
        else
            webView.setLayoutParams(heightMatch);

        if(redirectLoad){
            //...

            // Reset Scroll Position
            browserNestedScroll.scrollTo(0, 0);

            if(!webView.isIncognitoWebView) {
                // Update ScreenShot
                webView.getTabFragment().updateScreenShot();
                // New Preference Data
                TabData newData = new TabData(view.getTitle(), url);
                ClassesTabData newClassesData = new ClassesTabData(ScreenManager.getScreenshot(webView.getTabFragment().getView()));
                // Synchronize New Data
                tabBuilder.updateSyncTabData(tabBuilder.getActiveTabIndex(), newData, newClassesData, webView);
                // Add History Data
                // ...
            }
            else{
                // Update ScreenShot
                webView.getIncognitoTabFragment().updateScreenShot();
                // Update Data
                IncognitoTabData data=tabBuilder.getIncognitoTabDataList().get(tabBuilder.getActiveIncognitoTabIndex());
                data.setTitle(webView.getTitle());
                data.setUrl(webView.getUrl());
                data.setTabPreview(webView.getIncognitoTabFragment().getUpdatedScreenShot());
            }
        }
        redirectLoad=true;
        super.onPageFinished(view, url);
    }
}
