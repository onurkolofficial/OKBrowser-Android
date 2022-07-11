package com.onurkol.app.browser.webview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.onurkol.app.browser.R;
import com.onurkol.app.browser.controller.browser.HistoryController;
import com.onurkol.app.browser.controller.settings.GUIController;
import com.onurkol.app.browser.controller.tabs.TabController;
import com.onurkol.app.browser.data.tabs.TabData;
import com.onurkol.app.browser.fragments.tabs.TabFragment;
import com.onurkol.app.browser.libs.JavascriptManager;
import com.onurkol.app.browser.libs.ScreenManager;

public class OKWebViewClient extends WebViewClient {
    TabController tabController;
    GUIController guiController;
    HistoryController historyController;
    Context wcContext;

    JavascriptManager javascriptManager;

    TabData currentTabData;

    boolean isPageFinished=false;

    public OKWebViewClient(Context context){
        tabController=TabController.getController(context);
        guiController=GUIController.getController();
        historyController=HistoryController.getController();
        javascriptManager=JavascriptManager.getManager();
        wcContext=context;
        currentTabData=tabController.getCurrentTabData();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        updateTabData(view);
        // Update View Data
        EditText browserToolbarSearchInput=((Activity)wcContext).findViewById(R.id.browserToolbarSearchInput);
        browserToolbarSearchInput.setText(url);
        // Check GUI Mode and update button state (DENSE MODE BOTTOM MENU)
        if(view.canGoForward())
            tabController.getCurrentTab().setBackForwardState(TabFragment.MENU_UI_CAN_FORWARD_BACK_STATE);
        else
            tabController.getCurrentTab().setBackForwardState(TabFragment.MENU_UI_CAN_BACK_NO_FORWARD_STATE);
        tabController.getCurrentTab().setMenuUIStateUpdate();
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        updateTabData(view);

        // <BUG-Fixed> WebView not scrolled with nested scroll.
        checkWebViewHeight((OKWebView)view);

        if(!isPageFinished){
            // Called onPageFinish 1 time in this block.
            javascriptManager.exec("window.scrollTo(0,0);");

            // Sync and Save History
            if(!tabController.getCurrentTab().isIncognito()) {
                historyController.syncHistoryData();
                historyController.newHistory(view.getTitle(), url); // Note! History add index always '0'.
            }
            isPageFinished=true;
        }
        else
            isPageFinished=false;

        // For get touch title
        javascriptManager.exec("var w=window;"
                + "function wrappedOnDownFunc(e){"
                + "  w._touchtarget = e.touches[0].target;"
                + "}"
                + "w.addEventListener('touchstart',wrappedOnDownFunc);");

        super.onPageFinished(view, url);
    }

    private void checkWebViewHeight(OKWebView webView){
        // <BUG> Clicked youtube menu button, webview height set to :wrap
        int getScreenHeigth = ScreenManager.getScreenHeight(wcContext);
        int getContentHeight = webView.getContentHeight();
        // Params
        LinearLayout.LayoutParams heightWrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams heightMatch = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Check View Layout Params
        if (getScreenHeigth >= getContentHeight || getContentHeight==0)
            webView.setLayoutParams(heightMatch);
        else
            webView.setLayoutParams(heightWrap);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        updateTabData(view);
        view.loadUrl(url);
        return true;
    }

    private void updateTabData(WebView view){
        // Update Tab Data (Finish)
        currentTabData.setUrl(view.getUrl());
        currentTabData.setTitle(view.getTitle());
        if(currentTabData.getTabFragment().isIncognito())
            tabController.replaceIncognitoTabData(currentTabData.getTabIndex(), currentTabData);
        else
            tabController.replaceTabData(currentTabData.getTabIndex(), currentTabData);
        // Save Preference (is not incognito)
        if(currentTabData.getTabFragment()!=null &&
                !currentTabData.getTabFragment().isIncognito() &&
                view.getUrl()!=null)
            tabController.saveTabDataPreference();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if(error.getErrorCode()==ERROR_CONNECT)
            tabController.getCurrentTab().setUIStateError();
        super.onReceivedError(view, request, error);
    }
}
