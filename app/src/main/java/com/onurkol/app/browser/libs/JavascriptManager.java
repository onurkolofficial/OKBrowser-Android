package com.onurkol.app.browser.libs;

import android.webkit.ValueCallback;

import com.onurkol.app.browser.webview.OKWebView;

public class JavascriptManager {
    private static JavascriptManager instance=null;
    OKWebView getWebView;

    public static synchronized JavascriptManager getManager(){
        if(instance==null)
            instance=new JavascriptManager();
        return instance;
    }

    public void setExecWebView(OKWebView webView){
        getWebView=webView;
    }
    public OKWebView getExecWebView(){
        return getWebView;
    }

    public void exec(String javascript){
        if(getWebView!=null)
            getWebView.evaluateJavascript(javascript, null);
    }
    public void exec(String javascript, ValueCallback<String> javascriptResultCallback){
        if(getWebView!=null)
            getWebView.evaluateJavascript(javascript, javascriptResultCallback);
    }
    public void exec(OKWebView view, String javascript){
        view.evaluateJavascript(javascript, null);
    }
    public void exec(OKWebView view, String javascript, ValueCallback<String> javascriptResultCallback){
        view.evaluateJavascript(javascript, javascriptResultCallback);
    }
}
