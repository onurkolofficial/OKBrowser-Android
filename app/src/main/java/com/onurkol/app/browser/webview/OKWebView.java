package com.onurkol.app.browser.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.interfaces.webview.OKWebViewInterface;

public class OKWebView extends WebView implements OKWebViewInterface {
    TabController tabController;
    Context wContext;

    OKWebViewClient okWebViewClient;
    OKWebViewChromeClient okWebViewChromeClient;

    public OKWebView(@NonNull Context context) {
        super(context);
        tabController=TabController.getController(context);
        wContext=context;
    }
    public OKWebView(@NonNull Context context, AttributeSet attrs){
        super(context, attrs);
        tabController=TabController.getController(context);
        wContext=context;
    }
    public OKWebView(@NonNull Context context, AttributeSet attrs, int defStyle){
        super(context, attrs);
        tabController=TabController.getController(context);
        wContext=context;
    }

    @Override
    public void setOKWebViewClient(@NonNull OKWebViewClient webViewClient) {
        okWebViewClient=webViewClient;
        setWebViewClient(webViewClient);
    }

    @Override
    public void setOKWebViewChromeClient(@NonNull OKWebViewChromeClient webViewChromeClient) {
        okWebViewChromeClient=webViewChromeClient;
        setWebChromeClient(webViewChromeClient);
    }

    @Override
    public OKWebViewClient getOKWebViewClient() {
        return okWebViewClient;
    }

    @Override
    public OKWebViewChromeClient getOKWebViewChromeClient() {
        return okWebViewChromeClient;
    }

}
