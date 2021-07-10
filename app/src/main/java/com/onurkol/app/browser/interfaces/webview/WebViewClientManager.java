package com.onurkol.app.browser.interfaces.webview;

import com.onurkol.app.browser.webview.OKWebViewChromeClient;
import com.onurkol.app.browser.webview.OKWebViewClient;

public interface WebViewClientManager {
    // Client
    void setOKWebViewClient(OKWebViewClient webViewClient);
    OKWebViewClient getOKWebViewClient();

    // Chrome Client
    void setOKWebViewChromeClient(OKWebViewChromeClient webViewClient);
    OKWebViewChromeClient getOKWebViewChromeClient();
}
