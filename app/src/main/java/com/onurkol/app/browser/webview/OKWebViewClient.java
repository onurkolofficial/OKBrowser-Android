package com.onurkol.app.browser.webview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.onurkol.app.browser.tools.ScreenManager;

public class OKWebViewClient extends WebViewClient {
    Activity activity;
    // Variables
    boolean redirectLoad;
    String syncUrl;
    // Classes
    TabBuilder tabBuilder;
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
        syncUrl=url;
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
            syncUrl=url;
            // Check WebView Height (some bugs)
            checkWebViewHeight((OKWebView)view);
            // Update for Resource page
            updateSyncForWeb((OKWebView)view);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        /*
        // Youtube App
        Uri uri = Uri.parse(url);
        if (uri.getHost().contains("youtube.com/watch")) {
            IntentUtils.viewYoutube(mActivity, url);
            return true;
        }
         */
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // Get Views
        Activity activity=ContextManager.getManager().getContextActivity();
        View rootView=view.getRootView();

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
            updateSyncForWeb(webView);
            // Add History Data
            if(!webView.isIncognitoWebView) {
                HistoryData historyData = new HistoryData(webView.getTitle(), webView.getUrl(), DateManager.getDate());
                HistoryManager.getInstance().newHistory(historyData);
            }
        }
        redirectLoad=true;
        super.onPageFinished(view, url);
    }

    private void checkWebViewHeight(OKWebView webView){
        // Fixed Nested Scroll Height:
        int getScreenHeigth=ScreenManager.getScreenHeight();
        int getContentHeight=webView.getContentHeight();
        // Params
        FrameLayout.LayoutParams heightWrap=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,getContentHeight);
        FrameLayout.LayoutParams heightMatch=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);

        // Check View Layout Params
        if(getScreenHeigth>=getContentHeight)
            webView.setLayoutParams(heightMatch);
        else
            webView.setLayoutParams(heightWrap);
    }

    public void updateSyncForWeb(OKWebView webView){
        if(tabBuilder==null)
            tabBuilder=TabBuilder.Build();
        if(!webView.isIncognitoWebView) {
            // Update ScreenShot
            webView.getTabFragment().updateScreenShot();
            // New Preference Data
            String saveUrl=((webView.getVisibility()!=View.GONE) ? syncUrl : "");
            TabData newData = new TabData(webView.getTitle(), saveUrl);
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
            data.setUrl(syncUrl);
            data.setTabPreview(webView.getIncognitoTabFragment().getUpdatedScreenShot());
        }
    }

    /*
    // Start Youtube App
    public static void viewYoutube(Context context, String url) {
        IntentUtils.viewWithPackageName(context, url, "com.google.android.youtube");
    }

    public static void viewWithPackageName(Context context, String url, String packageName) {
        try {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (isAppInstalled(context, packageName)) {
                viewIntent.setPackage(packageName);
            }
            context.startActivity(viewIntent);
        } catch (Exception e) {
            Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(viewIntent);
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
        }
        return false;
    }
     */

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
