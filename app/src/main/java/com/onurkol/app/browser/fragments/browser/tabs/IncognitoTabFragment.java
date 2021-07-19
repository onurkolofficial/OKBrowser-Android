package com.onurkol.app.browser.fragments.browser.tabs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.ContextManager;
import com.onurkol.app.browser.tools.ScreenManager;
import com.onurkol.app.browser.webview.OKWebView;
import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;
import com.onurkol.app.browser.webview.WebViewConfig;

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
        webViewLayout=fragmentView.findViewById(R.id.webViewLayout);
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
        // Set WebView Fragment
        okBrowserIncognitoWebView.setIncognitoTabFragment(this);
        // Set Variables
        okBrowserIncognitoWebView.isIncognitoWebView=true;
        // Set WebView Config
        WebViewConfig.getInstance().setWebViewConfig(okBrowserIncognitoWebView);


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
}
