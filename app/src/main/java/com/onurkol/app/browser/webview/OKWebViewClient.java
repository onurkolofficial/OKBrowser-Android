package com.onurkol.app.browser.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.data.browser.HistoryData;
import com.onurkol.app.browser.data.browser.tabs.ClassesTabData;
import com.onurkol.app.browser.data.browser.tabs.IncognitoTabData;
import com.onurkol.app.browser.data.browser.tabs.TabData;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.HistoryManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.tools.DateManager;
import com.onurkol.app.browser.tools.JavascriptManager;
import com.onurkol.app.browser.tools.ScreenManager;

public class OKWebViewClient extends WebViewClient {
    Activity activity;
    // Variables
    boolean redirectLoad;
    // Classes
    TabBuilder tabBuilder;
    JavascriptManager jsManager;
    // Elements
    EditText browserSearch;
    LinearLayout connectFailedLayout;

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // Get Views
        activity=ContextManager.getManager().getContextActivity();
        View rootView=view.getRootView();
        // Variables
        redirectLoad=false;
        // Get Classes
        tabBuilder=TabBuilder.Build();
        jsManager=JavascriptManager.getManager();
        // Get Elements
        browserSearch=activity.findViewById(R.id.browserSearch);
        connectFailedLayout=rootView.findViewById(R.id.connectFailedLayout);
        // Get WebView
        OKWebView webView=((OKWebView)view);
        webView.isLoading=true;

        if(!webView.isIncognitoWebView) {
            // New Preference Data
            TabData newData = new TabData(view.getTitle(), url);
            ClassesTabData newClassesData = new ClassesTabData(webView.getTabFragment(), ScreenManager.getScreenshot(webView.getTabFragment().getView()));
            // Synchronize New Data for Fixed re-change tab in show web page
            tabBuilder.updateSyncTabData(webView.getTabFragment().getTabIndex(), newData, newClassesData);
        }
        // Remove Error Page
        if(connectFailedLayout.getVisibility()==View.VISIBLE) {
            connectFailedLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
        // Set Url
        browserSearch.setText(url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        if(activity==null){
            activity=ContextManager.getManager().getContextActivity();
            browserSearch=activity.findViewById(R.id.browserSearch);
        }
        // Set youtube video url
        if(url.contains("watch?")) {
            browserSearch.setText(url);
            // Check WebView Height (some bugs)
            //checkWebViewHeight((OKWebView)view);
            // Update for Resource page
            updateSyncForWeb((OKWebView)view, url);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // Get Views
        Activity activity=ContextManager.getManager().getContextActivity();

        // Get Elements
        SwipeRefreshLayout browserSwipeRefresh = activity.findViewById(R.id.browserSwipeRefresh);
        NestedScrollView browserNestedScroll = activity.findViewById(R.id.browserNestedScroll);
        // Get WebView
        OKWebView webView=((OKWebView)view);

        // Stop Swipe Refresh
        if(browserSwipeRefresh!=null)
            browserSwipeRefresh.setRefreshing(false);

        // Check WebView Height (some bugs)
        checkWebViewHeight(webView);

        if(redirectLoad){
            webView.isLoading=false;
            webView.isRefreshing=false;

            if(tabBuilder==null)
                tabBuilder=TabBuilder.Build();

            // Reset Scroll Position
            if(browserNestedScroll!=null) {
                browserNestedScroll.scrollTo(0, 0);
                view.scrollTo(0, 0);
            }
            // Update Web Page
            updateSyncForWeb(webView, webView.getUrl());
            // Add History Data
            if(!webView.isIncognitoWebView) {
                HistoryData historyData = new HistoryData(webView.getTitle(), webView.getUrl(), DateManager.getDate());
                HistoryManager.getInstance().newHistory(historyData);
            }
        }
        redirectLoad=true;
        // Check Javascript Manager
        if(jsManager==null)
            jsManager=JavascriptManager.getManager();
        // Exec Javascript
        jsManager.exec(webView,"var w=window;" +
                "function wrappedOnDownFunc(e){" +
                "  w._touchtarget = e.touches[0].target;" +
                "}" +
                "w.addEventListener('touchstart',wrappedOnDownFunc);");

        super.onPageFinished(view, url);
    }

    private void checkWebViewHeight(OKWebView webView){
        // <BUG> Clicked youtube menu button, webview height set to :wrap

        // Fixed Nested Scroll Height:
        int getScreenHeigth = ScreenManager.getScreenHeight();
        int getContentHeight = webView.getContentHeight();
        // Params
        LinearLayout.LayoutParams heightWrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams heightMatch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Check View Layout Params
        if (getScreenHeigth >= getContentHeight || getContentHeight==0)
            webView.setLayoutParams(heightMatch);
        else
            webView.setLayoutParams(heightWrap);
    }

    public void updateSyncForWeb(OKWebView webView, String url){
        if(tabBuilder==null)
            tabBuilder=TabBuilder.Build();
        if(!webView.isIncognitoWebView) {
            // Update ScreenShot
            webView.getTabFragment().updateScreenShot();
            // New Preference Data
            TabData newData = new TabData(webView.getTitle(), url);
            ClassesTabData newClassesData = new ClassesTabData(webView.getTabFragment(), ScreenManager.getScreenshot(webView.getTabFragment().getView()));
            // RE-Synchronize New Data
            tabBuilder.updateSyncTabData(webView.getTabFragment().getTabIndex(), newData, newClassesData);
        }
        else{
            // Update ScreenShot
            webView.getIncognitoTabFragment().updateScreenShot();
            // Update Data
            IncognitoTabData data=tabBuilder.getIncognitoTabDataList().get(tabBuilder.getActiveIncognitoFragment().getTabIndex());
            data.setTitle(webView.getTitle());
            data.setUrl(url);
            data.setTabPreview(webView.getIncognitoTabFragment().getUpdatedScreenShot());
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        // Get Root View
        View rootView=view.getRootView();

        // Get Elements
        LinearLayout connectFailedLayout=rootView.findViewById(R.id.connectFailedLayout);
        // Get WebView
        OKWebView webView=((OKWebView)view);

        // Hide WebView
        webView.setVisibility(View.GONE);
        connectFailedLayout.setVisibility(View.VISIBLE);

        super.onReceivedError(view, request, error);
    }
}
