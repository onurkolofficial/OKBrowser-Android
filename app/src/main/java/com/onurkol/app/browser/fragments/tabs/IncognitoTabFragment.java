package com.onurkol.app.browser.fragments.tabs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.lib.tabs.TabBuilder;
import com.onurkol.app.browser.tools.ScreenManager;
import com.onurkol.app.browser.webview.OKWebView;
import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;
import com.onurkol.app.browser.webview.WebViewConfig;

public class IncognitoTabFragment extends Fragment {
    // Elements
    OKWebView okBrowserIncognitoWebView;
    LinearLayout incognitoHomeLayout, connectFailedLayout;
    View fragmentView;

    // Types
    private Bitmap fragmentScreen;

    // Variables
    private int activeTabIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView=inflater.inflate(R.layout.fragment_new_incognito_tab, container, false);

        // Get Elements
        okBrowserIncognitoWebView=fragmentView.findViewById(R.id.okBrowserIncognitoWebView);
        incognitoHomeLayout=fragmentView.findViewById(R.id.incognitoHomeLayout);
        connectFailedLayout=fragmentView.findViewById(R.id.connectFailedLayout);
        // Default
        connectFailedLayout.setVisibility(View.GONE);

        // Set WebView Config
        WebViewConfig.getInstance().setWebViewConfig(okBrowserIncognitoWebView);
        // Set WebView Clients
        okBrowserIncognitoWebView.setOKWebViewClient(new OKWebViewClient());
        okBrowserIncognitoWebView.setOKWebViewChromeClient(new OKWebViewChromeClient());
        // Set WebView Fragment
        okBrowserIncognitoWebView.setIncognitoTabFragment(this);
        // Set Variables
        okBrowserIncognitoWebView.isIncognitoWebView=true;
        return fragmentView;
    }

    public void setActiveTabIndex(int tabIndex){
        activeTabIndex=tabIndex;
    }
    public int getActiveTabIndex(){
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
