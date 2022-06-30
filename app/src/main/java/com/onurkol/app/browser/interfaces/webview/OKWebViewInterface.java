package com.onurkol.app.browser.interfaces.webview;

import androidx.annotation.NonNull;

import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;

public interface OKWebViewInterface {
    void setOKWebViewClient(@NonNull OKWebViewClient webViewClient);
    void setOKWebViewChromeClient(@NonNull OKWebViewChromeClient webViewChromeClient);
    OKWebViewClient getOKWebViewClient();
    OKWebViewChromeClient getOKWebViewChromeClient();
}
