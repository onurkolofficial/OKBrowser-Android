package com.onurkol.app.browser.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.activity.SettingsActivity;
import com.onurkol.app.browser.activity.browser.BookmarkActivity;
import com.onurkol.app.browser.activity.browser.HistoryActivity;
import com.onurkol.app.browser.activity.browser.core.DownloadsActivity;
import com.onurkol.app.browser.data.browser.BookmarkData;
import com.onurkol.app.browser.interfaces.BrowserDefaultSettings;
import com.onurkol.app.browser.lib.AppPreferenceManager;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.lib.browser.BookmarkManager;
import com.onurkol.app.browser.lib.browser.tabs.TabBuilder;
import com.onurkol.app.browser.lib.browser.tabs.core.ToolbarTabCounter;
import com.onurkol.app.browser.webview.OKWebView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MenuToolbarMain {
    static PopupWindow popupWindow;

    // Classes
    static WeakReference<TabBuilder> tabBuilderStatic;
    static WeakReference<ToolbarTabCounter> tabCounterStatic;
    static WeakReference<AppPreferenceManager> prefManagerStatic;
    static WeakReference<BookmarkManager> bookmarkManagerStatic;

    static WeakReference<Context> contextStatic;

    // Intents
    static Intent historyIntent,downloadsIntent,bookmarksIntent,settingsIntent;

    public static PopupWindow getMenu(){
        // Init Context
        ContextManager contextManager=ContextManager.getManager();
        Context context=contextManager.getContext();

        contextStatic=new WeakReference<>(context);

        // Get Classes
        TabBuilder tabBuilder=TabBuilder.Build();
        ToolbarTabCounter tabCounter=new ToolbarTabCounter();
        AppPreferenceManager prefManager=AppPreferenceManager.getInstance();
        BookmarkManager bookmarkManager=BookmarkManager.getInstance();
        // Get Static Classes
        tabBuilderStatic=new WeakReference<>(tabBuilder);
        tabCounterStatic=new WeakReference<>(tabCounter);
        prefManagerStatic=new WeakReference<>(prefManager);
        bookmarkManagerStatic=new WeakReference<>(bookmarkManager);

        // Get Intents
        historyIntent=new Intent(context, HistoryActivity.class);
        downloadsIntent=new Intent(context, DownloadsActivity.class);
        bookmarksIntent=new Intent(context, BookmarkActivity.class);
        settingsIntent=new Intent(context, SettingsActivity.class);

        // Elements
        LinearLayout exitBrowserLayoutButton, newTabLayoutButton, newIncognitoTabLayoutButton,
                closeAllTabsLayoutButton,desktopModeLayoutCheckbox, findInPageLayoutButton,
                bookmarksLayoutButton, settingsLayoutButton;
        ImageButton menuForwardButton, menuRefreshButton, addBookmarkButton,downloadsButton,
                historyButton;
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
        findInPageLayoutButton=view.findViewById(R.id.findInPageLayoutButton);
        bookmarksLayoutButton=view.findViewById(R.id.bookmarksLayoutButton);
        settingsLayoutButton=view.findViewById(R.id.settingsLayoutButton);
        addBookmarkButton=view.findViewById(R.id.addBookmarkButton);
        downloadsButton=view.findViewById(R.id.downloadsButton);
        historyButton=view.findViewById(R.id.historyButton);

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
        bookmarksLayoutButton.setOnClickListener(v -> {
            context.startActivity(bookmarksIntent);
            popupWindow.dismiss();
        });
        settingsLayoutButton.setOnClickListener(v -> {
            context.startActivity(settingsIntent);
            popupWindow.dismiss();
        });
        downloadsButton.setOnClickListener(v -> {
            context.startActivity(downloadsIntent);
            popupWindow.dismiss();
        });
        historyButton.setOnClickListener(v -> {
            context.startActivity(historyIntent);
            popupWindow.dismiss();
        });
        findInPageLayoutButton.setOnClickListener(findPageButtonListener);
        desktopModeLayoutCheckbox.setOnClickListener(desktopModeButtonListener);
        addBookmarkButton.setOnClickListener(addBookmarkButtonListener);

        // Check Show Some Menu Buttons
        if(getWebView.getUrl()==null || getWebView.getUrl().equals("") || getWebView.onBackView) {
            desktopModeLayoutCheckbox.setVisibility(View.GONE);
            findInPageLayoutButton.setVisibility(View.GONE);
        }
        else {
            desktopModeLayoutCheckbox.setVisibility(View.VISIBLE);
            findInPageLayoutButton.setVisibility(View.VISIBLE);
        }

        // Check Refresh Button Icon
        if(getWebView.isRefreshing || getWebView.isLoading)
            menuRefreshButton.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_close_24));
        else
            menuRefreshButton.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_refresh_24));

        // Check Desktop Mode Button
        desktopModeCheckBox.setChecked(getWebView.isDesktopMode);

        // Check Bookmarks
        if(bookmarkManager.getSavedBookmarkData().size()<=0){
            addBookmarkButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_border_24));
        }
        else{
            int bookmarkCount=bookmarkManager.getSavedBookmarkData().size();
            int i = 0;
            while (i < bookmarkCount) {
                // Get Data
                BookmarkData data=bookmarkManager.getSavedBookmarkData().get(i);
                // Check Data
                if(getWebView.getUrl().equals(data.getUrl())){
                    addBookmarkButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_24));
                    addBookmarkButton.setColorFilter(ContextCompat.getColor(context,R.color.primary));
                    break;
                } else {
                    addBookmarkButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_border_24));
                }
                i++;
            }
        }

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
        tabBuilderStatic.get().createNewTab();
        // Show Incognito Icon
        incognitoIcon.setVisibility(View.GONE);
        // Update Tab Counts
        browserTabListButton.setImageDrawable(tabCounterStatic.get().getTabCountDrawable());
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
        tabBuilderStatic.get().createNewIncognitoTab();
        // Show Incognito Icon
        incognitoIcon.setVisibility(View.VISIBLE);
        // Update Tab Counts
        browserTabListButton.setImageDrawable(tabCounterStatic.get().getIncognitoTabCountDrawable());
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
        for(int in=0; in<tabBuilderStatic.get().getTabDataList().size(); in++)
            tabBuilderStatic.get().removeFragment(tabBuilderStatic.get().getTabFragmentList().get(in));
        for(int ii=0; ii<tabBuilderStatic.get().getIncognitoTabDataList().size(); ii++)
            tabBuilderStatic.get().removeFragment(tabBuilderStatic.get().getIncognitoTabFragmentList().get(ii));
        // Clear Data List
        tabBuilderStatic.get().getTabFragmentList().clear();
        tabBuilderStatic.get().getTabDataList().clear();
        tabBuilderStatic.get().getClassesTabDataList().clear();
        tabBuilderStatic.get().getIncognitoTabFragmentList().clear();
        tabBuilderStatic.get().getIncognitoTabDataList().clear();
        // SYNC for Tab List
        tabBuilderStatic.get().saveTabListPreference(tabBuilderStatic.get().getTabDataList());
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
        // Elements
        View fragmentView;
        OKWebView webView;
        // Check Url Back, Layout & Tabs
        if(tabBuilderStatic.get().getActiveTabFragment()!=null){
            // Check Tab WebView Layout
            // Get WebView & Fragment View
            fragmentView=tabBuilderStatic.get().getActiveTabFragment().getView();
            webView=tabBuilderStatic.get().getActiveTabFragment().getWebView();
            // Check Exists WebView History
            if(webView.canGoForward() && webView.getVisibility()==View.VISIBLE)
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
            fragmentView=tabBuilderStatic.get().getActiveIncognitoFragment().getView();
            webView=tabBuilderStatic.get().getActiveIncognitoFragment().getWebView();
            // Check Exists WebView History
            if(webView.canGoForward() && webView.getVisibility()==View.VISIBLE)
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
        if(tabBuilderStatic.get().getActiveTabFragment()!=null)
            webView = tabBuilderStatic.get().getActiveTabFragment().getWebView();
        else
            webView = tabBuilderStatic.get().getActiveIncognitoFragment().getWebView();

        if(webView.isRefreshing || webView.isLoading){
            webView.isRefreshing=false;
            webView.isLoading=false;
            webView.stopLoading();
        }
        else{
            if(webView.getUrl()!=null) {
                // Refresh Current WebView
                webView.isRefreshing = true;
                webView.reload();
            }
        }
        // Dismiss Popup
        popupWindow.dismiss();
    };
    // Browser Find Page Button
    static View.OnClickListener findPageButtonListener=view -> {
        // Get Context Activity
        Activity activity=ContextManager.getManager().getContextActivity();

        // Hide Main Toolbar & Show Find
        activity.findViewById(R.id.includeTabToolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.includeFindToolbar).setVisibility(View.VISIBLE);

        // Dismiss Popup
        popupWindow.dismiss();
    };
    // Browser Desktop Page Button
    static View.OnClickListener desktopModeButtonListener=view -> {
        // Get WebView
        OKWebView webView;
        if(tabBuilderStatic.get().getActiveTabFragment()!=null)
            webView = tabBuilderStatic.get().getActiveTabFragment().getWebView();
        else
            webView = tabBuilderStatic.get().getActiveIncognitoFragment().getWebView();

        // Get Preference Data
        if(webView.isDesktopMode) {
            webView.isDesktopMode=false;
            webView.getSettings().setUserAgentString(null);
        }
        else {
            webView.isDesktopMode=true;
            webView.getSettings().setUserAgentString(BrowserDefaultSettings.DESKTOP_USER_AGENT);
        }
        // Reload Page
        webView.reload();
        // Dismiss Popup
        popupWindow.dismiss();
    };
    // Add Bookmark Button
    static View.OnClickListener addBookmarkButtonListener=view -> {
        // Get WebView & Check Home Layout
        boolean isHomePage;
        OKWebView webView;
        if(tabBuilderStatic.get().getActiveTabFragment()!=null) {
            webView = tabBuilderStatic.get().getActiveTabFragment().getWebView();
            View fragView=tabBuilderStatic.get().getActiveTabFragment().getView();
            if(fragView.findViewById(R.id.newTabHomeLayout).getVisibility()==View.VISIBLE)
                isHomePage=true;
            else
                isHomePage=false;
        }
        else {
            webView = tabBuilderStatic.get().getActiveIncognitoFragment().getWebView();
            View fragView=tabBuilderStatic.get().getActiveIncognitoFragment().getView();
            if(fragView.findViewById(R.id.incognitoHomeLayout).getVisibility()==View.VISIBLE)
                isHomePage=true;
            else
                isHomePage=false;
        }

        // Get Data
        ArrayList<BookmarkData> dataList=bookmarkManagerStatic.get().getSavedBookmarkData();
        // Check Valid Url
        if(!isHomePage) {
            boolean isBookmarkNotFound = true;
            int i = 0;
            while (i < dataList.size()) {
                // Get Data
                String bookmarkUrl = dataList.get(i).getUrl();
                // Get WebView Data
                String webUrl = webView.getUrl();
                // Check Url
                if (webUrl.equals(bookmarkUrl)) {
                    // Remove Bookmark
                    dataList.remove(i);
                    // Save Preference Data
                    bookmarkManagerStatic.get().saveBookmarkListPreference(dataList);

                    // Set bookmark Status
                    isBookmarkNotFound = false;
                    break;
                } else
                    isBookmarkNotFound = true;
                i++;
            }
            // Check Exist Bookmark
            if (isBookmarkNotFound) {
                // Add Bookmark
                bookmarkManagerStatic.get().newBookmark(new BookmarkData(webView.getTitle(), webView.getUrl()));
                // Show Message
                Toast.makeText(contextStatic.get(), contextStatic.get().getString(R.string.bookmark_added_text), Toast.LENGTH_SHORT).show();
            }
        }
        // Dismiss Popup
        popupWindow.dismiss();
    };
}
