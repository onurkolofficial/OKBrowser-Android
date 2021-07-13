package com.onurkol.app.browser.menu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.lib.tabs.core.ToolbarTabCounter;
import com.onurkol.app.browser.webview.OKWebView;

public class MenuToolbarMain {
    static PopupWindow popupWindow;

    // Classes
    static TabBuilder tabBuilder;
    static ToolbarTabCounter tabCounter;

    public static PopupWindow getMenu(){
        // Init Context
        ContextManager contextManager=ContextManager.getManager();
        Context context=contextManager.getContext();

        // Get Classes
        tabBuilder=TabBuilder.Build();
        tabCounter=new ToolbarTabCounter();

        // Elements
        LinearLayout exitBrowserLayoutButton, newTabLayoutButton, newIncognitoTabLayoutButton,
                closeAllTabsLayoutButton,desktopModeLayoutCheckbox;
        ImageButton menuForwardButton, menuRefreshButton;
        CheckBox desktopModeCheckBox;

        // Get Popup Window
        popupWindow=new PopupWindow(context);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.menu_toolbar_main, null);

        // Popup Window Settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(640);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setElevation(20);

        // Get Elements
        exitBrowserLayoutButton=view.findViewById(R.id.exitBrowserLayoutButton);
        newTabLayoutButton=view.findViewById(R.id.newTabLayoutButton);
        newIncognitoTabLayoutButton=view.findViewById(R.id.newIncognitoTabLayoutButton);
        closeAllTabsLayoutButton=view.findViewById(R.id.closeAllTabsLayoutButton);
        menuForwardButton=view.findViewById(R.id.menuForwardButton);
        menuRefreshButton=view.findViewById(R.id.menuRefreshButton);
        desktopModeLayoutCheckbox=view.findViewById(R.id.desktopModeLayoutCheckbox);
        desktopModeCheckBox=view.findViewById(R.id.desktopModeCheckBox);

        // Get WebView
        OKWebView getWebView;
        if(tabBuilder.getActiveTabFragment()!=null)
            getWebView=tabBuilder.getActiveTabFragment().getWebView();
        else
            getWebView=tabBuilder.getActiveIncognitoFragment().getWebView();

        // Set Listeners
        exitBrowserLayoutButton.setOnClickListener(exitButtonListener);
        newTabLayoutButton.setOnClickListener(newTabButtonListener);
        newIncognitoTabLayoutButton.setOnClickListener(newIncognitoTabButtonListener);
        closeAllTabsLayoutButton.setOnClickListener(closeAllTabsButtonListener);
        menuForwardButton.setOnClickListener(forwardButtonListener);
        menuRefreshButton.setOnClickListener(refreshButtonListener);

        // Check Show Desktop Mode in Valid Web
        if(getWebView.getUrl()==null || getWebView.getUrl().equals("") || getWebView.onBackView)
            desktopModeLayoutCheckbox.setVisibility(View.GONE);
        else
            desktopModeLayoutCheckbox.setVisibility(View.VISIBLE);

