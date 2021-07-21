package com.onurkol.app.browser.tools;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.webkit.ValueCallback;

import com.onurkol.app.browser.menu.webview.MenuWebViewContext;
import com.onurkol.app.browser.webview.OKWebView;

public class JavascriptManager {
    private static JavascriptManager instance=null;
    OKWebView getWebView;

    public static synchronized JavascriptManager getManager(){
        if(instance==null)
            instance=new JavascriptManager();
        return instance;
    }

    public void setWebView(OKWebView webView){
        getWebView=webView;
    }
    public OKWebView getWebView(){
        return getWebView;
    }

    public void exec(String javascript){
        if(getWebView!=null)
            getWebView.evaluateJavascript(javascript,null);
    }
    public void exec(String javascript, ValueCallback<String> javascriptResultCallback){
        if(getWebView!=null)
            getWebView.evaluateJavascript(javascript,javascriptResultCallback);
    }
    public void exec(OKWebView view, String javascript){
        view.evaluateJavascript(javascript,null);
    }
    public void exec(OKWebView view, String javascript, ValueCallback<String> javascriptResultCallback){
        view.evaluateJavascript(javascript,javascriptResultCallback);
    }

    public void getUrlAndTitleWithJavascript(String type, String url){
        // Open Anchor Menu ( TabFragment & IncognitoTabFragment )
        ValueCallback<String> javascriptValueCallback=string -> {
            // Get Link Title
            String getLinkTitle=string.substring(1, (string.length() - 1));
            String getLink=url;

            // Get Current WebView
            OKWebView webView=getWebView();

            if(type.equals(MenuWebViewContext.KEY_MENU_IMAGE_ANCHOR)){
                // Get Handler URL
                Handler handler=new Handler();
                Message message=handler.obtainMessage();

                webView.requestFocusNodeHref(message);
                getLink=message.getData().getString("url");
                // Open Context Menu
                MenuWebViewContext.getImageAnchorContextMenu(getLink, webView.getHitTestResult().getExtra(), getLinkTitle)
                        .showAtLocation(webView, Gravity.CENTER, 0, 0);
            }
            else{
                // Open Context Menu
                MenuWebViewContext.getAnchorContextMenu(getLink, getLinkTitle)
                        .showAtLocation(webView, Gravity.CENTER, 0, 0);
            }
        };

        // Exec Javascript
        exec("window._touchtarget?window._touchtarget.innerText:''",javascriptValueCallback);
    }
}
