package com.onurkol.app.browser.fragments.browser.tabs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.interfaces.browser.tabs.TabSettings;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.menu.webview.MenuWebViewContext;
import com.onurkol.app.browser.tools.JavascriptManager;
import com.onurkol.app.browser.tools.ScreenManager;
import com.onurkol.app.browser.webview.OKWebView;
import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;
import com.onurkol.app.browser.webview.WebViewConfig;
import com.onurkol.app.browser.webview.listeners.DownloadFileListener;
import com.onurkol.app.browser.webview.listeners.VideoFullScreen;

public class IncognitoTabFragment extends Fragment {
    // Elements
    OKWebView okBrowserIncognitoWebView;
    LinearLayout incognitoHomeLayout, connectFailedLayout;
    View fragmentView, webViewLayout;
    ViewGroup webViewVideoLayout;

    // Types
    private Bitmap fragmentScreen;

    // Variables
    private int activeTabIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_new_incognito_tab, container, false);
        Activity activity= ContextManager.getManager().getContextActivity();

        // Get Elements
        okBrowserIncognitoWebView=fragmentView.findViewById(R.id.okBrowserIncognitoWebView);
        incognitoHomeLayout=fragmentView.findViewById(R.id.incognitoHomeLayout);
        connectFailedLayout=fragmentView.findViewById(R.id.connectFailedLayout);
        webViewLayout=fragmentView.findViewById(R.id.incognitoPageRootLayout);
        webViewVideoLayout=activity.findViewById(R.id.webViewVideoLayout);
        // Loading Video View
        View loadingView = getLayoutInflater().inflate(R.layout.layout_video_loading, null);
        // Default
        connectFailedLayout.setVisibility(View.GONE);
        // WebView Clients
        OKWebViewClient okWebViewClient=new OKWebViewClient();
        OKWebViewChromeClient okWebViewChromeClient=new OKWebViewChromeClient(webViewLayout, webViewVideoLayout, loadingView, okBrowserIncognitoWebView);
        // Set WebView Clients
        okBrowserIncognitoWebView.setOKWebViewClient(okWebViewClient);
        okBrowserIncognitoWebView.setOKWebViewChromeClient(okWebViewChromeClient);
        // Set WebView Listeners
        okBrowserIncognitoWebView.getOKWebViewChromeClient().setOnToggledFullscreen(VideoFullScreen.fullscreenCallback);
        okBrowserIncognitoWebView.setDownloadListener(new DownloadFileListener());
        // Set WebView Fragment
        okBrowserIncognitoWebView.setIncognitoTabFragment(this);
        // Set Variables
        okBrowserIncognitoWebView.isIncognitoWebView=true;
        // Set WebView Config
        WebViewConfig.getInstance().setWebViewConfig(okBrowserIncognitoWebView);

        // Register Context Menu
        this.registerForContextMenu(okBrowserIncognitoWebView);

        // Check Tab Argument
        if(getArguments()!=null) {
            if (!getArguments().getString(TabSettings.KEY_TAB_URL_SENDER).equals("")) {
                // Set Visibilities
                incognitoHomeLayout.setVisibility(View.GONE);
                okBrowserIncognitoWebView.setVisibility(View.VISIBLE);
                // Load Url
                okBrowserIncognitoWebView.loadUrl(getArguments().getString(TabSettings.KEY_TAB_URL_SENDER));
            }
        }
        return fragmentView;
    }

    public void setTabIndex(int tabIndex){
        activeTabIndex=tabIndex;
    }
    public int getTabIndex(){
        return activeTabIndex;
    }

    public OKWebView getWebView(){
        return okBrowserIncognitoWebView;
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
        fragmentScreen=ScreenManager.getScreenshot(fragmentView.findViewById(R.id.incognitoPageRootLayout));
    }
    public Bitmap getUpdatedScreenShot(){
        return fragmentScreen;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        // Get Hit Result
        WebView.HitTestResult result = okBrowserIncognitoWebView.getHitTestResult();
        View rootView=okBrowserIncognitoWebView.getRootView();

        // Get Classes
        JavascriptManager jsManager=JavascriptManager.getManager();

        // Init Javascript Manager
        jsManager.setWebView(okBrowserIncognitoWebView);

        // Get Menu Type
        int type=result.getType();
        float defBlurValue=0.5F;
        if(type == WebView.HitTestResult.IMAGE_TYPE){
            // Open Image Menu
            MenuWebViewContext.getImageContextMenu(result.getExtra())
                    .showAtLocation(okBrowserIncognitoWebView, Gravity.CENTER, 0,0);
            // Set Background Blur
            rootView.setAlpha(defBlurValue);
        }
        else if(type == WebView.HitTestResult.SRC_ANCHOR_TYPE){
            // Open Anchor Menu
            jsManager.getUrlAndTitleWithJavascript(MenuWebViewContext.KEY_MENU_ANCHOR,result.getExtra());
            // Set Background Blur
            rootView.setAlpha(defBlurValue);
        }
        else if(type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            // Open Image & Anchor Menu
            // Get Anchor URL
            Handler handler=new Handler();
            Message message=handler.obtainMessage();

            okBrowserIncognitoWebView.requestFocusNodeHref(message);
            String getMessageURL=message.getData().getString("url");

            // Check and Get Title, Url and Image URL
            if(URLUtil.isValidUrl(result.getExtra()))
                jsManager.getUrlAndTitleWithJavascript(MenuWebViewContext.KEY_MENU_IMAGE_ANCHOR, result.getExtra());
            else
                jsManager.getUrlAndTitleWithJavascript(MenuWebViewContext.KEY_MENU_ANCHOR, getMessageURL);
            // Set Background Blur
            rootView.setAlpha(defBlurValue);

        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