        return popupWindow;
    }
    // Listeners
    // Exit Button
    static View.OnClickListener exitButtonListener=view -> {
        // Exit App
        System.exit(0);
    };
    // New Tab Button
    static View.OnClickListener newTabButtonListener=view -> {
        // Get Context Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Get Elements
        ImageView incognitoIcon=activity.findViewById(R.id.incognitoIcon);
        ImageButton browserTabListButton=activity.findViewById(R.id.browserTabListButton);
        EditText browserSearch=activity.findViewById(R.id.browserSearch);

        // New Tab
        tabBuilder.createNewTab();
        // Show Incognito Icon
        incognitoIcon.setVisibility(View.GONE);
        // Update Tab Counts
        browserTabListButton.setImageDrawable(tabCounter.getTabCountDrawable());
        // Clear Url for New Tab
        browserSearch.setText("");
        // Dismiss Popup
        popupWindow.dismiss();
    };
    // New Incognito Tab Button
    static View.OnClickListener newIncognitoTabButtonListener=view -> {
        // Get Context Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Get Elements
        ImageView incognitoIcon=activity.findViewById(R.id.incognitoIcon);
        ImageButton browserTabListButton=activity.findViewById(R.id.browserTabListButton);
        EditText browserSearch=activity.findViewById(R.id.browserSearch);

        // New Incognito Tab
        tabBuilder.createNewIncognitoTab();
        // Show Incognito Icon
        incognitoIcon.setVisibility(View.VISIBLE);
        // Update Tab Counts
        browserTabListButton.setImageDrawable(tabCounter.getIncognitoTabCountDrawable());
        // Clear Url for New Tab
        browserSearch.setText("");
        // Dismiss Popup
        popupWindow.dismiss();
    };
    // Close All Tabs Button
    static View.OnClickListener closeAllTabsButtonListener=view -> {
        // Get Context Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Remove Views
        for(int in=0; in<tabBuilder.getTabDataList().size(); in++)
            tabBuilder.removeFragment(tabBuilder.getTabFragmentList().get(in));
        for(int ii=0; ii<tabBuilder.getIncognitoTabDataList().size(); ii++)
            tabBuilder.removeFragment(tabBuilder.getIncognitoTabFragmentList().get(ii));
        // Clear Data List
        tabBuilder.getTabFragmentList().clear();
        tabBuilder.getTabDataList().clear();
        tabBuilder.getClassesTabDataList().clear();
        tabBuilder.getIncognitoTabFragmentList().clear();
        tabBuilder.getIncognitoTabDataList().clear();
        // SYNC for Tab List
        tabBuilder.saveTabListPreference(tabBuilder.getTabDataList());
        // Change Toolbar
        activity.findViewById(R.id.includeNoTabToolbar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.includeTabToolbar).setVisibility(View.GONE);
        // Dismiss Popup
        popupWindow.dismiss();
    };
    // Browser Forward Button
    static View.OnClickListener forwardButtonListener=view -> {
        // Get Context Activity
        Activity activity=ContextManager.getManager().getContextActivity();
        // Get Elements
        EditText browserSearch=activity.findViewById(R.id.browserSearch);
        // Variables
        String forwardUrl="";
        // Get Views
        View fragmentView;
        OKWebView webView;
        // Check Url Back, Layout & Tabs
        if(tabBuilder.getActiveTabFragment()!=null){
            // Check Tab WebView Layout
            // Get WebView & Fragment View
            fragmentView=tabBuilder.getActiveTabFragment().getView();
            webView=tabBuilder.getActiveTabFragment().getWebView();
            // Check Exists WebView History
            if(webView.canGoForward())
                webView.goForward();
            else{
                // Show WebView & Hide Page Layout
                webView.setVisibility(View.VISIBLE);
                (fragmentView.findViewById(R.id.newTabHomeLayout)).setVisibility(View.GONE);
            }
            // Sync Tab Data
            webView.syncOnBackForward(webView.getUrl());
        }
        else{
            // Check Incognito WebView Layout
            fragmentView=tabBuilder.getActiveIncognitoFragment().getView();
            webView=tabBuilder.getActiveIncognitoFragment().getWebView();
            // Check Exists WebView History
            if(webView.canGoForward())
                webView.goForward();
            else{
                // Show WebView & Hide Page Layout
                webView.setVisibility(View.VISIBLE);
                (fragmentView.findViewById(R.id.incognitoHomeLayout)).setVisibility(View.GONE);
            }
        }
        forwardUrl=webView.getUrl();
        // Show Url
        browserSearch.setText(forwardUrl);
        // Dismiss Popup
        popupWindow.dismiss();
    };
    // Browser Refresh Button
    static View.OnClickListener refreshButtonListener=view -> {
        // Get WebView
        OKWebView webView;
        if(tabBuilder.getActiveTabFragment()!=null)
            webView = tabBuilder.getActiveTabFragment().getWebView();
        else
            webView = tabBuilder.getActiveIncognitoFragment().getWebView();
        // Refresh Current WebView
        webView.isRefreshing=true;
        webView.reload();
        // Dismiss Popup
        popupWindow.dismiss();
    };
}
